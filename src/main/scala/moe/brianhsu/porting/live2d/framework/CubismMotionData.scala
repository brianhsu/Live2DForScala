package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{MotionPoint, MotionSegment}

case class CubismMotionData(curvesList: List[CubismMotionCurve],
                            segmentsList: List[MotionSegment],
                            pointsList: List[MotionPoint],
                            eventsList: List[MotionEvent],
                            duration: Float = 0.0f,
                            isLoop: Boolean = false,
                            curveCount: Int = 0,
                            fps: Float = 0.0f) {

  lazy val curves = curvesList.toArray
  lazy val segments = segmentsList.toArray
  lazy val points = pointsList.toArray
  lazy val events = eventsList.toArray
}
