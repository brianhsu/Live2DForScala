package moe.brianhsu.porting.live2d.framework

object CubismMotionCurveTarget {
  sealed trait TargetType
  object TargetType {
    def apply(code: String): TargetType = code match {
      case "Model" => Model
      case "Parameter" => Parameter
      case "PartOpacity" => PartOpacity
      case _ => Model
    }
  }
  case object Model extends TargetType         ///< モデルに対して
  case object Parameter extends TargetType      ///< パラメータに対して
  case object PartOpacity extends TargetType    ///< パーツの不透明度に対して
}

