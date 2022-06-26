package moe.brianhsu.live2d.enitiy.math.matrix

class GeneralMatrix(override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {
  type T = GeneralMatrix
  override protected def buildFrom(elements: Array[Float]): GeneralMatrix = new GeneralMatrix(elements)

  override def equals(obj: Any): Boolean = {
    obj != null &&
    obj.isInstanceOf[GeneralMatrix] &&
      (obj.asInstanceOf[GeneralMatrix].elements sameElements this.elements)
  }
}
