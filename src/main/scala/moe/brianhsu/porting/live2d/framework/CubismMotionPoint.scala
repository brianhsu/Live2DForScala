package moe.brianhsu.porting.live2d.framework

case class CubismMotionPoint(var Time: Float, var Value: Float) {
  override def toString: String = s"CubismMotionPoint(${Time}f, ${Value}f)"
}
