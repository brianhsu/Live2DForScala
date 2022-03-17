package moe.brianhsu.live2d.enitiy.model.drawable

import moe.brianhsu.live2d.enitiy.core.types.{CArrayOfCsmCoordinate, CArrayOfShort}

import java.nio.ByteBuffer

/**
 * This class represent information about vertices of a drawable when rendering.
 *
 * @param numberOfVertex                    How many vertices this drawable has.
 * @param numberOfTriangleIndex             How many triangle index this drawable has.
 * @param pointerToArrayOfPositions         The pointer to the actual memory address that stores the array of vertex position.
 * @param pointerToArrayOfTextureCoordinate The pointer to the actual memory address that stores the array of vertex texture coordinate.
 * @param pointerToArrayOfIndex             The pointer to the actual memory address that stores the array of triangle index.
 */
case class VertexInfo(numberOfVertex: Int, numberOfTriangleIndex: Int,
                      pointerToArrayOfPositions: CArrayOfCsmCoordinate,
                      pointerToArrayOfTextureCoordinate: CArrayOfCsmCoordinate,
                      pointerToArrayOfIndex: CArrayOfShort) {


  /**
   * Get the position of the drawable vertices.
   *
   * @return The List[(X, Y)] that denotes the position of vertices of this drawable.
   */
  def positions: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfPositions)

  /**
   * Get the texture coordinates of the drawable vertices.
   *
   * @return The List[(X, Y)] that denotes the texture coordinates of vertices of this drawable.
   */

  def textureCoordinates: List[(Float, Float)] = createTupleListFrom(pointerToArrayOfTextureCoordinate)

  /**
   * Get the triangle index of this drawable.
   *
   * @return The list of triangle index.
   */
  def indices: List[Short] = (0 until numberOfTriangleIndex).toList.map(i => pointerToArrayOfIndex(i))

  /**
   * Direct buffer of vertex array
   *
   * @return This will return a ByteBuffer that represent the array of vertex.
   */
  def vertexArrayDirectBuffer: ByteBuffer = pointerToArrayOfPositions.directBuffer(numberOfVertex)

  /**
   * Direct buffer of uv (texture coordinate) array
   *
   * @return This will return a ByteBuffer that represent the array of uv (texture coordinate).
   */
  def uvArrayDirectBuffer: ByteBuffer = pointerToArrayOfTextureCoordinate.directBuffer(numberOfVertex)

  /**
   * Direct buffer of triangle index array
   *
   * @return This will return a ByteBuffer that represent the array of triangle index.
   */
  def indexArrayDirectBuffer: ByteBuffer = pointerToArrayOfIndex.directBuffer(numberOfTriangleIndex)

  private def createTupleListFrom(coordinateArray: CArrayOfCsmCoordinate): List[(Float, Float)] = {
    (0 until numberOfVertex).toList.map { i =>
      val coordinate = coordinateArray(i)
      (coordinate.getX, coordinate.getY)
    }
  }
}
