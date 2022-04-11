package moe.brianhsu.live2d.enitiy.avatar.updater

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class SystemNanoTimeBasedFrameInfoFeature extends AnyFeatureSpec with Matchers with GivenWhenThen {
  Feature("Calculate correct frame time info") {
    Scenario("Before any frame updated") {
      Given("A SystemNanoTimeBasedFrameInfo")
      val frameTimeInfo = new SystemNanoTimeBasedFrameInfo

      When("Before any updates")

      Then("The deltaTimeInSeconds and totalElapsedTimeInSeconds should be zero")
      frameTimeInfo.deltaTimeInSeconds shouldBe 0
      frameTimeInfo.totalElapsedTimeInSeconds shouldBe 0
    }

    Scenario("Update for first frame") {
      Given("A SystemNanoTimeBasedFrameInfo")
      val frameTimeInfo = new SystemNanoTimeBasedFrameInfo

      When("Update at certain 1 frame mocked time")
      val mockedTimestamp = 6275206995111L
      frameTimeInfo.updateFrameTime(mockedTimestamp)

      Then("The deltaTimeInSeconds and totalElapsedTimeInSeconds should be zero")
      frameTimeInfo.deltaTimeInSeconds shouldBe 0
      frameTimeInfo.totalElapsedTimeInSeconds shouldBe 0
    }

    Scenario("Update two frames") {
      Given("A SystemNanoTimeBasedFrameInfo")
      val frameTimeInfo = new SystemNanoTimeBasedFrameInfo

      When("Update at certain 1 frame mocked time (60fps, 1st frame)")
      val mockedTimestamp = 6275206995111L
      frameTimeInfo.updateFrameTime(mockedTimestamp)

      And("Update after 0.016666666666666666 seconds (60fps, 2nd frame)")
      val secondFrameTimestamp = mockedTimestamp + (0.016666666666666666 * 1000 * 10000000).toLong
      frameTimeInfo.updateFrameTime(secondFrameTimestamp)

      Then("The deltaTimeInSeconds and totalElapsedTimeInSeconds should be zero")
      frameTimeInfo.deltaTimeInSeconds shouldBe 0.16666667f
      frameTimeInfo.totalElapsedTimeInSeconds shouldBe 0.16666667f
    }

    Scenario("Update three frames") {
      Given("A SystemNanoTimeBasedFrameInfo")
      val frameTimeInfo = new SystemNanoTimeBasedFrameInfo

      When("Update at certain 1 frame mocked time (60fps, 1st frame)")
      val mockedTimestamp = 6275206995111L
      frameTimeInfo.updateFrameTime(mockedTimestamp)

      And("Update after 0.016666666666666666 seconds (60fps, 2nd frame)")
      val duration = (0.016666666666666666 * 1000 * 10000000).toLong
      val secondFrameTimestamp = mockedTimestamp + duration
      frameTimeInfo.updateFrameTime(secondFrameTimestamp)

      And("Update again 0.016666666666666666 seconds (60fps, 2nd frame)")
      val thirdFrameTimestamp = secondFrameTimestamp + duration
      frameTimeInfo.updateFrameTime(thirdFrameTimestamp)

      Then("The deltaTimeInSeconds and totalElapsedTimeInSeconds should be zero")
      frameTimeInfo.deltaTimeInSeconds shouldBe 0.16666667f
      frameTimeInfo.totalElapsedTimeInSeconds shouldBe 0.33333334f
    }

  }

}
