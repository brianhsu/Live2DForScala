package moe.brianhsu.live2d.boundary.gateway.avatar.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Physics, Pose}

trait PhysicsReader {
  /**
   * Load and create Pose effect
   *
   * @return  `None` if there is no physics setting, or `Some[CubismPhysics]` contains
   *          the [[moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose]] effect.
   */
  def loadPhysics: Option[Physics]

}
