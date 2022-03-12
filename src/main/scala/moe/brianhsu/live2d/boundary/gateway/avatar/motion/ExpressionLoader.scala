package moe.brianhsu.live2d.boundary.gateway.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.motion.Motion

trait ExpressionLoader {
  def loadExpressions: Map[String, Motion]
}
