package moe.brianhsu.live2d.adapter.gateway.reader

import moe.brianhsu.live2d.adapter.gateway.avatar.CubismModelBackend
import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.boundary.gateway.reader.{Live2DModelReader, MocInfoReader}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

/**
 * Live2DModel loader loads model from MocInfoReader and texture file list.
 *
 * @param mocInfoReader  The loader for load MocInfo.
 * @param textureFiles   The texture file path list.
 * @param core           The CubismCore responsible for actually read data from model file.
 */
class Live2DModelFileReader(mocInfoReader: MocInfoReader, textureFiles: List[String])
                           (implicit core: CubismCore) extends Live2DModelReader {

  override def loadModel(): Try[Live2DModel] = for {
    mocInfo <- mocInfoReader.loadMocInfo()
    backend <- new CubismModelBackend(mocInfo, textureFiles).validatedBackend
  } yield {
    new Live2DModel(backend)
  }
}
