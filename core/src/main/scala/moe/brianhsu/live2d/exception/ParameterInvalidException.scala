package moe.brianhsu.live2d.exception

class ParameterInvalidException(id: String, assigned: Float, min: Float, max: Float) extends
  Exception(s"The assigned parameter value $assigned of $id is invalid, the range should be in [$min, $max]")
