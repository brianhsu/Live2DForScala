package moe.brianhsu.live2d.framework.model.drawable

import moe.brianhsu.live2d.core.types.{CArrayOfCsmVector, CArrayOfShort}

case class VertexInfo(numberOfVertex: Int, numberOfIndex: Int,
                      pointerToArrayOfPositions: CArrayOfCsmVector,
                      pointerToArrayOfTextureCoordinate: CArrayOfCsmVector,
                      pointerToArrayOfIndex: CArrayOfShort) {


  def positions: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfPositions)

  def textureCoordinates: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfTextureCoordinate)

  def indices: List[Short] = (0 until numberOfIndex).toList.map(i => pointerToArrayOfIndex(i))

  private def createTupleListFrom(vectorArray: CArrayOfCsmVector): List[(Float, Float)] = {
    (0 until numberOfVertex).toList.map { i =>
      val csmVector = vectorArray(i)
      (csmVector.getX, csmVector.getY)
    }
  }
}

