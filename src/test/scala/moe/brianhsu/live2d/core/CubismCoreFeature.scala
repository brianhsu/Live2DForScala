package moe.brianhsu.live2d.core

import moe.brianhsu.live2d.core.types.CsmLogFunction
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class CubismCoreFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("Init with custom logger") {
    Scenario("create cubism with custom logger") {
      Given("A custom logger")
      val logger = new CsmLogFunction {
        override def invoke(message: String): Unit = {}
      }

      When("create a Cubism object with that logger")
      val cubismCore = new CubismCore(logger)

      Then("the logger of cLibrary should be the custom logger")
      cubismCore.cLibrary.csmGetLogFunction() shouldBe logger
    }
  }
}
