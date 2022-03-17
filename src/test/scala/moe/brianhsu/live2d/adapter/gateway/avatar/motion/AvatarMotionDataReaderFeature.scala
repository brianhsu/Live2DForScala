package moe.brianhsu.live2d.adapter.gateway.avatar.motion

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.{Model, Parameter, PartOpacity}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.SegmentType._
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{CurveTarget, MotionCurve, SegmentType}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.{CustomSerializer, Formats, JString, NoTypeHints}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import scala.io.Source
import scala.util.Using

class AvatarMotionDataReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {

  Feature("Read pose parts data from Live2D avatar settings") {

    Scenario("Load motion idle[0] from avatar Mark") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionData = new AvatarMotionDataReader(settings.motionGroups("idle")(0)).loadMotionData()

      motionData.fps shouldBe 30.0
      motionData.isLoop shouldBe true
      motionData.curveCount shouldBe 37
      motionData.duration shouldBe 10.4f
      motionData.curves.size shouldBe 37
      motionData.curves should contain theSameElementsInOrderAs readCurvesFromFile("src/test/resources/expectation/motionData/markIdle0Curve.json")
    }

    Scenario("Load motion idle[1] from avatar Mark") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionData = new AvatarMotionDataReader(settings.motionGroups("idle")(1)).loadMotionData()

      motionData.fps shouldBe 30.0
      motionData.isLoop shouldBe true
      motionData.curveCount shouldBe 37
      motionData.duration shouldBe 4.8f

      val curves = motionData.curves
      curves.size shouldBe 37
      motionData.curves should contain theSameElementsInOrderAs readCurvesFromFile("src/test/resources/expectation/motionData/markIdle1Curve.json")

    }

    Scenario("Load motion idle[3] from avatar Mark") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionData = new AvatarMotionDataReader(settings.motionGroups("idle")(3)).loadMotionData()

      motionData.fps shouldBe 30.0
      motionData.isLoop shouldBe true
      motionData.curveCount shouldBe 37
      motionData.duration shouldBe 4.833f

      val curves = motionData.curves
      curves.size shouldBe 37
      motionData.curves should contain theSameElementsInOrderAs readCurvesFromFile("src/test/resources/expectation/motionData/markIdle3Curve.json")
    }

    Scenario("Load motion from avatar Hiyori") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Hiyori"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionData = new AvatarMotionDataReader(settings.motionGroups("idle")(0)).loadMotionData()

      motionData.fps shouldBe 30.0
      motionData.isLoop shouldBe true
      motionData.curveCount shouldBe 31
      motionData.duration shouldBe 4.7f

      val curves = motionData.curves
      curves.size shouldBe 31
      motionData.curves should contain theSameElementsInOrderAs readCurvesFromFile("src/test/resources/expectation/motionData/hiyoriIdle1Curve.json")
    }
  }
  object TargetTypeSerializer extends CustomSerializer[CurveTarget](_ => (
    {
      case JString("Model") => Model
      case JString("Parameter") => Parameter
      case JString("PartOpacity") => PartOpacity
    },
    {
      case Model => JString("Model")
      case Parameter => JString("Parameter")
      case PartOpacity => JString("PartOpacity")
    }
  ))
  object SegmentTypeSerializer extends CustomSerializer[SegmentType](_ => (
    {
      case JString("Linear") => Linear
      case JString("Bezier") => Bezier
      case JString("BezierCardanoInterpretation") => BezierCardanoInterpretation
      case JString("Stepped") => Stepped
      case JString("InverseStepped") => InverseStepped
    },
    {
      case Linear => JString("Linear")
      case Bezier => JString("Bezier")
      case BezierCardanoInterpretation => JString("BezierCardanoInterpretation")
      case Stepped => JString("Stepped")
      case InverseStepped => JString("InverseStepped")

    }
  ))

  private implicit val format: Formats = Serialization.formats(NoTypeHints) + TargetTypeSerializer + SegmentTypeSerializer

  private def readCurvesFromFile(filename: String): List[MotionCurve] = {
    val source = Source.fromFile(filename)
    val testData = Using.resource(source) { source => source.getLines().toList }
    testData.map(line => parse(line).extract[MotionCurve])
  }


}
