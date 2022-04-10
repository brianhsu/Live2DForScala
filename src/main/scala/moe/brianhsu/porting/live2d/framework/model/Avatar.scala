package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateStrategy

/**
 * This class represent a complete Live 2D Cubism Avatar runtime model.
 *
 */
class Avatar(val avatarSettings: Settings, val model: Live2DModel) {

  var updateStrategyHolder: Option[UpdateStrategy] = None

  /**
   * Update Live2D model parameters of this avatar according to time in seconds elapsed
   * from last update.
   *
   * The actually update implementation will be controlled by [[UpdateStrategy]] inside [[updateStrategyHolder]].
   *
   * @param frameTimeInfo The FrameTimeInfo object tells us how about frame time information.
   */
  def update(frameTimeInfo: FrameTimeInfo): Unit = {
    updateStrategyHolder.foreach(_.update(frameTimeInfo))
  }
}
