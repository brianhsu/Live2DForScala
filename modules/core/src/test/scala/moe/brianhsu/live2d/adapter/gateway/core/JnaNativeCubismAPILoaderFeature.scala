package moe.brianhsu.live2d.adapter.gateway.core

import moe.brianhsu.live2d.enitiy.core.types.{CsmLogFunction, CsmVersion, MocVersion42}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class JnaNativeCubismAPILoaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("Init with custom logger") {
    Scenario("create cubism with custom logger") {
      Given("a custom logger")
      val logger = new CsmLogFunction {
        override def invoke(message: String): Unit = {}
      }

      When("create a Cubism object with that logger")
      val cubismCore = new JnaNativeCubismAPILoader(logger)

      Then("the logger of cLibrary should be the custom logger")
      cubismCore.cubismAPI.csmGetLogFunction() shouldBe logger
    }
  }

  Feature("Get version related information") {
    Scenario("Get core library version") {
      Given("a JNA cubism core")
      val cubismCore = new JnaNativeCubismAPILoader()

      When("get core library version")
      val version = cubismCore.libraryVersion

      Then("it should get the version correctly")
      version shouldBe CsmVersion(4, 2, 4)
    }

    Scenario("Get supported .moc file version") {
      Given("a JNA cubism core")
      val cubismCore = new JnaNativeCubismAPILoader()

      When("get latest supported .moc file version")
      val version = cubismCore.latestSupportedMocVersion

      Then("it should get the version correctly")
      version shouldBe MocVersion42
    }
  }
}
