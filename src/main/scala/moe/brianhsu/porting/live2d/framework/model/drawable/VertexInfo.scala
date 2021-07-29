package moe.brianhsu.porting.live2d.framework.model.drawable

import moe.brianhsu.live2d.enitiy.core.types.{CArrayOfCsmVector, CArrayOfShort}

import java.nio.ByteBuffer

/**
 * This class represent information about vertices of a drawable when rendering.
 *
 * @param numberOfVertex                      How many vertices this drawable has.
 * @param numberOfTriangleIndex               How many triangle index this drawable has.
 * @param pointerToArrayOfPositions           The pointer to the actual memory address that stores the array of vertex position.
 * @param pointerToArrayOfTextureCoordinate   The pointer to the actual memory address that stores the array of vertex texture coordinate.
 * @param pointerToArrayOfIndex               The pointer to the actual memory address that stores the array of triangle index.
 */
case class VertexInfo(numberOfVertex: Int, numberOfTriangleIndex: Int,
                      pointerToArrayOfPositions: CArrayOfCsmVector,
                      pointerToArrayOfTextureCoordinate: CArrayOfCsmVector,
                      pointerToArrayOfIndex: CArrayOfShort) {


  /**
   * Get the position of the drawable vertices.
   *
   * @return  The List[(X, Y)] that denotes the position of vertices of this drawable.
   */
  def positions: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfPositions)

  /**
   * Get the texture coordinates of the drawable vertices.
   *
   * @return  The List[(X, Y)] that denotes the texture coordinates of vertices of this drawable.
   */

  def textureCoordinates: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfTextureCoordinate)

  /**
   * Get the triangle index of this drawable.
   *
   * @return  The list of triangle index.
   */
  def indices: List[Short] = (0 until numberOfTriangleIndex).toList.map(i => pointerToArrayOfIndex(i))

  /**
   * Direct buffer of vertex array
   *
   * @return  This will return a ByteBuffer that represent the array of vertex.
   */
  def vertexArrayDirectBuffer: ByteBuffer = pointerToArrayOfPositions.getDirectBuffer(numberOfVertex)

  /**
   * Direct buffer of uv (texture coordinate) array
   *
   * @return  This will return a ByteBuffer that represent the array of uv (texture coordinate).
   */
  def uvArrayDirectBuffer: ByteBuffer = pointerToArrayOfTextureCoordinate.getDirectBuffer(numberOfVertex)

  /**
   * Direct buffer of triangle index array
   *
   * @return  This will return a ByteBuffer that represent the array of triangle index.
   */
  def indexArrayDirectBuffer: ByteBuffer = pointerToArrayOfIndex.getDirectBuffer(numberOfTriangleIndex)

  private def createTupleListFrom(vectorArray: CArrayOfCsmVector): List[(Float, Float)] = {
    (0 until numberOfVertex).toList.map { i =>
      val csmVector = vectorArray(i)
      (csmVector.getX, csmVector.getY)
    }
  }
}

