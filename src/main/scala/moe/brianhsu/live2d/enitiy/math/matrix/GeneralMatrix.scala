package moe.brianhsu.live2d.enitiy.math.matrix

class GeneralMatrix(override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {
  type T = GeneralMatrix
  override protected def buildFrom(tr: Array[Float]): GeneralMatrix = new GeneralMatrix(tr)
}
