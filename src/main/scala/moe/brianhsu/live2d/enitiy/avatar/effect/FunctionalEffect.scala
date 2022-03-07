package moe.brianhsu.live2d.enitiy.avatar.effect

trait FunctionalEffect {
  def calculateOperations(currentTimeInSeconds: Float, deltaTimeInSeconds: Float): List[ParameterOperation]
}
