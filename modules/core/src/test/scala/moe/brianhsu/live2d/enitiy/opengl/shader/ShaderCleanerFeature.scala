package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.shader.ShaderCleaner.ShaderDeleter
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.GivenWhenThen

class ShaderCleanerFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {

  Feature("Delete shader") {
    Scenario("Delete shader") {
      Given("a stubbed shader programId")
      val programId = 123

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]

      And("a ShaderCleaner.ShaderDeleter")
      val shaderCleaner = new ShaderDeleter(programId, gl)

      When("cleanup a shader")
      shaderCleaner.run()

      Then("the mocked OpenGL binding should delete the program")
      (gl.glDeleteProgram _).verify(programId).once()
    }

  }

}
