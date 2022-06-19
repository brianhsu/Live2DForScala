package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.exception.{ShaderCompileException, ShaderLinkException}
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class ShaderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen
                    with MockFactory with OpenGLMock {

  Feature("Create Shader") {
    Scenario("Compile and Link successfully") {
      Given("A stubbed OpenGL / ShaderCompile and ShaderCleaner object")
      val mockedProgramId = 123
      val mockedVertexShaderId = 234
      val mockedFragmentShaderId = 456

      val compiler = stub[ShaderCompiler]
      val cleaner = stub[ShaderCleaner]

      And("dummy source code")
      val mockedVertexSource = "VertexSourceCode"
      val mockedFragmentSource = "FragmentSourceCode"

      And("stubbed OpenGL binding based on that")
      val gl = createOpenGLStub()
      import gl.constants._

      (() => gl.glCreateProgram()).when().returns(mockedProgramId)
      (compiler.compile _).when(GL_VERTEX_SHADER, mockedVertexSource).returns(Success(mockedVertexShaderId))
      (compiler.compile _).when(GL_FRAGMENT_SHADER, mockedFragmentSource).returns(Success(mockedFragmentShaderId))
      (compiler.link _).when(mockedProgramId).returns(Success(mockedProgramId))

      When("create a Shader based on those")
      val shader = new Shader(gl, compiler, cleaner) {
        override protected def vertexShaderSource: String = mockedVertexSource
        override protected def fragmentShaderSource: String = mockedFragmentSource
      }


      inSequence {
        Then("it should create a program using OpenGL binding")
        (() => gl.glCreateProgram()).verify().once().returns(mockedProgramId)

        And("Compile source code")
        (compiler.compile _).verify(GL_VERTEX_SHADER, mockedVertexSource).once()
        (compiler.compile _).verify(GL_FRAGMENT_SHADER, mockedFragmentSource).once()

        And("Attach shader")
        (gl.glAttachShader _).verify(mockedProgramId, mockedVertexShaderId).once()
        (gl.glAttachShader _).verify(mockedProgramId, mockedFragmentShaderId).once()

        And("Link program")
        (compiler.link _).verify(mockedProgramId).once()

        And("detach and delete vertex shader")
        (gl.glDetachShader _).verify(mockedProgramId, mockedVertexShaderId).once()
        (gl.glDeleteShader _).verify(mockedVertexShaderId).once()

        And("detach and delete fragment shader")
        (gl.glDetachShader _).verify(mockedProgramId, mockedFragmentShaderId).once()
        (gl.glDeleteShader _).verify(mockedFragmentShaderId).once()

      }

      And("The programId should be the mocked program id")
      shader.programId shouldBe mockedProgramId
    }

    Scenario("Failed to compile vertex shader") {
      Given("A stubbed OpenGL / ShaderCompile and ShaderCleaner object")
      val mockedProgramId = 123
      val mockedVertexShaderId = 234
      val mockedFragmentShaderId = 456

      val compiler = stub[ShaderCompiler]
      val cleaner = stub[ShaderCleaner]

      And("dummy source code and log message")
      val mockedVertexSource = "VertexSourceCode"
      val mockedFragmentSource = "FragmentSourceCode"

      And("stubbed OpenGL binding based on that")
      val gl = createOpenGLStub()
      val mockedException = new ShaderCompileException(mockedVertexShaderId, "Cannot compile vertex")

      import gl.constants._

      (() => gl.glCreateProgram()).when().returns(mockedProgramId)
      (compiler.compile _).when(GL_VERTEX_SHADER, mockedVertexSource).returns(Failure(mockedException))
      (compiler.compile _).when(GL_FRAGMENT_SHADER, mockedFragmentSource).returns(Success(mockedFragmentShaderId))

      When("create a Shader based on those")
      Then("it should throw an exception")
      val exception = the[ShaderCompileException] thrownBy {
         new Shader(gl, compiler, cleaner) {
          override protected def vertexShaderSource: String = mockedVertexSource
          override protected def fragmentShaderSource: String = mockedFragmentSource
        }
      }

      And("it should be the correct mockedException")
      exception shouldBe mockedException
    }

    Scenario("Failed to compile fragment shader") {
      Given("A stubbed OpenGL / ShaderCompile and ShaderCleaner object")
      val mockedProgramId = 123
      val mockedVertexShaderId = 234
      val mockedFragmentShaderId = 456

      val compiler = stub[ShaderCompiler]
      val cleaner = stub[ShaderCleaner]

      And("dummy source code and log message")
      val mockedVertexSource = "VertexSourceCode"
      val mockedFragmentSource = "FragmentSourceCode"

      And("stubbed OpenGL binding based on that")
      val gl = createOpenGLStub()
      val mockedException = new ShaderCompileException(mockedFragmentShaderId, "Cannot compile fragment")
      import gl.constants._

      (() => gl.glCreateProgram()).when().returns(mockedProgramId)
      (compiler.compile _).when(GL_VERTEX_SHADER, mockedVertexSource).returns(Success(mockedVertexShaderId))
      (compiler.compile _).when(GL_FRAGMENT_SHADER, mockedFragmentSource).returns(Failure(mockedException))

      When("create a Shader based on those")
      Then("it should throw an exception")
      val exception = the[ShaderCompileException] thrownBy {
        new Shader(gl, compiler, cleaner) {
          override protected def vertexShaderSource: String = mockedVertexSource
          override protected def fragmentShaderSource: String = mockedFragmentSource
        }
      }

      And("it should be the correct mockedException")
      exception shouldBe mockedException
    }

    Scenario("Failed to link") {
      Given("A stubbed OpenGL / ShaderCompile and ShaderCleaner object")
      val mockedProgramId = 123
      val mockedVertexShaderId = 234
      val mockedFragmentShaderId = 456

      val compiler = stub[ShaderCompiler]
      val cleaner = stub[ShaderCleaner]

      And("dummy source code")
      val mockedVertexSource = "VertexSourceCode"
      val mockedFragmentSource = "FragmentSourceCode"

      And("stubbed OpenGL binding based on that")
      val gl = createOpenGLStub()
      val mockedException = new ShaderLinkException(mockedProgramId, "Cannot link program")

      import gl.constants._

      (() => gl.glCreateProgram()).when().returns(mockedProgramId)
      (compiler.compile _).when(GL_VERTEX_SHADER, mockedVertexSource).returns(Success(mockedVertexShaderId))
      (compiler.compile _).when(GL_FRAGMENT_SHADER, mockedFragmentSource).returns(Success(mockedFragmentShaderId))
      (compiler.link _).when(mockedProgramId).returns(Failure(mockedException))

      When("create a Shader based on those")
      Then("it should throw an exception")
      val exception = the[ShaderLinkException] thrownBy {
        new Shader(gl, compiler, cleaner) {
          override protected def vertexShaderSource: String = mockedVertexSource
          override protected def fragmentShaderSource: String = mockedFragmentSource
        }
      }

      And("it should be the correct mockedException")
      exception shouldBe mockedException
    }
  }

  Feature("Use program") {
    Scenario("Use program") {
      Given("a mocked programId")
      val mockedProgramId = 123

      And("An stubbed OpenGL binding")
      val gl = createOpenGLStub()

      And("a dummy Shader")
      val shader = createDummyShader(mockedProgramId, gl)

      When("call useProgram on shader")
      shader.useProgram()

      Then("the OpenGL binding should get delegated call")
      (gl.glUseProgram _).verify(mockedProgramId).once()
    }
  }

  Feature("Access location") {
    Scenario("positionLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedPositionLocation = 456

      And("An stubbed OpenGL binding")
      val gl = createOpenGLStub()
      (gl.glGetAttribLocation _).when(mockedProgramId, "position").returns(mockedPositionLocation)

      And("a dummy Shader")
      val shader = createDummyShader(mockedProgramId, gl)

      When("access a positionLocation")
      val positionLocation = shader.positionLocation

      Then("it should be same as the value returned by OpenGL binding")
      positionLocation shouldBe mockedPositionLocation
    }

    Scenario("uvLocation") {
      Given("a mocked programId and uv location")
      val mockedProgramId = 123
      val mockedUvLocation = 234

      And("An stubbed OpenGL binding")
      val gl = createOpenGLStub()
      (gl.glGetAttribLocation _).when(mockedProgramId, "uv").returns(mockedUvLocation)

      And("a dummy Shader")
      val shader = createDummyShader(mockedProgramId, gl)

      When("access a uvLocation")
      val uvLocation = shader.uvLocation

      Then("it should be same as the value returned by OpenGL binding")
      uvLocation shouldBe mockedUvLocation
    }

    Scenario("textureLocation") {
      Given("a mocked programId and texture location")
      val mockedProgramId = 123
      val mockedTextureLocation = 789

      And("An stubbed OpenGL binding")
      val gl = createOpenGLStub()
      (gl.glGetUniformLocation _).when(mockedProgramId, "texture").returns(mockedTextureLocation)

      And("a dummy Shader")
      val shader = createDummyShader(mockedProgramId, gl)

      When("access a textureLocation")
      val textureLocation = shader.textureLocation

      Then("it should be same as the value returned by OpenGL binding")
      textureLocation shouldBe mockedTextureLocation
    }

    Scenario("baseColorLocation") {
      Given("a mocked programId and baseColor location")
      val mockedProgramId = 123
      val mockedBaseColorLocation = 890

      And("An stubbed OpenGL binding")
      val gl = createOpenGLStub()
      (gl.glGetUniformLocation _).when(mockedProgramId, "baseColor").returns(mockedBaseColorLocation)

      And("a dummy Shader")
      val shader = createDummyShader(mockedProgramId, gl)

      When("access a baseColorLocation")
      val baseColorLocation = shader.baseColorLocation

      Then("it should be same as the value returned by OpenGL binding")
      baseColorLocation shouldBe mockedBaseColorLocation
    }

  }

  private def createDummyShader(mockedProgramId: Int, openGLBinding: OpenGLBinding) = {
    new Shader(openGLBinding) {
      override val programId: Int = mockedProgramId
      override def createShaderProgram(): Int = mockedProgramId
      override protected def vertexShaderSource: String = ""
      override protected def fragmentShaderSource: String = ""
    }

  }

}
