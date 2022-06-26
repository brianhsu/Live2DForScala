package moe.brianhsu.live2d.boundary.gateway.avatar.effect

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose

/**
 * Reader to create Pose effect
 */
trait PoseReader {
  /**
   * Load and create Pose effect
   *
   * @return  `None` if there is no pose setting, or `Some[Pose]` contains
   *          the [[moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose]] effect.
   */
  def loadPose: Option[Pose]
}
