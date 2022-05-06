package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.exception.{ShaderCompileException, ShaderLinkException}
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, OptionValues, TryValues}

class ShaderCompilerFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OptionValues with TryValues {
  private val GL_INFO_LOG_LENGTH = 1

  Feature("Link program") {
    Scenario("Link successfully") {
      Given("a mocked program ID")
      val programId = 123

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]

      And("a ShaderCompile based on that and does not have error when read link log")
      val compiler = new ShaderCompiler(gl) {
        override def readShaderLinkError(programId: Int): Option[String] = None
      }

      When("Link a program")
      val result = compiler.link(programId)

      Then("it should be a success and contain the programId")
      result.success.value shouldBe programId
    }

    Scenario("There is an error when link") {
      Given("a mocked program ID")
      val programId = 123

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]

      And("a ShaderCompile based on that and have error when read link log")
      val compiler = new ShaderCompiler(gl) {
        override def readShaderLinkError(programId: Int): Option[String] = Some("Link Error")
      }

      When("Link a program")
      val result = compiler.link(programId)

      Then("it should be a failure and contain the exception")
      result.failure.exception shouldBe a[ShaderLinkException]
      result.failure.exception.asInstanceOf[ShaderLinkException].programId shouldBe programId

      And("OpenGL binding should be called to link program")
      (gl.glLinkProgram _).verify(programId).once()
    }
  }

  Feature("Compile program") {
    Scenario("Compile successfully") {
      Given("a mocked shader ID")
      val mockedShaderId = 123
      val mockedShaderType = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glCreateShader _).when(mockedShaderType).returns(mockedShaderId)

      And("a ShaderCompile based on that and does not have error when read compile log")
      val compiler = new ShaderCompiler(gl) {
        override def readShaderCompileError(shaderId: Int): Option[String] = None
      }

      When("Compile a program")
      val result = compiler.compile(mockedShaderType, "ShaderSourceCode")

      Then("it should be a success and contain the shaderId")
      result.success.value shouldBe mockedShaderId

      And("OpenGL binding should be called to compile program")
      inSequence {
        (gl.glCreateShader _).verify(mockedShaderType).once()
        (gl.glShaderSource _).verify(where { (shaderId, count, sourceCode) =>
          shaderId shouldBe mockedShaderId
          count shouldBe 1
          sourceCode should contain theSameElementsInOrderAs List("ShaderSourceCode")
          true
        }).once()
        (gl.glCompileShader _).verify(mockedShaderId).once()
      }


    }

    Scenario("There is an error when compile") {
      Given("a mocked shader ID")
      val mockedShaderId = 123
      val mockedShaderType = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glCreateShader _).when(mockedShaderType).returns(mockedShaderId)

      And("a ShaderCompile based on that and does not have error when read compile log")
      val compiler = new ShaderCompiler(gl) {
        override def readShaderCompileError(shaderId: Int): Option[String] = Some("Shader Compile Error")
      }

      When("Compile a program")
      val result = compiler.compile(mockedShaderType, "ShaderSourceCode")

      Then("it should be a failure and contain the exception")
      result.failure.exception shouldBe a[ShaderCompileException]
      result.failure.exception.asInstanceOf[ShaderCompileException].shaderId shouldBe mockedShaderId

      And("OpenGL binding should be called to compile program")
      inSequence {
        (gl.glCreateShader _).verify(mockedShaderType).once()
        (gl.glShaderSource _).verify(where { (shaderId, count, sourceCode) =>
          shaderId shouldBe mockedShaderId
          count shouldBe 1
          sourceCode should contain theSameElementsInOrderAs List("ShaderSourceCode")
          true
        }).once()
        (gl.glCompileShader _).verify(mockedShaderId).once()
      }

    }
  }

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
