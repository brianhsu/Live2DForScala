package moe.brianhsu.live2d.boundary.gateway.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression

/**
 * Expression Reader
 */
trait ExpressionReader {

  /**
   * Load expression
   *
   * @return A map contains expression name and corresponding Expression object.
   */
  def loadExpressions: Map[String, Expression]
}
