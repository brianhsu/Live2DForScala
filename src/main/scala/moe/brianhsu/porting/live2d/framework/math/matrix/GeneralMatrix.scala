package moe.brianhsu.porting.live2d.framework.math.matrix

class GeneralMatrix(protected val dataArray: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4[GeneralMatrix] {
  override protected def buildFrom(tr: Array[Float]): GeneralMatrix = new GeneralMatrix(tr)
}
