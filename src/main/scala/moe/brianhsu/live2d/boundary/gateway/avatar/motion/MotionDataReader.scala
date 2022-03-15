package moe.brianhsu.live2d.boundary.gateway.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.motion.data.MotionData

trait MotionDataReader {
  def loadMotionData(): MotionData
}
