package moe.brianhsu.live2d.framework.model

import com.sun.jna.ptr.FloatByReference
import moe.brianhsu.live2d.core.types.{CPointerToMoc, CPointerToModel, ModelAlignment}
import moe.brianhsu.live2d.core.utils.MemoryInfo
import moe.brianhsu.live2d.core.{CsmVector, ICubismCore}
import moe.brianhsu.live2d.framework.exception.{DrawableInitException, MocNotRevivedException, ParameterInitException, PartInitException, TextureSizeMismatchException}
import moe.brianhsu.live2d.framework.model.drawable.{ConstantFlags, Drawable, DynamicFlags, VertexInfo}
import moe.brianhsu.live2d.framework.{MocInfo, model}
import moe.brianhsu.live2d.framework.math.ModelMatrix

/**
 * The Live 2D model that represent an .moc file.
 *
 * Library user should NEVER create instance of this class directly, it should be obtain
 * from a ``Cubism`` class.
 *
 * @param mocInfo   The moc file information
 * @param core      The core library of Cubism
 */
class Live2DModel(mocInfo: MocInfo, textureFiles: List[String])(core: ICubismCore) {

  private var savedParameters: Map[String, Float] = Map.empty
  private lazy val revivedMoc: CPointerToMoc = reviveMoc()
  private lazy val modelSize: Int =  core.cLibrary.csmGetSizeofModel(this.revivedMoc)
  private lazy val modelMemoryInfo: MemoryInfo = core.memoryAllocator.allocate(this.modelSize, ModelAlignment)
  protected val cubismModel: CPointerToModel = createCubsimModel()

  val modelMatrix: ModelMatrix = new ModelMatrix(canvasInfo.width, canvasInfo.height)

  saveParameters()


  def getTextureFileByIndex(index: Int): String = textureFiles(index)

  def isUsingMasking: Boolean = drawables.values.exists(d => d.masks.nonEmpty)

  def getDrawableByIndex(drawableIndex: Int): Drawable = drawablesByIndex(drawableIndex)

  def isUsingMask: Boolean = drawables.values.exists(x => x.masks.nonEmpty)

  private def createCubsimModel(): CPointerToModel = {
    val model = core.cLibrary.csmInitializeModelInPlace(
      this.revivedMoc,
      this.modelMemoryInfo.alignedMemory,
      this.modelSize
    )

    if (textureFiles.size != getTextureCountFromModel(model)) {
      throw new TextureSizeMismatchException
    }

    model
  }

  private def getTextureCountFromModel(model: CPointerToModel): Int = {
    val drawableCounts = core.cLibrary.csmGetDrawableCount(model)
    val textureIndexList = core.cLibrary.csmGetDrawableTextureIndices(model)
    val maxIndex = (0 until drawableCounts).map(i => textureIndexList(i)).max
    maxIndex + 1
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
   * Drawable of this model.
   *
   * This is a map that key is the drawableId, and value is corresponding Drawable object.
   *
   */
  lazy val drawables: Map[String, Drawable] = createDrawable()

  lazy val drawablesByIndex: List[Drawable] = drawables.values.toList.sortBy(_.index)


  /**
   * This method will access all lazy member fields that load data from the CubismCore C Library,
   * and throws exceptions if there is any corrupted data.
   *
   * @return  The model itself.
   * @throws  DrawableInitException if it cannot construct drawable objects.
   * @throws  MocNotRevivedException if there are errors when reading .moc3 file.
   * @throws  ParameterInitException if it cannot construct parameter objects.
   * @throws  PartInitException if it cannot construct part objects.
   * @throws  TextureSizeMismatchException if the the number of provided texture does not match the information in the model.
   */
  def validAllDataFromNativeLibrary: Live2DModel = {
    this.drawables
    this.parameters
    this.parts
    this
  }

  /**
   * Get the drawables that is sorted by render order.
   *
   * This list is sorted by render order in ascending order.
   */
  def sortedDrawables: List[Drawable] = drawables.values.toList.sortBy(_.renderOrder)

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

  def saveParameters(): Unit = {

    for (parameter <- parameters.values) {
      savedParameters += (parameter.id -> parameter.current)
    }

  }

  def loadParameters(): Unit = {
    savedParameters.foreach { case (id, value) =>
      parameters.get(id).foreach(_.update(value))
    }
  }

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  def update(): Unit = {
    core.cLibrary.csmUpdateModel(this.cubismModel)
    core.cLibrary.csmResetDrawableDynamicFlags(this.cubismModel)
  }

  def reset(): Unit = {
    parameters.values.foreach { p => p.update(p.default) }
    update()
  }

  def setParameterValue(parameterId: String, value: Float, weight: Float = 1.0f): Unit = {
    parameters.get(parameterId).foreach { p =>
      val valueFitInRange = (value * weight).max(p.min).min(p.max)

      if (weight == 1) {
        p.update(valueFitInRange)
      } else {
        p.update((p.current * (1 - weight)) + (valueFitInRange * weight))
      }
    }
  }

  def addParameterValue(id: String, value: Float, weight: Float = 1.0f): Unit = {
    parameters.get(id).foreach { p =>
      setParameterValue(id, p.current + (value * weight))
    }
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

      val part = model.Part(opacityPointer, this, partId, parentId)

      partId -> part
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
      val parameter = Parameter(currentValuePointer, this, id, minValue, maxValue, defaultValue)

      id -> parameter
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
        this, drawableId, i, constantFlags, dynamicFlags, textureIndex, masks,
        vertexInfo, drawOrderPointer, renderOrderPointer, opacityPointer
      )

      drawableId -> drawable
    }.toMap
  }

}
