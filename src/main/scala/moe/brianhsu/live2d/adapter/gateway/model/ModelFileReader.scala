package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.boundary.gateway.model.{MocInfoReader, ModelReader}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

class ModelFileReader(mocInfoReader: MocInfoReader, textureFiles: List[String])
                     (implicit core: CubismCore) extends ModelReader {

  override def loadModel(): Try[Live2DModel] = for {
    mocInfo <- mocInfoReader.loadMocInfo()
    backend <- Try(new CubismModelBackend(mocInfo, textureFiles))
  } yield {
    new Live2DModel(backend)
  }
}
