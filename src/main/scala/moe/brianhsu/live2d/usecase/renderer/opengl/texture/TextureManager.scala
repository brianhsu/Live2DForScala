package moe.brianhsu.live2d.usecase.renderer.opengl.texture

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

import java.awt.image.BufferedImage
import java.io.File
import java.nio.{ByteBuffer, ByteOrder}
import javax.imageio.ImageIO

object TextureManager {

  private var bindingToManager: Map[OpenGLBinding, TextureManager] = Map.empty

  def getInstance(implicit openGLBinding: OpenGLBinding): TextureManager = {
    bindingToManager.get(openGLBinding) match {
      case Some(manager) => manager
      case None =>
        this.bindingToManager += (openGLBinding -> new TextureManager)
        this.bindingToManager(openGLBinding)
    }
  }

}

class TextureManager private[texture] (implicit gl: OpenGLBinding) {

  import gl.constants._

  case class ImageBitmap(width: Int, height: Int, bitmap: ByteBuffer)

  private var filenameToTexture: Map[String, TextureInfo] = Map.empty

  def loadTexture(filename: String): TextureInfo = {
    val textureInfo = filenameToTexture.get(filename) match {
      case None       => createOpenGLTexture(readBitmapFromFile(filename))
      case Some(info) => info
    }

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

    TextureInfo(textureIds(0), bitmapInfo.width, bitmapInfo.height)
  }

  private def readBitmapFromFile(filename: String): ImageBitmap = {
    val image = ImageIO.read(new File(filename))
    val bitmap = image.getType match {
      case BufferedImage.TYPE_4BYTE_ABGR => loadABGRBuffer(image)
      case _ => convertToABGRBuffer(image)
    }

    val buffer = ByteBuffer.allocateDirect(bitmap.length)
      .order(ByteOrder.nativeOrder())
      .put(bitmap)
      .rewind()

    ImageBitmap(image.getWidth, image.getHeight, buffer)
  }

  private def loadABGRBuffer(image: BufferedImage): Array[Byte] = {
    image.getRaster
      .getPixels(0, 0, image.getWidth, image.getHeight, null: Array[Int])
      .map(_.toByte)
  }

  private def convertToABGRBuffer(image: BufferedImage): Array[Byte] = {
    val newImage = new BufferedImage(image.getWidth, image.getHeight, BufferedImage.TYPE_4BYTE_ABGR)
    newImage.createGraphics().drawImage(image, 0, 0, image.getWidth, image.getHeight, null)
    loadABGRBuffer(newImage)
  }

}
