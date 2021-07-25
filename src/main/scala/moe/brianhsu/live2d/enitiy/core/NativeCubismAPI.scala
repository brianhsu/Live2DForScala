package moe.brianhsu.live2d.enitiy.core

import com.sun.jna._
import com.sun.jna.ptr._
import moe.brianhsu.live2d.enitiy.core.types.{CArrayOfArrayOfCsmVector, CArrayOfArrayOfInt, CArrayOfArrayOfShort, CArrayOfByte, CArrayOfFloat, CArrayOfInt, CPointerToMoc, CPointerToModel, CStringArray, CsmLogFunction}

object NativeCubismAPI {
  object DynamicDrawableFlagMask {
    val csmIsVisible: Byte = (1 << 0).toByte
    val csmVisibilityDidChange: Byte = (1 << 1).toByte
    val csmOpacityDidChange: Byte = (1 << 2).toByte
    val csmDrawOrderDidChange: Byte = (1 << 3).toByte
    val csmRenderOrderDidChange: Byte = (1 << 4).toByte
    val csmVertexPositionsDidChange: Byte = (1 << 5).toByte
  }

  object ConstantDrawableFlagMask {
    val csmBlendAdditiveBit: Byte = (1 << 0).toByte
    val csmBlendMultiplicative: Byte = (1 << 1).toByte
    val csmIsDoubleSided: Byte = (1 << 2).toByte
    val csmIsInvertedMask: Byte = (1 << 3).toByte
  }
}

/**
 * Native Cubism C API interface
 *
 * This is intended for use with JNA, library user should not use this
 * trait directly, unless you want interact with Cubism Live 2D underlying
 * native C library.
 */
trait NativeCubismAPI extends Library {

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

  /**
   * Gets number of drawables.
   *
   * @param  model  Model to query.
   *
   * @return  Valid count on success; '-1' otherwise.
   */
  def csmGetDrawableCount(model: CPointerToModel): Int

  /**
   * Gets drawable IDs.
   * All IDs are null-terminated ANSI strings.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableIds(model: CPointerToModel): CStringArray

  /**
   * Gets constant drawable flags.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableConstantFlags(model: CPointerToModel): CArrayOfByte

  /**
   * Gets dynamic drawable flags.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableDynamicFlags(model: CPointerToModel): CArrayOfByte

  /**
   * Gets drawable texture indices.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableTextureIndices(model: CPointerToModel): CArrayOfInt

  /**
   * Gets drawable draw orders.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableDrawOrders(model: CPointerToModel): CArrayOfInt

  /**
   * Gets drawable render orders.
   * The higher the order, the more up front a drawable is.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0'otherwise.
   */
  def csmGetDrawableRenderOrders(model: CPointerToModel): CArrayOfInt

  /**
   * Gets drawable opacities.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableOpacities(model: CPointerToModel): CArrayOfFloat

  /**
   * Gets numbers of masks of each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableMaskCounts(model: CPointerToModel): CArrayOfInt

  /**
   * Gets mask indices of each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableMasks(model: CPointerToModel): CArrayOfArrayOfInt

  /**
   * Gets number of vertices of each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableVertexCounts(model: CPointerToModel): CArrayOfInt

  /**
   * Gets vertex position data of each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; a null pointer otherwise.
   */
  def csmGetDrawableVertexPositions(model: CPointerToModel): CArrayOfArrayOfCsmVector

  /**
   * Gets texture coordinate data of each drawables.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableVertexUvs(model: CPointerToModel): CArrayOfArrayOfCsmVector

  /**
   * Gets number of triangle indices for each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableIndexCounts(model: CPointerToModel): CArrayOfInt

  /**
   * Gets triangle index data for each drawable.
   *
   * @param  model  Model to query.
   *
   * @return  Valid pointer on success; '0' otherwise.
   */
  def csmGetDrawableIndices(model: CPointerToModel): CArrayOfArrayOfShort

  /**
   * Resets all dynamic drawable flags.
   *
   * @param  model  Model containing flags.
   */
  def csmResetDrawableDynamicFlags(model: CPointerToModel): Unit
}

