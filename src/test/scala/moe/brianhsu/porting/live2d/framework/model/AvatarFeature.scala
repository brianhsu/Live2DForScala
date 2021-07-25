package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.porting.live2d.framework.Cubism
import moe.brianhsu.porting.live2d.framework.exception.MocNotRevivedException
import org.scalatest.{GivenWhenThen, TryValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Failure

class AvatarFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  val cubism = new Cubism

  Feature("Validate Live2DModel when accessing model") {
    Scenario("Loading an avatar from an exist directory that has corrupted .moc3 file") {
      Given("a directory that exist but not a runtime")
      val directory = "src/test/resources/models/corruptedModel/corruptedMoc3"

      When("loading an avatar from it")
      val avatarHolder = cubism.loadAvatar(directory)

      Then("it should return a Success[Avatar]")
      val avatar = avatarHolder.success.value

      And("the model should be a Failure[MocNotRevivedException]")
      avatar.modelHolder shouldBe a[Failure[_]]
      avatar.modelHolder.failure.exception shouldBe a[MocNotRevivedException]
    }

  }
}
