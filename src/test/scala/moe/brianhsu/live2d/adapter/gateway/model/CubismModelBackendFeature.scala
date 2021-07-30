package moe.brianhsu.live2d.adapter.gateway.model

import com.sun.jna.{Native, Pointer}
import moe.brianhsu.live2d.adapter.gateway.core.JnaCubismCore
import moe.brianhsu.live2d.adapter.gateway.core.memory.DefaultMemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI
import moe.brianhsu.live2d.enitiy.core.types._
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.enitiy.model.{CanvasInfo, Part}
import moe.brianhsu.porting.live2d.framework.exception._
import moe.brianhsu.porting.live2d.framework.util.MocFileReader
import moe.brianhsu.porting.live2d.framework.{Cubism, MocInfo}
import moe.brianhsu.porting.live2d.utils._
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, Inside, OptionValues, TryValues}

import scala.io.Source

class CubismModelBackendFeature extends AnyFeatureSpec with GivenWhenThen
                          with Matchers with Inside with OptionValues with MockFactory
                          with TableDrivenPropertyChecks with TryValues {

  private val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
  private val mockedTextureFiles = List("texture1.png", "texture2.png")
  private val cubism = new Cubism

  Feature("Update the model information") {
    Scenario("Update the model") {
      Given("a mocked Cubism Core Library and pointer to model")
      val mockedCLibrary = stub[NativeCubismAPI]
      val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
      val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

      And("a Cubism model backend")
      val model = new CubismModelBackend(null, Nil)(mockedCubismCore) {
        override lazy val cubismModel: CPointerToModel = mockedModel
      }

      When("update the model")
      model.update()

      Then("it should call the c library to update the model")
      (mockedCLibrary.csmUpdateModel _).verify(mockedModel)
      (mockedCLibrary.csmResetDrawableDynamicFlags _).verify(mockedModel)

    }
  }

  Feature("Reading model information") {
    Scenario("Reading canvas info from model") {
      Given("A Cubism HaruGreeter Model")
      val model = createModelBackend(modelFile)

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
      Given("A Cubism HaruGreeter Model")
      val model = createModelBackend(modelFile)

      When("Get the parts")
      val parts = model.parts

      Then("it should have correct number of parts")
      val expectedParts = Source.fromResource("expectation/partIdList.txt").getLines().toList
      parts.size shouldBe expectedParts.size

      expectedParts.foreach { partId =>
        And(s"part $partId should have correct values")

        val part = parts.get(partId).value
        inside(part) { case Part(opacityPointer, id, parentIdHolder) =>
          opacityPointer should not be null
          id shouldBe partId
          parentIdHolder shouldBe None
        }
        part.opacity shouldBe 1.0f
      }
    }

    Scenario("reading parts that has parent from model") {
      Given("a mocked Cubism Core Library and pointer to model")
      val mockedCLibrary = mock[NativeCubismAPI]
      val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
      val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))
      val opacities = mock[CArrayOfFloat]
      val ids = mock[CStringArray]
      val parents = mock[CArrayOfInt]

      And("there is three mocked parts")
      (opacities.getPointerToFloat _).expects(*).anyNumberOfTimes().returning(new Pointer(Native.malloc(4)))

      (ids.apply _).expects(0).anyNumberOfTimes().returning("id0")
      (ids.apply _).expects(1).anyNumberOfTimes().returning("id1")
      (ids.apply _).expects(2).anyNumberOfTimes().returning("id2")

      And("the second part's parent is first part, and third part contains invalid parent index")
      (parents.apply _).expects(0).anyNumberOfTimes().returning(-1)
      (parents.apply _).expects(1).anyNumberOfTimes().returning(0)
      (parents.apply _).expects(2).anyNumberOfTimes().returning(Int.MaxValue)

      (mockedCLibrary.csmGetPartCount _).expects(*).anyNumberOfTimes().returning(3)
      (mockedCLibrary.csmGetPartIds _).expects(*).anyNumberOfTimes().returning(ids)
      (mockedCLibrary.csmGetPartOpacities _).expects(*).anyNumberOfTimes().returning(opacities)
      (mockedCLibrary.csmGetPartParentPartIndices _).expects(*).anyNumberOfTimes().returning(parents)

      When("construct a Live2D model from that mocked data")
      val model = new CubismModelBackend(null, Nil)(mockedCubismCore) {
        override lazy val cubismModel: CPointerToModel = mockedModel
      }

      Then("only the second part has parentId")
      model.parts.get("id0").value.parentIdHolder shouldBe None
      model.parts.get("id1").value.parentIdHolder shouldBe Some("id0")
      model.parts.get("id2").value.parentIdHolder shouldBe None
    }

    Scenario("reading parameters data from model") {
      Given("A Cubism HaruGreeter Model")
      val model = createModelBackend(modelFile)

      When("Get the parameters")
      val parameters = model.parameters

      Then("it should have correct number of parameters")
      val expectedParameters = ExpectedParameter.getExpectedParameters
      parameters.size shouldBe expectedParameters.size

      expectedParameters.foreach { expectedParameter =>
        And(s"${expectedParameter.id} should have correct values")
        val parameter = parameters.get(expectedParameter.id).value
        inside(parameter) { case CPointerParameter(pointer, id, min, max, default) =>
          pointer should not be null
          id shouldBe expectedParameter.id
          default shouldBe expectedParameter.default
          min shouldBe expectedParameter.min
          max shouldBe expectedParameter.max
        }
        parameter.current shouldBe expectedParameter.current

      }
    }

    Scenario("reading drawables from the model") {
      Given("A Cubism HaruGreeter Model")
      val model = createModelBackend(modelFile)

      When("Get the drawables")
      val drawables = model.drawables

      Then("the basic information of drawables should be correct")
      ExpectedDrawableBasic.getList.foreach { expectedBasicInfo =>
        val drawable = drawables(expectedBasicInfo.id)
        inside(drawable) { case Drawable(id, index, constantFlags, dynamicFlags, textureIndex, masks,
                                         vertexInfo, drawOrderPointer, renderOrderPointer, opacityPointer) =>
          id shouldBe expectedBasicInfo.id
          index shouldBe >= (0)
          constantFlags.bitmask shouldBe expectedBasicInfo.constFlags
          dynamicFlags.bitmask shouldBe expectedBasicInfo.dynamicFlags
          textureIndex shouldBe expectedBasicInfo.textureIndex
          masks.size shouldBe expectedBasicInfo.masksSize
          vertexInfo.positions.size shouldBe expectedBasicInfo.positionsSize
          vertexInfo.textureCoordinates.size shouldBe expectedBasicInfo.textureCoordinatesSize
          vertexInfo.indices.size shouldBe expectedBasicInfo.indexSize
          drawOrderPointer should not be null
          renderOrderPointer should not be null
          opacityPointer should not be null
        }
        drawable.renderOrder shouldBe expectedBasicInfo.renderOrder
        drawable.drawOrder shouldBe expectedBasicInfo.drawOrder
        drawable.opacity shouldBe expectedBasicInfo.opacity
      }

      And("the masks of drawables should be correct")
      ExpectedDrawableMask.getList.foreach { expectedDrawableMask =>
        val drawableId = expectedDrawableMask.id
        val index = expectedDrawableMask.index
        val drawable = drawables(drawableId)
        val mask = drawable.masks(index)
        mask shouldBe expectedDrawableMask.maskValue
      }

      And("the positions of drawables should be correct")
      ExpectedDrawablePosition.getList.foreach { expectedDrawablePosition =>
        val drawableId = expectedDrawablePosition.id
        val drawable = drawables(drawableId)
        val index = expectedDrawablePosition.index
        val position = drawable.vertexInfo.positions(index)

        position shouldBe (expectedDrawablePosition.x, expectedDrawablePosition.y)
      }

      And("the texture coordinate of drawables should be correct")
      ExpectedDrawableCoordinate.getList.foreach { expectedDrawableCoordinate =>
        val drawableId = expectedDrawableCoordinate.id
        val drawable = drawables(drawableId)
        val index = expectedDrawableCoordinate.index
        val coordinates = drawable.vertexInfo.textureCoordinates(index)
        coordinates shouldBe (expectedDrawableCoordinate.x, expectedDrawableCoordinate.y)
      }

      And("the triangle index of drawables should be correct")
      ExpectedDrawableIndex.getList.foreach { expectedDrawableIndex =>
        val drawable = drawables(expectedDrawableIndex.id)
        val index = expectedDrawableIndex.index
        drawable.vertexInfo.indices(index) shouldBe expectedDrawableIndex.value
      }

      And("the vertex array direct buffer should point to correct location and can access correct data")
      ExpectedDrawablePosition.getList.foreach { expectedDrawablePosition =>
        val drawableId = expectedDrawablePosition.id
        val drawable = drawables(drawableId)
        val index = expectedDrawablePosition.index
        val buffer = drawable.vertexInfo.vertexArrayDirectBuffer

        buffer.asFloatBuffer.get(index * 2) shouldBe expectedDrawablePosition.x
        buffer.asFloatBuffer.get(index * 2 + 1) shouldBe expectedDrawablePosition.y
      }

      And("the uv array direct buffer should point to correct location and can access correct data")
      ExpectedDrawableCoordinate.getList.foreach { expectedDrawableCoordinate =>
        val drawableId = expectedDrawableCoordinate.id
        val drawable = drawables(drawableId)
        val index = expectedDrawableCoordinate.index
        val buffer = drawable.vertexInfo.uvArrayDirectBuffer

        buffer.asFloatBuffer.get(index * 2) shouldBe expectedDrawableCoordinate.x
        buffer.asFloatBuffer.get(index * 2 + 1) shouldBe expectedDrawableCoordinate.y
      }

      And("the direct buffer of triangle index of drawables should be correct")
      ExpectedDrawableIndex.getList.foreach { expectedDrawableIndex =>
        val drawable = drawables(expectedDrawableIndex.id)
        val index = expectedDrawableIndex.index
        val buffer = drawable.vertexInfo.indexArrayDirectBuffer

        buffer.asShortBuffer().get(index) shouldBe expectedDrawableIndex.value
      }

    }
  }

  Feature("Error handling when reading the model") {
    Scenario("It should throw TextureSizeMismatch exception when textureFiles size is not correct") {

      val invalidCombos = Table(
        "textureFiles",
        Nil,
        List("textureFile1.png")
      )

      forAll(invalidCombos) { textureFiles =>
        Given("A Live2D HaruGreeter Model that does not match the information in the model")
        Then("it should throw TextureSizeMismatchException")
        a[TextureSizeMismatchException] should be thrownBy {
          val model = createModelBackend(modelFile, Nil)
          model.drawables
        }
      }
    }

    Scenario("Cannot revive the moc file") {
      Given("a not initialized memory of MocInfo")
      val memoryInfo = DefaultMemoryAllocator.allocate(1024, MocAlignment)
      val mocInfo = MocInfo(memoryInfo, 1024)

      And("create a Live2D model from that memory")
      val model = new CubismModelBackend(mocInfo, mockedTextureFiles)(new JnaCubismCore())

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
        val mockedCLibrary = mock[NativeCubismAPI]
        val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
        val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

        (mockedCLibrary.csmGetParameterCount _).expects(*).returning(count)
        (mockedCLibrary.csmGetParameterIds _).expects(*).returning(ids)
        (mockedCLibrary.csmGetParameterValues _).expects(*).returning(current)
        (mockedCLibrary.csmGetParameterDefaultValues _).expects(*).returning(default)
        (mockedCLibrary.csmGetParameterMinimumValues _).expects(*).returning(min)
        (mockedCLibrary.csmGetParameterMaximumValues _).expects(*).returning(max)


        And("a Live2D model")
        val model = new CubismModelBackend(null, mockedTextureFiles)(mockedCubismCore) {
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
        val mockedCLibrary = mock[NativeCubismAPI]
        val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
        val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

        (mockedCLibrary.csmGetPartCount _).expects(*).returning(count)
        (mockedCLibrary.csmGetPartIds _).expects(*).returning(ids)
        (mockedCLibrary.csmGetPartParentPartIndices _).expects(*).returning(parents)
        (mockedCLibrary.csmGetPartOpacities _).expects(*).returning(opacities)


        And("a Live2D model")
        val model = new CubismModelBackend(null, mockedTextureFiles)(mockedCubismCore) {
          override lazy val cubismModel: CPointerToModel = mockedModel
        }

        When("get parameters of this model")
        Then("it should throw ParameterInitializedException")
        a [PartInitException] should be thrownBy {
          model.parts
        }
      }
    }

    Scenario("The C library return invalid data when reading drawables") {
      val cStrings = new CStringArray
      val cFloats = new CArrayOfFloat
      val cBytes = new CArrayOfByte
      val cInts = new CArrayOfInt
      val cAAInt = new CArrayOfArrayOfInt
      val cAAShort = new CArrayOfArrayOfShort
      val cAAVector = new CArrayOfArrayOfCsmVector

      val invalidCombos = Table(
        ("count",    "ids", "cFlags", "dFlags", "textureIndex", "drawOrder", "renderOrder", "opacities", "maskCounts", "maskLists", "indexCountList", "indexList", "vCountList",   "vList",   "uvList"),
        (-1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,          null,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,     null,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,     null,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,           null,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,        null,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,          null,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,        null,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,         null,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,        null,            cInts,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,             null,    cAAShort,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,        null,        cInts, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,         null, cAAVector,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts,      null,  cAAVector),
        ( 1,      cStrings,   cBytes,   cBytes,          cInts,       cInts,         cInts,     cFloats,        cInts,      cAAInt,            cInts,    cAAShort,        cInts, cAAVector,       null),
      )

      forAll(invalidCombos) { (count, ids, cFlags, dFlags, textureIndex, drawOrder,
                               renderOrder, opacities, maskCounts, maskLists, indexCountList,
                               indexList, vCountList, vList, uvList) =>

        Given("a mocked Cubism Core Library and pointer to model")
        val mockedCLibrary = mock[NativeCubismAPI]
        val mockedCubismCore = new MockedCubismCore(mockedCLibrary)
        val mockedModel = new CPointerToModel(new Pointer(Native.malloc(1024)))

        (mockedCLibrary.csmGetDrawableCount _).expects(*).returning(count)
        (mockedCLibrary.csmGetDrawableIds _).expects(*).returning(ids)
        (mockedCLibrary.csmGetDrawableConstantFlags _).expects(*).returning(cFlags)
        (mockedCLibrary.csmGetDrawableDynamicFlags _).expects(*).returning(dFlags)
        (mockedCLibrary.csmGetDrawableTextureIndices _).expects(*).returning(textureIndex)
        (mockedCLibrary.csmGetDrawableDrawOrders _).expects(*).returning(drawOrder)
        (mockedCLibrary.csmGetDrawableRenderOrders _).expects(*).returning(renderOrder)
        (mockedCLibrary.csmGetDrawableOpacities _).expects(*).returning(opacities)
        (mockedCLibrary.csmGetDrawableMaskCounts _).expects(*).anyNumberOfTimes().returning(maskCounts)
        (mockedCLibrary.csmGetDrawableMasks _).expects(*).anyNumberOfTimes().returning(maskLists)
        (mockedCLibrary.csmGetDrawableIndexCounts _).expects(*).anyNumberOfTimes().returning(indexCountList)
        (mockedCLibrary.csmGetDrawableIndices _).expects(*).anyNumberOfTimes().returning(indexList)
        (mockedCLibrary.csmGetDrawableVertexCounts _).expects(*).anyNumberOfTimes().returning(vCountList)
        (mockedCLibrary.csmGetDrawableVertexPositions _).expects(*).anyNumberOfTimes().returning(vList)
        (mockedCLibrary.csmGetDrawableVertexUvs _).expects(*).anyNumberOfTimes().returning(uvList)

        And("a Live2D model")
        val model = new CubismModelBackend(null, Nil)(mockedCubismCore) {
          override lazy val cubismModel: CPointerToModel = mockedModel
        }

        When("get parameters of this model")
        Then("it should throw ParameterInitializedException")
        a [DrawableInitException] should be thrownBy {
          model.drawables
        }
      }
    }

  }

  private def createModelBackend(mocFilename: String, textureFiles: List[String] = mockedTextureFiles): ModelBackend = {
    val core = new JnaCubismCore()
    val fileReader = new MocFileReader(core.memoryAllocator)
    val mocInfo = fileReader.readFile(mocFilename)
    new CubismModelBackend(mocInfo, textureFiles)(core)

  }
}
