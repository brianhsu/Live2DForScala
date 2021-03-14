package moe.brianhsu.live2d.core

import com.sun.jna._
import com.sun.jna.ptr._
import moe.brianhsu.live2d.core.types.{CArrayOfArrayOfInt, CArrayOfArrayOfShort, CArrayOfByte, CArrayOfFloat, CArrayOfInt, CPointerToMoc, CPointerToModel, CStringArray, CsmLogFunction, CArrayOfArrayOfCsmVector}

object CubismCoreCLibrary {
  object ConstantDrawableFlagMask {
    val csmBlendAdditiveBit: Byte = (1 << 0).toByte
    val csmBlendMultiplicative: Byte = (1 << 1).toByte
    val csmIsDoubleSided: Byte = (1 << 2).toByte
    val csmIsInvertedMask: Byte = (1 << 3).toByte
  }
}

trait CubismCoreCLibrary extends Library {

  /* ------- *
   * VERSION *
   * ------- */
  /**
   * Queries Core version.
   *
   * @return  Core version.
   */
  def csmGetVersion(): Int

  /**
   * Gets Moc file supported latest version.
   *
   * @return csmMocVersion (Moc file latest format version).
   */
  def csmGetLatestMocVersion(): Int

  /**
   * Gets Moc file format version.
   *
   * @param  address  Address of moc.
   * @param  size     Size of moc (in bytes).
   *
   * @return csmMocVersion
   */
  def csmGetMocVersion(address: CPointerToMoc, size: Int): Int

  /* ------- *
   * LOGGING *
   * ------- */
  /**
   * Sets log handler.
   *
   * @param  handler  Handler to use.
   */
  def csmSetLogFunction(handler: CsmLogFunction): Unit

  /**
   * Queries log handler.
   *
   * @return  Log handler.
   */
  def csmGetLogFunction(): CsmLogFunction

  /* --- *
   * MOC *
   * --- */
  /**
   * Tries to revive a moc from bytes in place.
   *
   * @param  address  Address of unrevived moc. The address must be aligned to 'csmAlignofMoc'.
   * @param  size     Size of moc (in bytes).
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmReviveMocInPlace(address: Memory, size: Int): CPointerToMoc

  /* ----- *
   * MODEL *
   * ----- */

  /**
   * Queries size of a model in bytes.
   *
   * @param  moc  Moc to query.
   *
   * @return  Valid size on success; '0' otherwise.
   */
  def csmGetSizeofModel(moc: CPointerToMoc): Int

  /**
   * Tries to instantiate a model in place.
   *
   * @param  moc      Source moc.
   * @param  address  Address to place instance at. Address must be aligned to 'csmAlignofModel'.
   * @param  size     Size of memory block for instance (in bytes).
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmInitializeModelInPlace(moc: CPointerToMoc, address: Memory, size: Int): CPointerToModel

  /**
   * Updates a model.
   *
   * @param  model  Model to update.
   */
  def csmUpdateModel(model: CPointerToModel): Unit

  /* ------ *
   * CANVAS *
   * ------ */
  /**
   * Reads info on a model canvas.
   *
   * @param  model              Model query.
   *
   * @param  outSizeInPixels    Canvas dimensions.
   * @param  outOriginInPixels  Origin of model on canvas.
   * @param  outPixelsPerUnit   Aspect used for scaling pixels to units.
   */
  def csmReadCanvasInfo(model: CPointerToModel, outSizeInPixels: CsmVector, outOriginInPixels: CsmVector, outPixelsPerUnit: FloatByReference): CStringArray

  /* ---------- *
   * PARAMETERS *
   * ---------- */
  /**
   * Gets number of parameters.
   *
   * @param  model  Model to query.
   *
   * @return  Valid count on success; '-1' otherwise.
   */
  def csmGetParameterCount(model: CPointerToModel): Int

  /**
   * Gets parameter IDs.
   * All IDs are null-terminated ANSI strings.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetParameterIds(model: CPointerToModel): CStringArray

  /**
   * Gets minimum parameter values.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetParameterMinimumValues(model: CPointerToModel): CArrayOfFloat

  /**
   * Gets maximum parameter values.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetParameterMaximumValues(model: CPointerToModel): CArrayOfFloat

  /**
   * Gets default parameter values.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetParameterDefaultValues(model: CPointerToModel): CArrayOfFloat

  /**
   * Gets read/write parameter values buffer.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetParameterValues(model: CPointerToModel): CArrayOfFloat

  /* ----- *
   * PARTS *
   * ----- */
  /**
   * Gets number of parts.
   *
   * @param  model  Model to query.
   *
   * @return  Valid count on success; '-1' otherwise.
   */
  def csmGetPartCount(model: CPointerToModel): Int

  /**
   * Gets parts IDs.
   * All IDs are null-terminated ANSI strings.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetPartIds(model: CPointerToModel): CStringArray

  /**
   * Gets read/write part opacities buffer.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetPartOpacities(model: CPointerToModel): CArrayOfFloat

  /**
   * Gets part's parent part indices.
   *
   * @param   model   Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
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
  def csmGetDrawableVertexPositions(model: CPointerToModel): CArrayOfArrayOfCsmVector
  def csmGetDrawableVertexUvs(model: CPointerToModel): CArrayOfArrayOfCsmVector
  def csmGetDrawableIndexCounts(model: CPointerToModel): CArrayOfInt
  def csmGetDrawableIndices(model: CPointerToModel): CArrayOfArrayOfShort
  def csmResetDrawableDynamicFlags(model: CPointerToModel): Unit
}

