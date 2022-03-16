package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.{Model, Parameter, PartOpacity}

case class MotionData(curvesList: List[MotionCurve],
                      segmentsList: List[MotionSegment],
                      pointsList: List[MotionPoint],
                      eventsList: List[MotionEvent],
                      duration: Float = 0.0f,
                      isLoop: Boolean = false,
                      curveCount: Int = 0,
                      fps: Float = 0.0f) {

  lazy val segments = segmentsList.toArray
  lazy val points = pointsList.toArray
  lazy val events = eventsList.toArray
  lazy val modelCurves = curvesList.filter(_.targetType == Model)
  lazy val parameterCurves = curvesList.filter(_.targetType == Parameter)
  lazy val partOpacityCurves = curvesList.filter(_.targetType == PartOpacity)

}
