package moe.brianhsu.live2d.framework.model.settings

case class ExpressionParameter(id: String, value: Float, blend: Option[String])
case class Expression(`type`: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], parameters: List[ExpressionParameter])
