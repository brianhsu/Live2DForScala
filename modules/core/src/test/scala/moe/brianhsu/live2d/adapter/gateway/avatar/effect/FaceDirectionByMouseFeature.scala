package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class FaceDirectionByMouseFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  case class TimedData(interval: Float, viewX: Float, viewY: Float, expectedTargetX: Float, expectedTargetY: Float)

  Feature("The face direction target point calculator should calculate correct face target point") {
    Scenario("Doing a sequence of mouse drag") {
      Given("an sequence of interval / viewX / viewY data")
      val testDataList = loadTestData

      Then("it should calculate correct expectedTargetX / expectedTargetY from test data")
      val targetPointCalculator = new FaceDirectionByMouse(30)
      var totalElapsedTimeInSeconds = 0.0f

      for (row <- testDataList) {
        totalElapsedTimeInSeconds += row.interval
        targetPointCalculator.updateFaceTargetCoordinate(row.viewX, row.viewY)
        targetPointCalculator.updateFrameTimeInfo(totalElapsedTimeInSeconds, row.interval)
        val (faceX, faceY) = targetPointCalculator.currentFaceCoordinate

        faceX shouldBe row.expectedTargetX +- 0.0000000001f
        faceY shouldBe row.expectedTargetY +- 0.0000000001f
      }
    }
  }

  private def loadTestData: List[TimedData] = {
    val lines = Source.fromResource("expectation/faceDirectionTargetPointData.csv").getLines().toList
    lines.drop(1).map { line =>
      val Array(interval, viewX, viewY, expectedTargetX, expectedTargetY) = line.split(" ")
      TimedData(interval.toFloat, viewX.toFloat, viewY.toFloat, expectedTargetX.toFloat, expectedTargetY.toFloat)
    }
  }
}
