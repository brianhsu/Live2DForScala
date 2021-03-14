package com.live2d.framework

import com.live2d.core._
import com.live2d.core.types._
import com.live2d.core.utils.MemoryInfo
import com.live2d.framework.exception._
import com.sun.jna.ptr.FloatByReference

/**
 * The Live 2D model that represent an .moc file.
 *
 * Library user should NEVER create instance of this class directly, it should be obtain
 * from a ``Cubism`` class.
 *
 * @param mocInfo   The moc file information
 * @param core      The core library of Cubism
 */
class Live2DModel(mocInfo: MocInfo)(core: CubismCore) {

  private lazy val revivedMoc: CPointerToMoc = reviveMoc()
  private lazy val modelSize: Int =  core.cLibrary.csmGetSizeofModel(revivedMoc)
  private lazy val modelMemoryInfo: MemoryInfo = core.memoryAllocator.allocate(modelSize, ModelAlignment)
  private lazy val cubismModel: CPointerToModel = {
    core.cLibrary.csmInitializeModelInPlace(revivedMoc, modelMemoryInfo.alignedMemory, modelSize)
  }

  /**
   * Parameters of this model
   *
   * @throws com.live2d.framework.exception.ParameterInitializedException when could not get valid parameters from Live 2D Cubism Model
   */
  lazy val parameters: Map[String, Parameter] = createParameters()

  /**
   * Parts of this model
   *
   * @throws com.live2d.framework.exception.PartInitializedException when could not get valid parts from Live 2D Cubism Model
   */
  lazy val parts: Map[String, Part] = createParts()

  /**
   * Get the canvas info about this Live 2D Model
   *
   * @return  The canvas info
   */
  def canvasInfo: CanvasInfo = {
    val outSizeInPixel = new CsmVector()
    val outOriginInPixel = new CsmVector()
    val outPixelPerUnit = new FloatByReference()

    core.cLibrary.csmReadCanvasInfo(cubismModel, outSizeInPixel, outOriginInPixel, outPixelPerUnit)

    CanvasInfo(outSizeInPixel.x, outSizeInPixel.y, (outOriginInPixel.x, outOriginInPixel.y), outPixelPerUnit.getValue)
  }

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  def update(): Unit = {
    core.cLibrary.csmUpdateModel(cubismModel)
    core.cLibrary.csmResetDrawableDynamicFlags(cubismModel)
  }

  /**
   * Dispose and free the allocated C memory
   */
  def freeUpMemory(): Unit = {
    println("Cleanup memory for Moc...")
    mocInfo.memory.dispose()

    println("Cleanup memory for Model...")
    modelMemoryInfo.dispose()
  }

  private def reviveMoc(): CPointerToMoc = {
    val revivedMoc = core.cLibrary.csmReviveMocInPlace(mocInfo.memory.alignedMemory, mocInfo.originalSize)

    if (revivedMoc == null) {
      throw new MocNotRevivedException
    }

    revivedMoc
  }

  private def createParts(): Map[String, Part] = {
    val partCount = core.cLibrary.csmGetPartCount(cubismModel)
    val partIds = core.cLibrary.csmGetPartIds(cubismModel)
    val parentIndices = core.cLibrary.csmGetPartParentPartIndices(cubismModel)
    val partOpacities = core.cLibrary.csmGetPartOpacities(cubismModel)

    if (partCount == -1 || partIds == null || parentIndices == null || partOpacities == null) {
      throw new PartInitializedException
    }

    var partIdToPart: Map[String, Part] = Map.empty

    for (i <- 0 until partCount) {
      val opacityPointer = partOpacities.getPointerToFloat(i)
      val partId = partIds(i)
      val parentIndex = parentIndices(i)
      val parentId = parentIndex match {
        case n if n >= 0 && n < partCount => Some(partIds(n))
        case _ => None
      }

      val part = Part(opacityPointer, partId, parentId)

      partIdToPart += (partId -> part)
    }

    partIdToPart
  }

  private def createParameters(): Map[String, Parameter] = {
    val parametersCount = core.cLibrary.csmGetParameterCount(cubismModel)
    val parametersIds = core.cLibrary.csmGetParameterIds(cubismModel)
    val currentValues = core.cLibrary.csmGetParameterValues(cubismModel)
    val defaultValues = core.cLibrary.csmGetParameterDefaultValues(cubismModel)
    val minValues = core.cLibrary.csmGetParameterMinimumValues(cubismModel)
    val maxValues = core.cLibrary.csmGetParameterMaximumValues(cubismModel)

    if (parametersCount == -1 || parametersIds == null || currentValues == null ||
        defaultValues == null || minValues == null || maxValues == null) {
      throw new ParameterInitializedException
    }

    var parameters: Map[String, Parameter] = Map.empty
    for (i <- 0 until parametersCount) {
      val id = parametersIds(i)
      val minValue = minValues(i)
      val maxValue = maxValues(i)
      val defaultValue = defaultValues(i)
      val currentValuePointer = currentValues.getPointerToFloat(i)
      val parameter = Parameter(currentValuePointer, id, minValue, maxValue, defaultValue)
      parameters += (id -> parameter)
    }

    parameters
  }

}
