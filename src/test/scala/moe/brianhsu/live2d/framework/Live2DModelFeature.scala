package moe.brianhsu.live2d.framework

import com.sun.jna.{Native, Pointer}
import moe.brianhsu.live2d.core.{CubismCore, CubismCoreCLibrary}
import moe.brianhsu.live2d.core.types.{CArrayOfFloat, CArrayOfInt, CPointerToModel, CStringArray, MocAlignment}
import moe.brianhsu.live2d.core.utils.DefaultMemoryAllocator
import moe.brianhsu.live2d.framework.exception.{MocNotRevivedException, ParameterInitException, PartInitException}
import moe.brianhsu.live2d.framework.model.{CanvasInfo, Live2DModel, Part}
import moe.brianhsu.live2d.utils.{ExpectedParameter, MockedCubismCore}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Inside, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.io.Source

class Live2DModelFeature extends AnyFeatureSpec with GivenWhenThen
                          with Matchers with Inside with OptionValues with MockFactory
                          with TableDrivenPropertyChecks {

  private val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
  private val cubism = new Cubism

  Feature("Reading model information") {
    Scenario("Reading canvas info from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Get the canvas info")
      val canvasInfo = model.canvasInfo

      Then("it should return the correct canvas info")
      inside(canvasInfo) { case CanvasInfo(widthInPixel, heightInPixel, (originX, originY), pixelPerUnit) =>
        widthInPixel shouldBe 2400
        heightInPixel shouldBe 4500
        originX shouldBe 1200
        originY shouldBe 2250
        pixelPerUnit shouldBe 2400
      }

      canvasInfo.width shouldBe 1
      canvasInfo.height shouldBe 1.875
    }

    Scenario("reading parts that has no parent from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Get the parts")
      val parts = model.parts

      Then("it should have correct number of parts")
      val expectedParts = Source.fromResource("expectation/PartIdList.txt").getLines().toList
      parts.size shouldBe expectedParts.size

      And("all expected part id should have corresponding Part object")
      expectedParts.foreach { partId =>
        info(s"Validate $partId")

        val part = parts.get(partId)
        part.value shouldBe a[Part]
        part.value.id shouldBe partId
        part.value.parentIdHolder shouldBe None
        part.value.opacity shouldBe 1.0f
      }
    }

    Scenario("reading parts that has parent from model") {
      Given("a mocked Cubism Core Library and pointer to model")
      val mockedCLibrary = mock[CubismCoreCLibrary]
      val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
      val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))
      val opacities = mock[CArrayOfFloat]
      val ids = mock[CStringArray]
      val parents = mock[CArrayOfInt]

      And("there is three mocked parts")
      (opacities.getPointerToFloat _).expects(*).anyNumberOfTimes.returning(new Pointer(Native.malloc(4)))

      (ids.apply _).expects(0).anyNumberOfTimes.returning("id0")
      (ids.apply _).expects(1).anyNumberOfTimes.returning("id1")
      (ids.apply _).expects(2).anyNumberOfTimes.returning("id2")

      And("the second part's parent is first part, and third part contains invalid parent index")
      (parents.apply _).expects(0).anyNumberOfTimes.returning(-1)
      (parents.apply _).expects(1).anyNumberOfTimes.returning(0)
      (parents.apply _).expects(2).anyNumberOfTimes.returning(Int.MaxValue)

      (mockedCLibrary.csmGetPartCount _).expects(*).anyNumberOfTimes.returning(3)
      (mockedCLibrary.csmGetPartIds _).expects(*).anyNumberOfTimes.returning(ids)
      (mockedCLibrary.csmGetPartOpacities _).expects(*).anyNumberOfTimes.returning(opacities)
      (mockedCLibrary.csmGetPartParentPartIndices _).expects(*).anyNumberOfTimes.returning(parents)

      When("construct a Live2D model from that mocked data")
      val model = new Live2DModel(null)(mockedCubismCore) {
        override lazy val cubismModel: CPointerToModel = mockedModel
      }

      Then("only the second part has parentId")
      model.parts.get("id0").value.parentIdHolder shouldBe None
      model.parts.get("id1").value.parentIdHolder shouldBe Some("id0")
      model.parts.get("id2").value.parentIdHolder shouldBe None
    }

    Scenario("reading parameters data from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Get the parts")
      val parameters = model.parameters

      Then("it should have correct number of parameters")
      val expectedParameters = ExpectedParameter.getExpectedParameters
      parameters.size shouldBe expectedParameters.size

      And("all expected parameters should have corresponding Parameter object")
      expectedParameters.foreach { expectedParameter =>
        info(s"Validate ${expectedParameter.id}")
        val parameter = parameters.get(expectedParameter.id)
        parameter.value.id shouldBe expectedParameter.id
        parameter.value.current shouldBe expectedParameter.current
        parameter.value.default shouldBe expectedParameter.default
        parameter.value.min shouldBe expectedParameter.min
        parameter.value.max shouldBe expectedParameter.max

      }
    }

    Scenario("Update the model") {
      Given("a mocked Cubism Core Library and pointer to model")
      val mockedCLibrary = stub[CubismCoreCLibrary]
      val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
      val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

      And("a Live2D model")
      val model = new Live2DModel(null)(mockedCubismCore) {
        override lazy val cubismModel: CPointerToModel = mockedModel
      }

      When("update the model")
      model.update()

      Then("it should call the c library to update the model")
      (mockedCLibrary.csmUpdateModel _).verify(mockedModel)
      (mockedCLibrary.csmResetDrawableDynamicFlags _).verify(mockedModel)

    }

  }

  Feature("Error handling when reading the model") {
    Scenario("Cannot revive the moc file") {
      Given("a not initialized memory of MocInfo")
      val memoryInfo = DefaultMemoryAllocator.allocate(1024, MocAlignment)
      val mocInfo = MocInfo(memoryInfo, 1024)

      And("create a Live2D model from that memory")
      val model = new Live2DModel(mocInfo)(new CubismCore())

      When("reading the internal cubismModel parameters")
      Then("it should throw MocNotRevivedException")
      a [MocNotRevivedException] should be thrownBy {
        model.parameters
      }
    }

    Scenario("The C library return invalid data when reading parameters") {
      val cStrings = new CStringArray
      val cFloats = new CArrayOfFloat

      val invalidCombos = Table(
        ("count",    "ids", "current", "default",   "min",   "max"),
        (-1,      cStrings,   cFloats,   cFloats, cFloats, cFloats),
        ( 1,          null,   cFloats,   cFloats, cFloats, cFloats),
        ( 1,      cStrings,      null,   cFloats, cFloats, cFloats),
        ( 1,      cStrings,   cFloats,      null, cFloats, cFloats),
        ( 1,      cStrings,   cFloats,   cFloats,    null, cFloats),
        ( 1,      cStrings,   cFloats,   cFloats, cFloats,    null)
      )

      forAll(invalidCombos) { (count, ids, current, default, min, max) =>
        Given("a mocked Cubism Core Library and pointer to model")
        val mockedCLibrary = mock[CubismCoreCLibrary]
        val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
        val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

        (mockedCLibrary.csmGetParameterCount _).expects(*).returning(count)
        (mockedCLibrary.csmGetParameterIds _).expects(*).returning(ids)
        (mockedCLibrary.csmGetParameterValues _).expects(*).returning(current)
        (mockedCLibrary.csmGetParameterDefaultValues _).expects(*).returning(default)
        (mockedCLibrary.csmGetParameterMinimumValues _).expects(*).returning(min)
        (mockedCLibrary.csmGetParameterMaximumValues _).expects(*).returning(max)


        And("a Live2D model")
        val model = new Live2DModel(null)(mockedCubismCore) {
          override lazy val cubismModel: CPointerToModel = mockedModel
        }

        When("get parameters of this model")
        Then("it should throw ParameterInitializedException")
        a [ParameterInitException] should be thrownBy {
          model.parameters
        }
      }
    }

    Scenario("The C library return invalid data when reading parts") {
      val cStrings = new CStringArray
      val cFloats = new CArrayOfFloat
      val cInts = new CArrayOfInt

      val invalidCombos = Table(
        ("count",    "ids", "opacities", "parents"),
        (-1,      cStrings,     cFloats,     cInts),
        ( 1,          null,     cFloats,     cInts),
        ( 1,      cStrings,        null,     cInts),
        ( 1,      cStrings,     cFloats,      null),
      )

      forAll(invalidCombos) { (count, ids, opacities, parents) =>
        Given("a mocked Cubism Core Library and pointer to model")
        val mockedCLibrary = mock[CubismCoreCLibrary]
        val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
        val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

        (mockedCLibrary.csmGetPartCount _).expects(*).returning(count)
        (mockedCLibrary.csmGetPartIds _).expects(*).returning(ids)
        (mockedCLibrary.csmGetPartParentPartIndices _).expects(*).returning(parents)
        (mockedCLibrary.csmGetPartOpacities _).expects(*).returning(opacities)


        And("a Live2D model")
        val model = new Live2DModel(null)(mockedCubismCore) {
          override lazy val cubismModel: CPointerToModel = mockedModel
        }

        When("get parameters of this model")
        Then("it should throw ParameterInitializedException")
        a [PartInitException] should be thrownBy {
          model.parts
        }
      }
    }

  }
}
