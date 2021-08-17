package moe.brianhsu.live2d.boundary.gateway.reader

import moe.brianhsu.live2d.enitiy.model.MocInfo

import scala.util.Try

/**
 * Interface for loading MocInfo
 */
trait MocInfoReader {
  /**
   * Load MocInfo
   *
   * @return Success[MocInfo] if load successfully, otherwise a Failure[Throwable] to indicate error.
   */
  def loadMocInfo(): Try[MocInfo]
}
