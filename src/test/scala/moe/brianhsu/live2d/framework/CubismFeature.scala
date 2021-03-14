package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.core.types.{CsmVersion, MocVersion40}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class CubismFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  private val cubism = new Cubism

  Feature("Get version information") {
    Scenario("Get core library version") {
      When("get core library version")
      val version = cubism.coreLibraryVersion

      Then("it should get the version correctly")
      version shouldBe CsmVersion(4, 0, 0)
    }

    Scenario("Get supported .moc file version") {
      When("get latest supported .moc file version")
      val version = cubism.latestSupportedMocVersion

      Then("it should get the version correctly")
      version shouldBe MocVersion40
    }
  }
}
