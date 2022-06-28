package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SetupMaskShader
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class SetupMaskShaderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock {

  Feature("Source code of Shader") {
    Scenario("Has main method in vertex shader") {
      Given("an implicit stubbed OpenGL binding")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      When("create a SetupMaskShader")
      val shader = new SetupMaskShader()

      Then("the vertex source code has main method")
      shader.vertexShaderSource should include ("void main() {")
    }

    Scenario("Has main method in fragment shader") {
      Given("an implicit stubbed OpenGL binding")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      When("create a SetupMaskShader")
      val shader = new SetupMaskShader()

      Then("the fragment source code has main method")
      shader.fragmentShaderSource should include ("void main() {")
    }

  }

}
