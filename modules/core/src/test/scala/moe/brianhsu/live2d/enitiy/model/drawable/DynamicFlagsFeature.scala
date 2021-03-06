package moe.brianhsu.live2d.enitiy.model.drawable

import com.sun.jna.{Memory, Native}
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI.DynamicDrawableFlagMask._
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class DynamicFlagsFeature extends AnyFeatureSpec
                          with GivenWhenThen with Matchers with TableDrivenPropertyChecks {


  Feature("Reading the flag value") {
    Scenario("Read the flags from the bitmap") {

      val dataTable = Table(
        ("bitmap", "isVisible", "visibilityChanged", "opacityChanged", "drawOrderChanged", "vertexPositionChanged", "blendColorChanged"),
        (0, false, false, false, false, false, false),
        (0 | csmIsVisible, true, false, false, false, false, false),
        (0 | csmVisibilityDidChange, false, true, false, false, false, false),
        (0 | csmOpacityDidChange, false, false, true, false, false, false),
        (0 | csmDrawOrderDidChange, false, false, false, true, false, false),
        (0 | csmVertexPositionsDidChange, false, false, false, false, true, false),
        (0 | csmIsVisible|csmVisibilityDidChange, true, true, false, false, false, false),
        (0 | csmVisibilityDidChange| csmDrawOrderDidChange, false, true, false, true, false, false),
        (0 | csmIsVisible | csmBlendColorChanged, true, false, false, false, false, true),
      )

      forAll(dataTable) { (bitmap, isVisible, visibilityChanged, opacityChanged, drawOrderChanged, vertexPositionChanged, blendColorChanged) =>
        When(s"create a ConstantDrawableFlags from that bitmap $bitmap")
        val pointer = new Memory(Native.getNativeSize(classOf[Byte])).share(0)
        pointer.setByte(0, bitmap.toByte)
        val flags = new DynamicFlags(pointer)

        Then("the flags should have correct value")
        flags.bitmask shouldBe bitmap.toByte
        flags.isVisible shouldBe isVisible
        flags.visibilityChanged shouldBe visibilityChanged
        flags.opacityChanged shouldBe opacityChanged
        flags.drawOrderChanged shouldBe drawOrderChanged
        flags.vertexPositionChanged shouldBe vertexPositionChanged
        flags.blendColorChanged shouldBe blendColorChanged
      }
    }

    Scenario("Read the updated flags from the bitmap") {

      val dataTable = Table(
        ("bitmap", "isVisible", "visibilityChanged", "opacityChanged", "drawOrderChanged", "vertexPositionChanged", "blendColorChanged"),
        (0, false, false, false, false, false, false),
        (0 | csmIsVisible, true, false, false, false, false, false),
        (0 | csmVisibilityDidChange, false, true, false, false, false, false),
        (0 | csmOpacityDidChange, false, false, true, false, false, false),
        (0 | csmDrawOrderDidChange, false, false, false, true, false, false),
        (0 | csmVertexPositionsDidChange, false, false, false, false, true, false),
        (0 | csmIsVisible|csmVisibilityDidChange, true, true, false, false, false, false),
        (0 | csmVisibilityDidChange| csmDrawOrderDidChange, false, true, false, true, false, false),
        (0 | csmIsVisible | csmBlendColorChanged, true, false, false, false, false, true),
      )

      forAll(dataTable) { (bitmap, isVisible, visibilityChanged, opacityChanged, drawOrderChanged, vertexPositionChanged, blendColorChanged) =>
        When(s"create a ConstantDrawableFlags from that bitmap $bitmap")
        val pointer = new Memory(Native.getNativeSize(classOf[Byte])).share(0)
        pointer.setByte(0, 0)
        val flags = new DynamicFlags(pointer)

        Then("the flags should all be false")
        flags.bitmask shouldBe 0
        flags.isVisible shouldBe false
        flags.visibilityChanged shouldBe false
        flags.opacityChanged shouldBe false
        flags.drawOrderChanged shouldBe false
        flags.vertexPositionChanged shouldBe false
        flags.blendColorChanged shouldBe false

        When("update the value that pointer points to")
        pointer.setByte(0, bitmap.toByte)

        Then("the flags should be updated")
        flags.bitmask shouldBe bitmap.toByte
        flags.isVisible shouldBe isVisible
        flags.visibilityChanged shouldBe visibilityChanged
        flags.opacityChanged shouldBe opacityChanged
        flags.drawOrderChanged shouldBe drawOrderChanged
        flags.vertexPositionChanged shouldBe vertexPositionChanged
        flags.blendColorChanged shouldBe blendColorChanged
      }
    }

  }
}
