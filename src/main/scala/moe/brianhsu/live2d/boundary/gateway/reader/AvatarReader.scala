package moe.brianhsu.live2d.boundary.gateway.reader

import moe.brianhsu.porting.live2d.framework.model.Avatar

import scala.util.Try

/**
 * Interface for loading Avatar
 */
trait AvatarReader {
  /**
   * Load MocInfo
   *
   * @return Success[Avatar] if load successfully, otherwise a Failure[Throwable] to indicate error.
   */
  def loadAvatar(): Try[Avatar]
}
