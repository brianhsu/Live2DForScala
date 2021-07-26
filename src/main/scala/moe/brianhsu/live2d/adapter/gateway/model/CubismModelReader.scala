package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.boundary.gateway.model.ModelReader
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.MocInfo

class CubismModelReader(mocInfo: MocInfo, textureFiles: List[String])(implicit core: CubismCore) extends ModelReader {
  override def loadModel: Live2DModel = new CubismLive2DModel(mocInfo, textureFiles)(core)
}
