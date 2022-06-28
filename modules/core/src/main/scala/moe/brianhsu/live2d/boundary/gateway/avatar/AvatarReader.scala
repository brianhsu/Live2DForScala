package moe.brianhsu.live2d.boundary.gateway.avatar

import moe.brianhsu.live2d.enitiy.avatar.Avatar

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
