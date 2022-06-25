package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.sprite.Sprite

class SpriteRenderer(implicit gl: OpenGLBinding) {


  import gl.constants._

  def drawSprite(sprite: Sprite): Unit = {

    val maxWidth = sprite.drawCanvasInfoReader.currentCanvasWidth
    val maxHeight = sprite.drawCanvasInfoReader.currentCanvasHeight

    gl.glUseProgram(sprite.shader.programId)
    gl.glEnable(GL_TEXTURE_2D)

    val uvVertex = Array(
      1.0f, 0.0f,
      0.0f, 0.0f,
      0.0f, 1.0f,
      1.0f, 1.0f
    )

    gl.glEnableVertexAttribArray(sprite.shader.positionLocation)
    gl.glEnableVertexAttribArray(sprite.shader.uvLocation)
    gl.glUniform1i(sprite.shader.textureLocation, 0)

    val positionVertex = Array(
      (sprite.rect.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.rect.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.rect.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.rect.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.rect.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.rect.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (sprite.rect.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (sprite.rect.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f)
    )

    val buffer1 = gl.newDirectFloatBuffer(positionVertex)
    val buffer2 = gl.newDirectFloatBuffer(uvVertex)

    gl.glVertexAttribPointer(sprite.shader.positionLocation, 2, GL_FLOAT, normalized = false, 0, buffer1)
    gl.glVertexAttribPointer(sprite.shader.uvLocation, 2, GL_FLOAT, normalized = false, 0, buffer2)

    gl.glUniform4f(sprite.shader.baseColorLocation, sprite.spriteColor.red, sprite.spriteColor.green, sprite.spriteColor.blue, sprite.spriteColor.alpha)
    gl.glBindTexture(GL_TEXTURE_2D, sprite.textureInfo.textureId)
    gl.glDrawArrays(GL_TRIANGLE_FAN, 0, 4)
  }


}
