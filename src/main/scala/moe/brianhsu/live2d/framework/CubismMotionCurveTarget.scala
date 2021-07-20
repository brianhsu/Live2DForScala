package moe.brianhsu.live2d.framework

object CubismMotionCurveTarget {
  sealed trait TargetType
  case object CubismMotionCurveTarget_Model extends TargetType         ///< モデルに対して
  case object CubismMotionCurveTarget_Parameter extends TargetType      ///< パラメータに対して
  case object CubismMotionCurveTarget_PartOpacity extends TargetType    ///< パーツの不透明度に対して
}

