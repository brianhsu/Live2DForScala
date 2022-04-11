package moe.brianhsu.live2d.enitiy.avatar

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateStrategy
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class AvatarFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {

  Feature("Update avatar should delegate to update strategy") {
    Scenario("There is no update strategy") {
      noException shouldBe thrownBy {
        Given("An avatar and an mocked FrameTimeInfo")
        val avatar = Avatar(stub[Settings], stub[Live2DModel])
        val frameTimeInfo = stub[FrameTimeInfo]

        And("it has no update strategy")
        avatar.updateStrategyHolder = None

        When("update the avatar")
        avatar.update(frameTimeInfo)

        Then("there should be no exception thrown")
      }
    }

    Scenario("There is update strategy set") {
      Given("An avatar and an mocked FrameTimeInfo")
      val avatar = Avatar(stub[Settings], stub[Live2DModel])
      val frameTimeInfo = stub[FrameTimeInfo]

      And("it has a stubbed strategy")
      val strategy = stub[UpdateStrategy]
      avatar.updateStrategyHolder = Some(strategy)

      When("update the avatar")
      avatar.update(frameTimeInfo)

      Then("it should delegate to the update strategy")
      (strategy.update _).verify(frameTimeInfo).once()
    }

  }

}
