package moe.brianhsu.live2d.boundary.gateway.model

import moe.brianhsu.live2d.enitiy.model.MocInfo

import scala.util.Try

trait MocInfoReader {
  def loadMocInfo: Try[MocInfo]
}
