package moe.brianhsu.live2d.boundary.gateway.model

import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait ModelReader {
  def loadModel: Live2DModel
}
