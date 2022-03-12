package moe.brianhsu.live2d.boundary.gateway.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression

trait ExpressionReader {
  def loadExpressions: Map[String, Expression]
}
