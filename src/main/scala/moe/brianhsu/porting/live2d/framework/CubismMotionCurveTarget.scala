package moe.brianhsu.porting.live2d.framework

object CubismMotionCurveTarget {
  sealed trait TargetType
  object TargetType {
    def apply(code: String): TargetType = code match {
      case "Model" => CubismMotionCurveTarget_Model
      case "Parameter" => CubismMotionCurveTarget_Parameter
      case "PartOpacity" => CubismMotionCurveTarget_PartOpacity
      case _ => CubismMotionCurveTarget_Model
    }
  }
  case object CubismMotionCurveTarget_Model extends TargetType         ///< モデルに対して
  case object CubismMotionCurveTarget_Parameter extends TargetType      ///< パラメータに対して
  case object CubismMotionCurveTarget_PartOpacity extends TargetType    ///< パーツの不透明度に対して
}

