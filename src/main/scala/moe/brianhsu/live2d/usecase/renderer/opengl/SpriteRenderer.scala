package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite

class SpriteRenderer(spriteShader: SpriteShader)(implicit gl: OpenGLBinding) {


  import gl.constants._

  def drawSprite(sprite: Sprite): Unit = {

    val maxWidth = sprite.drawCanvasInfoReader.currentCanvasWidth
    val maxHeight = sprite.drawCanvasInfoReader.currentCanvasHeight

    gl.glUseProgram(spriteShader.programId)
    gl.glEnable(GL_TEXTURE_2D)

    gl.glEnableVertexAttribArray(spriteShader.positionLocation)
    gl.glEnableVertexAttribArray(spriteShader.uvLocation)
    gl.glUniform1i(spriteShader.textureLocation, 0)

    val positionVertex = Array(
      (sprite.positionAndSize.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.positionAndSize.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.positionAndSize.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.positionAndSize.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.positionAndSize.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.positionAndSize.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.positionAndSize.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.positionAndSize.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f)
    )

    val uvVertex = Array(
      1.0f, 0.0f,
      0.0f, 0.0f,
      0.0f, 1.0f,
      1.0f, 1.0f
    )

    val buffer1 = gl.newDirectFloatBuffer(positionVertex)
    val buffer2 = gl.newDirectFloatBuffer(uvVertex)

    gl.glVertexAttribPointer(spriteShader.positionLocation, 2, GL_FLOAT, normalized = false, 0, buffer1)
    gl.glVertexAttribPointer(spriteShader.uvLocation, 2, GL_FLOAT, normalized = false, 0, buffer2)

    gl.glUniform4f(spriteShader.baseColorLocation, sprite.spriteColor.red, sprite.spriteColor.green, sprite.spriteColor.blue, sprite.spriteColor.alpha)
    gl.glBindTexture(GL_TEXTURE_2D, sprite.textureInfo.textureId)
    gl.glDrawArrays(GL_TRIANGLE_FAN, 0, 4)
  }


}
