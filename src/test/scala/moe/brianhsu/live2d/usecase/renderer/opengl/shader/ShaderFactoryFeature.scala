package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderFactory.DefaultShaderFactory
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ShaderFactoryFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                                with OpenGLMock {

  Feature("Create default shader factory") {
    Scenario("Create factory from stubbed OpenGL") {
      Given("a implicit stubbed OpenGL binding")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      When("create a DefaultShaderFactory")
      val factory = new DefaultShaderFactory

      Then("it should have correct shaders")
      factory.normalShader shouldBe a[NormalShader]
      factory.invertedMaskedShader shouldBe a[InvertedMaskedShader]
      factory.setupMaskShader shouldBe a[SetupMaskShader]
      factory.maskedShader shouldBe a[MaskedShader]
    }
  }

}
