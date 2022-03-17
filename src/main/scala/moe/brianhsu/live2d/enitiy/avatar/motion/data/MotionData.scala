package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.{Model, Parameter, PartOpacity}

case class MotionData(curves: List[MotionCurve], events: List[MotionEvent], duration: Float = 0.0f, isLoop: Boolean = false, curveCount: Int = 0, fps: Float = 0.0f) {

  lazy val modelCurves = curves.filter(_.targetType == Model)
  lazy val parameterCurves = curves.filter(_.targetType == Parameter)
  lazy val partOpacityCurves = curves.filter(_.targetType == PartOpacity)

}
