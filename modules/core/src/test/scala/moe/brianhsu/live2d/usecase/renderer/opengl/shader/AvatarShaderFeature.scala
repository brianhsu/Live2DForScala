package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.AvatarShader
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class AvatarShaderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  
  Feature("Access avatar shader location") {
    Scenario("attributePositionLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedPositionLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetAttribLocation _).when(mockedProgramId, "a_position").returns(mockedPositionLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a positionLocation")
      val positionLocation = shader.attributePositionLocation

      Then("it should be same as the value returned by OpenGL binding")
      positionLocation shouldBe mockedPositionLocation
    }

    Scenario("attributeTexCoordLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedTexCoordLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetAttribLocation _).when(mockedProgramId, "a_texCoord").returns(mockedTexCoordLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a TexCoordLocation")
      val texCoordLocation = shader.attributeTexCoordLocation

      Then("it should be same as the value returned by OpenGL binding")
      texCoordLocation shouldBe mockedTexCoordLocation
    }

    Scenario("samplerTexture0Location") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedSamplerTexture0Location = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "s_texture0").returns(mockedSamplerTexture0Location)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a samplerTexture0Location")
      val samplerTexture0Location = shader.samplerTexture0Location

      Then("it should be same as the value returned by OpenGL binding")
      samplerTexture0Location shouldBe mockedSamplerTexture0Location
    }

    Scenario("samplerTexture1Location") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedSamplerTexture1Location = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "s_texture1").returns(mockedSamplerTexture1Location)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a samplerTexture0Location")
      val samplerTexture1Location = shader.samplerTexture1Location

      Then("it should be same as the value returned by OpenGL binding")
      samplerTexture1Location shouldBe mockedSamplerTexture1Location
    }

    Scenario("uniformClipMatrixLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedUniformClipMatrixLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_clipMatrix").returns(mockedUniformClipMatrixLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformClipMatrixLocation")
      val uniformClipMatrixLocation = shader.uniformClipMatrixLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformClipMatrixLocation shouldBe mockedUniformClipMatrixLocation
    }

    Scenario("uniformChannelFlagLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedUniformChannelFlagLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_channelFlag").returns(mockedUniformChannelFlagLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformChannelFlagLocation")
      val uniformChannelFlagLocation = shader.uniformChannelFlagLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformChannelFlagLocation shouldBe mockedUniformChannelFlagLocation
    }

    Scenario("uniformBaseColorLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedUniformBaseColorLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_baseColor").returns(mockedUniformBaseColorLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformBaseColorLocation")
      val uniformBaseColorLocation = shader.uniformBaseColorLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformBaseColorLocation shouldBe mockedUniformBaseColorLocation
    }

    Scenario("uniformMatrixLocation") {
      Given("a mocked programId and position location")
      val mockedProgramId = 123
      val mockedUniformMatrixLocation = 456

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_matrix").returns(mockedUniformMatrixLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformMatrixLocation")
      val uniformMatrixLocation = shader.uniformMatrixLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformMatrixLocation shouldBe mockedUniformMatrixLocation
    }

    Scenario("uniformMultiplyColorLocation") {
      Given("a mocked programId and color location")
      val mockedProgramId = 123
      val mockedUniformMultiplyColorLocation = 789

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_multiplyColor").returns(mockedUniformMultiplyColorLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformMatrixLocation")
      val uniformMultiplyColorLocation = shader.uniformMultiplyColorLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformMultiplyColorLocation shouldBe mockedUniformMultiplyColorLocation
    }

    Scenario("uniformScreenColorLocation") {
      Given("a mocked programId and color location")
      val mockedProgramId = 123
      val mockedUniformScreenColorLocation = 912

      And("a stubbed OpenGL binding")
      val gl = stub[OpenGLBinding]
      (gl.glGetUniformLocation _).when(mockedProgramId, "u_screenColor").returns(mockedUniformScreenColorLocation)

      And("a dummy Shader")
      val shader = createDummyAvatarShader(mockedProgramId, gl)

      When("access a uniformMatrixLocation")
      val uniformScreenColorLocation = shader.uniformScreenColorLocation

      Then("it should be same as the value returned by OpenGL binding")
      uniformScreenColorLocation shouldBe mockedUniformScreenColorLocation
    }

  }

  private def createDummyAvatarShader(mockedProgramId: Int, openGLBinding: OpenGLBinding) = {
    new AvatarShader()(openGLBinding) {
      override val programId: Int = mockedProgramId
      override def createShaderProgram() = mockedProgramId
      override protected def vertexShaderSource: String = ""
      override protected def fragmentShaderSource: String = ""
    }
  }

}
