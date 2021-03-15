package moe.brianhsu.live2d.framework.model

import com.sun.jna.ptr.FloatByReference
import moe.brianhsu.live2d.core.types.{CPointerToMoc, CPointerToModel, ModelAlignment}
import moe.brianhsu.live2d.core.utils.MemoryInfo
import moe.brianhsu.live2d.core.{CsmVector, ICubismCore}
import moe.brianhsu.live2d.framework.exception.{DrawableInitException, MocNotRevivedException, ParameterInitException, PartInitException}
import moe.brianhsu.live2d.framework.model.drawable.{ConstantFlags, Drawable, DynamicFlags, VertexInfo}
import moe.brianhsu.live2d.framework.{MocInfo, model}

/**
 * The Live 2D model that represent an .moc file.
 *
 * Library user should NEVER create instance of this class directly, it should be obtain
 * from a ``Cubism`` class.
 *
 * @param mocInfo   The moc file information
 * @param core      The core library of Cubism
 */
class Live2DModel(mocInfo: MocInfo)(core: ICubismCore) {
  private lazy val revivedMoc: CPointerToMoc = reviveMoc()
  private lazy val modelSize: Int =  core.cLibrary.csmGetSizeofModel(this.revivedMoc)
  private lazy val modelMemoryInfo: MemoryInfo = core.memoryAllocator.allocate(this.modelSize, ModelAlignment)
  protected lazy val cubismModel: CPointerToModel = {
    core.cLibrary.csmInitializeModelInPlace(
      this.revivedMoc,
      this.modelMemoryInfo.alignedMemory,
      this.modelSize
    )
  }

  /**
   * Parameters of this model
   *
   * This is a map that key is the parameterId, and value is corresponding Parameter object.
   *
   * @throws moe.brianhsu.live2d.framework.exception.ParameterInitException when could not get valid parameters from Live 2D Cubism Model
   */
  lazy val parameters: Map[String, Parameter] = createParameters()

  /**
   * Parts of this model
   *
   * This is a map that key is the partId, and value is corresponding Part object.
   *
   * @throws moe.brianhsu.live2d.framework.exception.PartInitException when could not get valid parts from Live 2D Cubism Model
   */
  lazy val parts: Map[String, Part] = createParts()

  /**
   * Drwables of this model.
   *
   * This is a map that key is the drawableId, and value is corresponding Drawable object.
   *
   */
  lazy val drawables: Map[String, Drawable] = createDrawable()

  /**
   * Get the canvas info about this Live 2D Model
   *
   * @return  The canvas info
   */
  def canvasInfo: CanvasInfo = {
    val outSizeInPixel = new CsmVector()
    val outOriginInPixel = new CsmVector()
    val outPixelPerUnit = new FloatByReference()

    core.cLibrary.csmReadCanvasInfo(this.cubismModel, outSizeInPixel, outOriginInPixel, outPixelPerUnit)

    CanvasInfo(
      outSizeInPixel.getX, outSizeInPixel.getY,
      (outOriginInPixel.getX, outOriginInPixel.getY), outPixelPerUnit.getValue)
  }

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  def update(): Unit = {
    core.cLibrary.csmUpdateModel(this.cubismModel)
    core.cLibrary.csmResetDrawableDynamicFlags(this.cubismModel)
  }

  private def reviveMoc(): CPointerToMoc = {
    val revivedMoc = core.cLibrary.csmReviveMocInPlace(mocInfo.memory.alignedMemory, mocInfo.originalSize)

    if (revivedMoc == null) {
      throw new MocNotRevivedException
    }

    revivedMoc
  }

  private def createParts(): Map[String, Part] = {
    val partCount = core.cLibrary.csmGetPartCount(this.cubismModel)
    val partIds = core.cLibrary.csmGetPartIds(this.cubismModel)
    val parentIndices = core.cLibrary.csmGetPartParentPartIndices(this.cubismModel)
    val partOpacities = core.cLibrary.csmGetPartOpacities(this.cubismModel)

    if (partCount == -1 || partIds == null || parentIndices == null || partOpacities == null) {
      throw new PartInitException
    }

    val range = (0 until partCount).toList

    range.map { i =>
      val opacityPointer = partOpacities.getPointerToFloat(i)
      val partId = partIds(i)
      val parentIndex = parentIndices(i)
      val parentId = parentIndex match {
        case n if n >= 0 && n < partCount => Some(partIds(n))
        case _ => None
      }

      val part = model.Part(opacityPointer, partId, parentId)

      (partId -> part)
    }.toMap
  }

