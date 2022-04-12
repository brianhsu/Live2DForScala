package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo

abstract class LAppSprite(drawCanvasInfoReader: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                         (implicit private val gl: OpenGLBinding) {

  case class Position(originX: Float, originY: Float, width: Float, height: Float)

  import gl._

  private val positionLocation = gl.glGetAttribLocation(shader.shaderProgram, "position")
  private val uvLocation = gl.glGetAttribLocation(shader.shaderProgram, "uv")
  private val textureLocation = gl.glGetUniformLocation(shader.shaderProgram, "texture")
  private val colorLocation = gl.glGetUniformLocation(shader.shaderProgram, "baseColor")
  private val spriteColor = (1.0f, 1.0f, 1.0f, 1.0f)
  private var rect = createRectangle()

  protected def calculatePosition(): Position

  private def createRectangle() = {
    val position = calculatePosition()

    Rectangle(position.originX, position.originY, position.width, position.height)
  }

  def resize(): Unit = {
    this.rect = createRectangle()
  }

  def render(): Unit = {
    val maxWidth = drawCanvasInfoReader.currentCanvasWidth
    val maxHeight = drawCanvasInfoReader.currentCanvasHeight

    gl.glEnable(GL_TEXTURE_2D)

    val uvVertex = Array(
        1.0f, 0.0f,
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f
    )

    gl.glEnableVertexAttribArray(positionLocation)
    gl.glEnableVertexAttribArray(uvLocation)
    gl.glUniform1i(textureLocation, 0)

    val positionVertex = Array(
      (rect.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (rect.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.topY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (rect.leftX - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f),
      (rect.rightX - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.bottomY - maxHeight * 0.5f) / (maxHeight * 0.5f)
    )

    val buffer1 = gl.newDirectFloatBuffer(positionVertex)
    val buffer2 = gl.newDirectFloatBuffer(uvVertex)

    gl.glVertexAttribPointer(positionLocation, 2, GL_FLOAT, normalized = false, 0, buffer1)
    gl.glVertexAttribPointer(uvLocation, 2, GL_FLOAT, normalized = false, 0, buffer2)

    gl.glUniform4f(colorLocation, spriteColor._1, spriteColor._2, spriteColor._3, spriteColor._4)
    gl.glBindTexture(GL_TEXTURE_2D, textureInfo.textureId)
    gl.glDrawArrays(GL_TRIANGLE_FAN, 0, 4)
  }

  def isHit(pointX: Float, pointY: Float): Boolean = {
    val invertedY = drawCanvasInfoReader.currentCanvasHeight - pointY

    pointX >= rect.leftX &&
      pointX <= rect.rightX &&
      invertedY >= rect.bottomY &&
      invertedY <= rect.topY
  }
}
