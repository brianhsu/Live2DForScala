package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.core.CubismCoreCLibrary.ConstantDrawableFlagMask._
import moe.brianhsu.live2d.framework.ConstantDrawableFlags.{AdditiveBlend, MultiplicativeBlend, Normal}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ConstantDrawableFlagsFeature extends AnyFeatureSpec
                                   with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Calculate other flags") {
    Scenario("Both double sided & inverted mask is set") {
      Given("a bitmask that both bit is set")
      val flagsValue = 0 | csmIsDoubleSided | csmIsInvertedMask

      When(s"Create a ConstantDrawableFlags from that bitmap")
      val flag = ConstantDrawableFlags(flagsValue.toByte)

      Then("both isDoubledSided and isInvertedMask should be true")
      flag.isDoubleSided shouldBe true
      flag.isInvertedMask shouldBe true
    }

    Scenario("Only double sided is set") {
      Given("a bitmask that only csmIsDoubleSided bit is set")
      val flagsValue = 0 | csmIsDoubleSided

      When(s"Create a ConstantDrawableFlags from that bitmap")
      val flag = ConstantDrawableFlags(flagsValue.toByte)

      Then("only isDoubledSided should be true")
      flag.isDoubleSided shouldBe true
      flag.isInvertedMask shouldBe false
    }

    Scenario("Only inverted mask is set") {
      Given("a bitmask that only csmIsInvertedMask bit is set")
      val flagsValue = 0 | csmIsInvertedMask

      When(s"Create a ConstantDrawableFlags from that bitmap")
      val flag = ConstantDrawableFlags(flagsValue.toByte)

      Then("only isInvertedMask should be true")
      flag.isDoubleSided shouldBe false
      flag.isInvertedMask shouldBe true
    }

  }
  Feature("Calculate the blend mode") {
    Scenario("Construct from the bitmap only set blend mode bit") {

      val dataTable = Table(
        ("bitmap",                                              "expectedValue"),
        (0,                                                              Normal),
        (0 | csmBlendAdditiveBit,                                 AdditiveBlend),
        (0 | csmBlendMultiplicative,                        MultiplicativeBlend),
        (0 | csmBlendAdditiveBit | csmBlendMultiplicative,        AdditiveBlend)
      )

      forAll(dataTable) { (bitmap, expectedValue) =>
        When(s"Create a ConstantDrawableFlags from that bitmap $bitmap")
        val flag = new ConstantDrawableFlags(bitmap.toByte)

        Then("the blendMode should be Normal")
        flag.blendMode shouldBe expectedValue
      }
    }

    Scenario("Construct from the bitmap with other flag set") {

      val dataTable = Table(
        ("bitmap",                                                              "expectedValue"),
        (0 | csmIsInvertedMask,                                                              Normal),
        (0 | csmIsInvertedMask | csmBlendAdditiveBit,                                 AdditiveBlend),
        (0 | csmIsInvertedMask | csmBlendMultiplicative,                        MultiplicativeBlend),
        (0 | csmIsInvertedMask | csmBlendAdditiveBit | csmBlendMultiplicative,        AdditiveBlend)
      )

      forAll(dataTable) { (bitmap, expectedValue) =>
        When(s"Create a ConstantDrawableFlags from that bitmap $bitmap")
        val flag = new ConstantDrawableFlags(bitmap.toByte)

        Then("the blendMode should be Normal")
        flag.blendMode shouldBe expectedValue
      }
    }

  }
}