  private def createParameters(): Map[String, Parameter] = {
    val parametersCount = core.cLibrary.csmGetParameterCount(this.cubismModel)
    val parametersIds = core.cLibrary.csmGetParameterIds(this.cubismModel)
    val currentValues = core.cLibrary.csmGetParameterValues(this.cubismModel)
    val defaultValues = core.cLibrary.csmGetParameterDefaultValues(this.cubismModel)
    val minValues = core.cLibrary.csmGetParameterMinimumValues(this.cubismModel)
    val maxValues = core.cLibrary.csmGetParameterMaximumValues(this.cubismModel)

    if (parametersCount == -1 || parametersIds == null || currentValues == null ||
        defaultValues == null || minValues == null || maxValues == null) {
      throw new ParameterInitException
    }

    val range = (0 until parametersCount).toList

    range.map { i =>
      val id = parametersIds(i)
      val minValue = minValues(i)
      val maxValue = maxValues(i)
      val defaultValue = defaultValues(i)
      val currentValuePointer = currentValues.getPointerToFloat(i)
      val parameter = Parameter(currentValuePointer, id, minValue, maxValue, defaultValue)

      (id -> parameter)
    }.toMap
  }

  private def createDrawable(): Map[String, Drawable] = {
    val drawableCounts = core.cLibrary.csmGetDrawableCount(this.cubismModel)
    val drawableIdList = core.cLibrary.csmGetDrawableIds(this.cubismModel)
    val constantFlagsList = core.cLibrary.csmGetDrawableConstantFlags(this.cubismModel)
    val dynamicFlagsList = core.cLibrary.csmGetDrawableDynamicFlags(this.cubismModel)
    val textureIndexList = core.cLibrary.csmGetDrawableTextureIndices(this.cubismModel)
    val drawOrderList = core.cLibrary.csmGetDrawableDrawOrders(this.cubismModel)
    val renderOrderList = core.cLibrary.csmGetDrawableRenderOrders(this.cubismModel)
    val opacityList = core.cLibrary.csmGetDrawableOpacities(this.cubismModel)

    if (drawableCounts == -1 || drawableIdList == null || constantFlagsList == null ||
      dynamicFlagsList == null || textureIndexList == null || drawOrderList == null ||
      renderOrderList == null || opacityList == null) {
      throw new DrawableInitException
    }

    // Mask Related
    val maskCountList = core.cLibrary.csmGetDrawableMaskCounts(this.cubismModel)
    val masksList = core.cLibrary.csmGetDrawableMasks(this.cubismModel)

    if (maskCountList == null || masksList == null) {
      throw new DrawableInitException
    }

    // Vertex Related
    val indexCountList = core.cLibrary.csmGetDrawableIndexCounts(this.cubismModel)
    val indexList = core.cLibrary.csmGetDrawableIndices(this.cubismModel)
    val vertexCountList = core.cLibrary.csmGetDrawableVertexCounts(this.cubismModel)
    val positionList =  core.cLibrary.csmGetDrawableVertexPositions(this.cubismModel)
    val textureCoordinateList =  core.cLibrary.csmGetDrawableVertexUvs(this.cubismModel)

    if (indexCountList == null || indexList == null || vertexCountList == null ||
        positionList == null || textureCoordinateList == null) {
      throw new DrawableInitException
    }


    val range = (0 until drawableCounts).toList

    range.map { i =>
      val drawableId = drawableIdList(i)
      val constantFlags = ConstantFlags(constantFlagsList(i))
      val dynamicFlags = DynamicFlags(dynamicFlagsList.getPointerToByte(i))
      val textureIndex = textureIndexList(i)
      val drawOrderPointer = drawOrderList.getPointerToInt(i)
      val renderOrderPointer = renderOrderList.getPointerToInt(i)
      val opacityPointer = opacityList.getPointerToFloat(i)
      val maskCount = maskCountList(i)
      val masks = (0 until maskCount).toList.map(j => masksList(i)(j))
      val vertexInfo = VertexInfo(
        vertexCountList(i),
        indexCountList(i),
        positionList(i),
        textureCoordinateList(i),
        indexList(i)
      )

      val drawable = Drawable(
        drawableId, constantFlags, dynamicFlags, textureIndex, masks,
        vertexInfo, drawOrderPointer, renderOrderPointer, opacityPointer
      )

      (drawableId -> drawable)
    }.toMap
  }

}
