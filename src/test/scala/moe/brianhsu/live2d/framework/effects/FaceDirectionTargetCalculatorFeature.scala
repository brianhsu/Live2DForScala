package moe.brianhsu.live2d.framework.effects

import moe.brianhsu.live2d.framework.effect.FaceDirectionTargetCalculator
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class FaceDirectionTargetCalculatorFeature extends AnyFeatureSpec with GivenWhenThen with Matchers{

  case class TimedData(interval: Float, viewX: Float, viewY: Float, expectedTargetX: Float, expectedTargetY: Float)

  Feature("The face direction target point calculator should calculate correct face target point") {
    Given("An sequence of interval / viewX / viewY data")
    val testDataList = loadTestData

    Then("it should calculate correct expectedTargetX / expectedTargetY from test data")
    val targetPointCalculator = new FaceDirectionTargetCalculator(30)

    for (row <- testDataList) {
      targetPointCalculator.setFaceTargetCoordinate(row.viewX, row.viewY)
      targetPointCalculator.update(row.interval)
      val (faceX, faceY) = targetPointCalculator.getFaceCoordinate
      faceX shouldBe row.expectedTargetX
      faceY shouldBe row.expectedTargetY
    }
  }

  private def loadTestData: List[TimedData] = {
    val lines = Source.fromResource("effects/FaceDirectionTargetPointData.csv").getLines().toList
    lines.map { line =>
      val Array(interval, viewX, viewY, expectedTargetX, expectedTargetY) = line.split(" ")
      TimedData(interval.toFloat, viewX.toFloat, viewY.toFloat, expectedTargetX.toFloat, expectedTargetY.toFloat)
    }
  }
}
