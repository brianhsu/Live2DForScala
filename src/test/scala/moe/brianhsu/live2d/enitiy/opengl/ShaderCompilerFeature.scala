package moe.brianhsu.live2d.enitiy.opengl

import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ShaderCompilerFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OptionValues {
  private val GL_INFO_LOG_LENGTH = 1

  Feature("Read shader program compile error log") {
    Scenario("Cannot get log length") {
      Given("a mocked shader ID")
      val shaderId = 123

      And("an OpenGL binding that has does not update log length")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)
      (gl.glGetShaderiv _).when(shaderId, GL_INFO_LOG_LENGTH, *)

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderCompileError(shaderId)

      Then("it should be None")
      errorLogHolder shouldBe None
    }

    Scenario("There is no error") {
      Given("a mocked shader ID")
      val shaderId = 123

      And("an OpenGL binding that has update log length to 0")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)

      (gl.glGetShaderiv _)
        .when(shaderId, GL_INFO_LOG_LENGTH, *)
        .onCall((_, _, values) => values(0) = 0)

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderCompileError(shaderId)

      Then("it should be None")
      errorLogHolder shouldBe None
    }

    Scenario("There is an error") {
      Given("a mocked shader ID and mocked error message")
      val shaderId = 123
      val errorMessage = "Compile Error Message"

      And("an OpenGL binding that has update log length to 0")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)

      (gl.glGetShaderiv _)
        .when(shaderId, GL_INFO_LOG_LENGTH, *)
        .onCall((_, _, values) => values(0) = errorMessage.length)

      (gl.glGetShaderInfoLog _)
        .when(shaderId, *, *)
        .onCall((_, _, logBuffer) => logBuffer.put(errorMessage.getBytes))

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderCompileError(shaderId)

      Then("it should be None")
      errorLogHolder.value shouldBe errorMessage
    }

  }

  Feature("Read shader program link error log") {
    Scenario("Cannot get log length") {
      Given("a mocked program ID")
      val programId = 456

      And("an OpenGL binding that has does not update log length")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)
      (gl.glGetProgramiv _).when(programId, GL_INFO_LOG_LENGTH, *)

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderLinkError(programId)

      Then("it should be None")
      errorLogHolder shouldBe None
    }

    Scenario("There is no error") {
      Given("a mocked program ID")
      val programId = 456

      And("an OpenGL binding that has update log length to 0")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)

      (gl.glGetProgramiv _)
        .when(programId, GL_INFO_LOG_LENGTH, *)
        .onCall((_, _, values) => values(0) = 0)

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderLinkError(programId)

      Then("it should be None")
      errorLogHolder shouldBe None
    }

    Scenario("There is an error") {
      Given("a mocked program ID")
      val programId = 456
      val errorMessage = "Link Error Message"

      And("an OpenGL binding that has update log length to 0")
      val gl = stub[OpenGLBinding]

      (() => gl.GL_INFO_LOG_LENGTH).when().returns(GL_INFO_LOG_LENGTH)

      (gl.glGetProgramiv _)
        .when(programId, GL_INFO_LOG_LENGTH, *)
        .onCall((_, _, values) => values(0) = errorMessage.length)

      (gl.glGetProgramInfoLog _)
        .when(programId, *, *)
        .onCall((_, _, logBuffer) => logBuffer.put(errorMessage.getBytes))

      And("a compiler based on that biding")
      val compiler = new ShaderCompiler(gl)

      When("read compile error log from OpenGL binding")
      val errorLogHolder = compiler.readShaderLinkError(programId)

      Then("it should be None")
      errorLogHolder.value shouldBe errorMessage
    }

  }

}
