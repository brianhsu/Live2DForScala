package moe.brianhsu.live2d.enitiy.avatar.motion.data

trait CurveTarget

object CurveTarget {

  def apply(code: String): CurveTarget = code match {
    case "Model" => Model
    case "Parameter" => Parameter
    case "PartOpacity" => PartOpacity
    case _ => Model
  }

  case object Model extends CurveTarget
  case object Parameter extends CurveTarget
  case object PartOpacity extends CurveTarget

}