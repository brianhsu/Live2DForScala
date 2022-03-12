package moe.brianhsu.live2d.boundary.gateway.avatar.effect

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose

trait PoseReader {
  def loadPose: Option[Pose]
}
