package moe.brianhsu.porting.live2d.framework

case class CubismMotionPoint(time: Float, value: Float) {
  override def toString: String = s"CubismMotionPoint(${time}f, ${value}f)"
}
