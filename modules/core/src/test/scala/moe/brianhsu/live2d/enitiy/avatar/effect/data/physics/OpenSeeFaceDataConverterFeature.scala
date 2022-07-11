package moe.brianhsu.live2d.enitiy.avatar.effect.data.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.data.OpenSeeFaceDataConverter
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.TrackingNode
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import org.json4s.native.{JsonMethods, Serialization}
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import java.io.PrintWriter
import scala.io.Source
import scala.util.Using

class OpenSeeFaceDataConverterFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  private implicit val jsonFormats: Formats = DefaultFormats

  Feature("Convert the OpenSeeFaceData into TrackingNode") {
    Scenario("Convert the data with default settings") {
      Given("a converter")
      val converter = new OpenSeeFaceDataConverter
      val expectationList = readExpectations()

      for (expectation <- expectationList) {
        When(s"convert data from ${expectation.currentData}")
        val trackingNode = converter.convert(expectation.currentData, expectation.previousLeftEyeNodes, expectation.previousRightEyeNodes)

        Then("it should have correct data")
        trackingNode shouldBe expectation.trackingNode
      }
    }

    Scenario("Convert the data with custom settings") {
      val table = Table(
        ("eyeSmileEyeOpenThreshold", "eyeSmileMouthFormThreshold", "eyeSmileMouthOpenThreshold", "expectedEyeSmile"),
        (5.0f, 1.0f, 1.0f, 0.0f),
        (1.0f, 1.0f, 1.0f, 0.0f),
        (1.0f, 0.5f, 1.0f, 0.0f),
        (5.0f, 0.5f, 0.6f, 1.0f),
      )

      forAll(table) { case (eyeSmileEyeOpenThreshold, eyeSmileMouthFormThreshold, eyeSmileMouthOpenThreshold, expectedEyeSmile) =>
        Given("a data point that ")
        val data = readExpectations()(145)

        When("convert the data through converter with custom settings")
        val settings = OpenSeeFaceDataConverter.DefaultSettings.copy(
          eyeSmileEyeOpenThreshold = eyeSmileEyeOpenThreshold,
          eyeSmileMouthFormThreshold = eyeSmileMouthFormThreshold,
          eyeSmileMouthOpenThreshold = eyeSmileMouthOpenThreshold
        )
        val converter = new OpenSeeFaceDataConverter(settings)

        Then("it should have correct converted values")
        val trackingNode = converter.convert(data.currentData, data.previousLeftEyeNodes, data.previousRightEyeNodes)
        trackingNode.leftEyeSmile shouldBe expectedEyeSmile
        trackingNode.rightEyeSmile shouldBe expectedEyeSmile
      }

    }

  }

  private def readExpectations(): List[Result] = {
    Using.Manager { use =>
      val inputStream = use(this.getClass.getResourceAsStream("/expectation/openSeeFaceToTrackingNode.json"))
      val jsonDataList = use(Source.fromInputStream(inputStream)).getLines().toList
      jsonDataList.map(line => JsonMethods.parse(line).extract[Result])
    }.get
  }

  case class Result(currentData: OpenSeeFaceData,
                    previousLeftEyeNodes: List[TrackingNode],
                    previousRightEyeNodes: List[TrackingNode],
                    trackingNode: TrackingNode)

}
