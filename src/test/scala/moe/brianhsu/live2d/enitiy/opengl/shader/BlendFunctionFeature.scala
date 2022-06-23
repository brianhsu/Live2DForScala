package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags
import moe.brianhsu.live2d.enitiy.opengl.{BlendFunction, OpenGLBinding}
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class BlendFunctionFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                                with OpenGLMock with TableDrivenPropertyChecks {

  Feature("Create from ConstantFlags.BlendMode") {
    Scenario("Create factory from stubbed OpenGL") {
      val constant = createOpenGLStub().constants
      import constant._

      val testData = Table(
        ("blendMode", "expectedBlending"),
        (ConstantFlags.Normal, BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)),
        (ConstantFlags.AdditiveBlend, BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)),
        (ConstantFlags.MultiplicativeBlend, BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)),
      )

      forAll(testData) { case (blendMode, expectedBlending) =>
        Given("a implicit stubbed OpenGL binding")
        implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

        When(s"create a Blending object from a BlendMode = $blendMode")
        val blending = BlendFunction(blendMode)

        Then(s"the blending should be $expectedBlending")
        blending shouldBe expectedBlending

      }
    }
  }

}
