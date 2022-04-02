package moe.brianhsu.live2d.boundary.gateway.avatar.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose
import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysics

trait PhysicsReader {
  /**
   * Load and create Pose effect
   *
   * @return  `None` if there is no physics setting, or `Some[CubismPhysics]` contains
   *          the [[moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose]] effect.
   */
  def loadPhysics: Option[CubismPhysics]

}
