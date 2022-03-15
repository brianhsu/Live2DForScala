package moe.brianhsu.live2d.boundary.gateway.avatar.motion

import moe.brianhsu.porting.live2d.framework.CubismMotionData

trait MotionDataReader {
  def loadMotionData(): CubismMotionData
}
