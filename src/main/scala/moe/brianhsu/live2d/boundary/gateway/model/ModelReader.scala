package moe.brianhsu.live2d.boundary.gateway.model

import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

trait ModelReader {
  def loadModel(): Try[Live2DModel]
}
