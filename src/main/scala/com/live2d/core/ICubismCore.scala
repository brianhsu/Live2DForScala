package com.live2d.core

import com.live2d.core.types._
import com.sun.jna._
import com.sun.jna.ptr._

trait ICubismCore extends Library {
  def csmGetVersion(): Int
  def csmGetLatestMocVersion(): Int
  def csmGetMocVersion(moc: CPointerToMoc, size: Int): Int
  def csmSetLogFunction(handler: CsmLogFunction): Unit
  def csmGetLogFunction(): CsmLogFunction
  def csmReviveMocInPlace(address: Memory, size: Int): CPointerToMoc
  def csmGetSizeofModel(moc: CPointerToMoc): Int
  def csmInitializeModelInPlace(moc: CPointerToMoc, address: Memory, size: Int): CPointerToModel
  def csmUpdateModel(model: CPointerToModel): Unit
  def csmGetParameterCount(model: CPointerToModel): Int
  def csmGetParameterValues(model: CPointerToModel): CArrayOfFloat
  def csmGetParameterDefaultValues(model: CPointerToModel): CArrayOfFloat
  def csmGetParameterMaximumValues(model: CPointerToModel): CArrayOfFloat
  def csmGetParameterMinimumValues(model: CPointerToModel): CArrayOfFloat
  def csmGetParameterIds(model: CPointerToModel): CStringArray
  def csmReadCanvasInfo(model: CPointerToModel, outSizeInPixels: CsmVector, outOriginInPixels: CsmVector, outPixelsPerUnit: FloatByReference): CStringArray
  def csmGetPartCount(model: CPointerToModel): Int
  def csmGetPartIds(model: CPointerToModel): CStringArray
  def csmGetPartOpacities(model: CPointerToModel): CArrayOfFloat
  def csmGetPartParentPartIndices(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableCount(model: CPointerToModel): Int
  def csmGetDrawableIds(model: CPointerToModel): CStringArray
  def csmGetDrawableConstantFlags(model: CPointerToModel): CArrayOfByte
  def csmGetDrawableDynamicFlags(model: CPointerToModel): CArrayOfByte
  def csmGetDrawableTextureIndices(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableDrawOrders(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableRenderOrders(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableOpacities(model: CPointerToModel): CArrayOfFloat
  def csmGetDrawableMaskCounts(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableMasks(model: CPointerToModel): CArrayOfArrayOfInt
  def csmGetDrawableVertexCounts(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableVertexPositions(model: CPointerToModel): PointerToArrayOfArrayOfCsmVector
  def csmGetDrawableVertexUvs(model: CPointerToModel): PointerToArrayOfArrayOfCsmVector
  def csmGetDrawableIndexCounts(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableIndices(model: CPointerToModel): CArrayOfArrayOfShort
  def csmResetDrawableDynamicFlags(model: CPointerToModel): Unit
}

