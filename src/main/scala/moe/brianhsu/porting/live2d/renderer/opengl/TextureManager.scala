package moe.brianhsu.porting.live2d.renderer.opengl

import TextureManager.TextureInfo
import moe.brianhsu.porting.live2d.adapter.OpenGL

import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO

object TextureManager {
  case class TextureInfo(textureId: Int, width: Int, height: Int)
}

class TextureManager(implicit gl: OpenGL) {

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
    gl.glGenTextures(1, textureIds, 0)
    gl.glBindTexture(GL_TEXTURE_2D, textureIds(0))
    gl.glTexImage2D(
      GL_TEXTURE_2D, 0, GL_RGBA,
      bitmapInfo.width, bitmapInfo.height, 0,
      GL_RGBA, GL_UNSIGNED_BYTE, bitmapInfo.bitmap
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
    ImageBitmap(image.getWidth, image.getHeight, ByteBuffer.wrap(bitmap))
  }

}
