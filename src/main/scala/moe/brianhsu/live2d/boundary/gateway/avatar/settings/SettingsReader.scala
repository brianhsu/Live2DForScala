package moe.brianhsu.live2d.boundary.gateway.avatar.settings

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import scala.util.Try

/**
 * Gateway for prepare a Live 2D Cubism Avatar setting.
 */
trait SettingsReader {
  /**
   * Load Live2D Cubism Avatar settings
   *
   * @return  A [[scala.util.Success]]`[Settings]` if load successful, otherwise a [[scala.util.Failure]].
   */
  def loadSettings(): Try[Settings]
}
