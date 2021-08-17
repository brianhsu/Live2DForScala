package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.boundary.gateway.model.{MocInfoReader, Live2DModelReader}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

class Live2DModelFileReader(mocInfoReader: MocInfoReader, textureFiles: List[String])
                           (implicit core: CubismCore) extends Live2DModelReader {

  override def loadModel(): Try[Live2DModel] = for {
    mocInfo <- mocInfoReader.loadMocInfo()
    backend <- new CubismModelBackend(mocInfo, textureFiles).validatedBackend
  } yield {
    new Live2DModel(backend)
  }
}
