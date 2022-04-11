package moe.brianhsu.live2d.adapter.gateway.model

import com.sun.jna.Memory
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI
import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, TryValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.{Files, NoSuchFileException, Paths}

class MocInfoFileReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TryValues {

  Feature("Read .moc3 from file") {

    Scenario("Read exist and correct .mocFile into mocInfo") {
      Given("A exist .moc3 file")
      val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
      val fileContent = Files.readAllBytes(Paths.get(modelFile))

      And("A set of mocked memory to be allocated and written")
      val mockedOriginalMemory = mock[MockableMemory]
      val mockedAlignedMemory = mock[MockableMemory]
      val mockedMemoryInfo = MemoryInfo(mockedOriginalMemory, mockedAlignedMemory)
      val mockedMemoryAllocator = mock[MemoryAllocator]
      implicit val core: NativeCubismAPILoader = createMockedCubismCore(mockedMemoryAllocator)

      (mockedMemoryAllocator.allocate _).expects(fileContent.size, MocAlignment).returning(mockedMemoryInfo).once()
      (mockedAlignedMemory.write: (Long, Array[Byte], Int, Int) => Unit).expects(0, *, 0, fileContent.size).once()

      When("Read .moc file using MocInfoFileReader")
      val mocInfoFileReader = new MocInfoFileReader(modelFile)

      Then("it should be a success")
      val mocInfo = mocInfoFileReader.loadMocInfo().success.value

      And("it should return correct information")
      mocInfo.memory shouldBe mockedMemoryInfo
      mocInfo.originalSize shouldBe fileContent.size
    }

    Scenario("Read non-exist .mocFile into mocInfo") {
      Given("A set of mocked memory")
      val mockedMemoryAllocator = mock[MemoryAllocator]
      implicit val core: NativeCubismAPILoader = createMockedCubismCore(mockedMemoryAllocator)

      When("Read .moc file using MocInfoFileReader")
      val mocInfoFileReader = new MocInfoFileReader("nonExistFile")

      Then("it should be a Failure[NoSuchFileException]")
      val exception = mocInfoFileReader.loadMocInfo().failure.exception
      exception shouldBe a[NoSuchFileException]
    }

  }

  private def createMockedCubismCore(mockedMemoryAllocator: MemoryAllocator) = {
    new NativeCubismAPILoader {
      override implicit val memoryAllocator: MemoryAllocator = mockedMemoryAllocator
      override val cubismAPI: NativeCubismAPI = stub[NativeCubismAPI]
    }
  }

  class MockableMemory extends Memory(1024)

}
