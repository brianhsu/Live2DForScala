package moe.brianhsu.porting.live2d.renderer.opengl

import TextureManager.TextureInfo
import moe.brianhsu.live2d.adapter.gateway.renderer.OpenGLBinding

import java.io.File
import java.nio.{ByteBuffer, ByteOrder}
import javax.imageio.ImageIO

object TextureManager {
  case class TextureInfo(textureId: Int, width: Int, height: Int)

  private var manager: Map[OpenGLBinding, TextureManager] = Map.empty

  def getInstance(implicit gl: OpenGLBinding): TextureManager = {
    manager.get(gl) match {
      case Some(manager) => manager
      case None =>
        this.manager += (gl -> new TextureManager())
        this.manager(gl)
    }
  }

}

class TextureManager private (implicit gl: OpenGLBinding) {

  import gl._

  case class ImageBitmap(width: Int, height: Int, bitmap: ByteBuffer)

  private var filenameToTexture: Map[String, TextureInfo] = Map.empty

  def loadTexture(filename: String): TextureInfo = {
    val textureInfo = filenameToTexture.getOrElse(
      filename,
      createOpenGLTexture(readBitmapFromFile(filename))
    )

    filenameToTexture += (filename -> textureInfo)

    textureInfo
  }

  private def createOpenGLTexture(bitmapInfo: ImageBitmap): TextureInfo = {

    val textureIds = new Array[Int](1)
    gl.glGenTextures(1, textureIds)
    gl.glBindTexture(GL_TEXTURE_2D, textureIds(0))
    gl.glTexImage2D(
      GL_TEXTURE_2D, 0, GL_RGBA,
      bitmapInfo.width, bitmapInfo.height, 0,
      GL_RGBA, GL_UNSIGNED_BYTE, bitmapInfo.bitmap.rewind()
    )

    gl.glGenerateMipmap(GL_TEXTURE_2D)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    gl.glBindTexture(GL_TEXTURE_2D, 0)

    TextureInfo(textureIds(0), bitmapInfo.width, bitmapInfo.height)
  }

  private def readBitmapFromFile(filename: String): ImageBitmap = {
    val image = ImageIO.read(new File(filename))
    val bitmap = image.getRaster.getPixels(0, 0, image.getWidth, image.getHeight, null: Array[Int]).map(_.toByte)
    val buffer = ByteBuffer.allocateDirect(bitmap.length)
      .order(ByteOrder.nativeOrder())
      .put(bitmap)
      .rewind()

    ImageBitmap(image.getWidth, image.getHeight, buffer)
  }

}
