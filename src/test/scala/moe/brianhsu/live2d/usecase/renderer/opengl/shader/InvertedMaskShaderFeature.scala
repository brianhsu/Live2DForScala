package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.InvertedMaskedShader
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class InvertedMaskShaderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                                with OpenGLMock {

  Feature("Source code of Shader") {
    Scenario("Has main method in vertex shader") {
      Given("A implicit stubbed OpenGL binding")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      When("Create a InvertedMaskShader")
      val shader = new InvertedMaskedShader()

      Then("The vertex source code has main method")
      shader.vertexShaderSource should include ("void main() {")
    }

    Scenario("Has main method in fragment shader") {
      Given("A implicit stubbed OpenGL binding")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      When("Create a InvertedMaskShader")
      val shader = new InvertedMaskedShader()

      Then("The fragment source code has main method")
      shader.fragmentShaderSource should include ("void main() {")
    }

  }

}
