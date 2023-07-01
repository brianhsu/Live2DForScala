package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI
import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.live2d.exception.MocNotRevivedException
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class MocInfoFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TableDrivenPropertyChecks {

  Feature("Error handling of uninitialized memory") {
    Scenario("Try to access revivedMoc from an uninitialized memory") {
      Given("a MocInfo with an uninitialized memory")
      val core = new JnaNativeCubismAPILoader()
      val memoryInfo = JnaMemoryAllocator.allocate(1024, MocAlignment)
      val mocInfo = MocInfo(memoryInfo, 1024)(core)

      When("access the revivedMoc field")
      Then("it should throw MocNotRevivedException")
      a[MocNotRevivedException] shouldBe thrownBy {
        mocInfo.revivedMoc
      }
    }
  }

  Feature("Delegated the consistency check to core library") {
    Scenario("Check consistency of a .moc") {

      val table = Table(
        ("return value", "expected boolean"),
        (0,              false),
        (1,              true)
      )

      forAll(table) { case (returnValue, expectedBoolean) =>
        Given(s"A mocked cubismAPI.csmHasMocConsistency always return $returnValue")
        val stubNativeAPI = stub[NativeCubismAPI]
        val core = new NativeCubismAPILoader {
          override implicit val memoryAllocator: MemoryAllocator = stub[MemoryAllocator]
          override val cubismAPI: NativeCubismAPI = stubNativeAPI
        }
        (stubNativeAPI.csmHasMocConsistency _).when(*, *).returning(returnValue)

        And("a MocInfo based on that")
        val mockedMemoryInfo = mock[MemoryInfo]
        val stubFileSize = 1024
        val mocInfo = MocInfo(mockedMemoryInfo, stubFileSize)(core)

        When("get the consistent of that mocInfo")
        val isConsistent = mocInfo.isConsistent

        Then(s"it should be $expectedBoolean")
        isConsistent shouldBe expectedBoolean
      }
    }
  }
}
