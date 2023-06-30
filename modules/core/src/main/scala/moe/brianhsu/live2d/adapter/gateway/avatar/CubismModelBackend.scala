package moe.brianhsu.live2d.adapter.gateway.avatar

import com.sun.jna.ptr.FloatByReference
import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.core.CsmCoordinate
import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo
import moe.brianhsu.live2d.enitiy.core.types.{CPointerToModel, ModelAlignment}
import moe.brianhsu.live2d.enitiy.model
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable.ColorFetcher
import moe.brianhsu.live2d.enitiy.model.drawable._
import moe.brianhsu.live2d.enitiy.model.parameter.{CPointerParameter, Parameter, ParameterType}
import moe.brianhsu.live2d.enitiy.model.{MocInfo, ModelCanvasInfo, Part}
import moe.brianhsu.live2d.exception._

import scala.util.Try

/**
 * The Live 2D model that represent an .moc file.
 *
 * Library user should NEVER create instance of this class directly, it should be obtain
 * from a ``Cubism`` class.
 *
 * @param mocInfo   The moc file information
 * @param core      The core library of Cubism
 */
class CubismModelBackend(mocInfo: MocInfo, override val textureFiles: List[String])(implicit core: NativeCubismAPILoader) extends ModelBackend {

  private lazy val modelSize: Int =  core.cubismAPI.csmGetSizeofModel(this.mocInfo.revivedMoc)
  private lazy val modelMemoryInfo: MemoryInfo = core.memoryAllocator.allocate(this.modelSize, ModelAlignment)
  protected lazy val cubismModel: CPointerToModel = createCubsimModel()

  private def createCubsimModel(): CPointerToModel = {

    val model = core.cubismAPI.csmInitializeModelInPlace(
      this.mocInfo.revivedMoc,
      this.modelMemoryInfo.alignedMemory,
      this.modelSize
    )
    val expectedTextureFileCount = calculateTextureCountFromModel(model)

    if (textureFiles.size != expectedTextureFileCount) {
      throw new TextureSizeMismatchException(expectedTextureFileCount)
    }

    model
  }

  private def calculateTextureCountFromModel(model: CPointerToModel): Int = {
    val drawableCounts = core.cubismAPI.csmGetDrawableCount(model)
    val textureIndexList = core.cubismAPI.csmGetDrawableTextureIndices(model)
    val maxIndex = (0 until drawableCounts).map(i => textureIndexList(i)).max
    maxIndex + 1
  }

  /**
   * Parameters of this model
   *
   * This is a map that key is the parameterId, and value is corresponding Parameter object.
   *
   * @throws moe.brianhsu.live2d.exception.ParameterInitException when could not get valid parameters from Live 2D Cubism Model
   */
  override lazy val parameters: Map[String, Parameter] = createParameters()

  /**
   * Parts of this model
   *
   * This is a map that key is the partId, and value is corresponding Part object.
   *
   * @throws moe.brianhsu.live2d.exception.PartInitException when could not get valid parts from Live 2D Cubism Model
   */
  override lazy val parts: Map[String, Part] = createParts()

  /**
   * Drawable of this model.
   *
   * This is a map that key is the drawableId, and value is corresponding Drawable object.
   *
   */
  override lazy val drawables: Map[String, Drawable] = createDrawable()

  /**
   * Get the canvas info about this Live 2D Model
   *
   * @return  The canvas info
   */
  override lazy val canvasInfo: ModelCanvasInfo = createCanvasInfo()

  /**
   * This method will access all lazy member fields that load data from the CubismCore C Library,
   * and return a Failure if there is any corrupted data, otherwise it will return a Success[ModelBackend].
   *
   * @return  The model itself.
   */
  override def validatedBackend: Try[ModelBackend] = Try {
    this.mocInfo.revivedMoc
    this.modelSize
    this.drawables
    this.modelMemoryInfo
    this.cubismModel
    this.parameters
    this.parts
    this
  }

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  override def update(): Unit = {
    core.cubismAPI.csmUpdateModel(this.cubismModel)
    core.cubismAPI.csmResetDrawableDynamicFlags(this.cubismModel)
  }

  private def createCanvasInfo() = {
    val outSizeInPixel = new CsmCoordinate()
    val outOriginInPixel = new CsmCoordinate()
    val outPixelPerUnit = new FloatByReference()

    core.cubismAPI.csmReadCanvasInfo(this.cubismModel, outSizeInPixel, outOriginInPixel, outPixelPerUnit)

    ModelCanvasInfo(
      outSizeInPixel.getX, outSizeInPixel.getY,
      (outOriginInPixel.getX, outOriginInPixel.getY),
      outPixelPerUnit.getValue
    )
  }


  private def createPartList(): List[Part] = {
    val partCount = core.cubismAPI.csmGetPartCount(this.cubismModel)
    val partIds = core.cubismAPI.csmGetPartIds(this.cubismModel)
    val parentIndices = core.cubismAPI.csmGetPartParentPartIndices(this.cubismModel)
    val partOpacities = core.cubismAPI.csmGetPartOpacities(this.cubismModel)

    if (partCount == -1 || partIds == null || parentIndices == null || partOpacities == null) {
      throw new PartInitException
    }

    val range = (0 until partCount).toList

    range.map { i =>
      val opacityPointer = partOpacities.pointerToFloat(i)
      val partId = partIds(i)
      val parentIndex = parentIndices(i)
      val parentId = parentIndex match {
        case n if n >= 0 && n < partCount => Some(partIds(n))
        case _ => None
      }

      val part = model.Part(opacityPointer, partId, parentId)

      part
    }
  }

  private def createParts(): Map[String, Part] = {
    createPartList().map(part => part.id -> part).toMap
  }

  private def createParameterList(): List[Parameter] = {
    val parametersCount = core.cubismAPI.csmGetParameterCount(this.cubismModel)
    val parametersIds = core.cubismAPI.csmGetParameterIds(this.cubismModel)
    val currentValues = core.cubismAPI.csmGetParameterValues(this.cubismModel)
    val defaultValues = core.cubismAPI.csmGetParameterDefaultValues(this.cubismModel)
    val minValues = core.cubismAPI.csmGetParameterMinimumValues(this.cubismModel)
    val maxValues = core.cubismAPI.csmGetParameterMaximumValues(this.cubismModel)
    val parameterTypes = core.cubismAPI.csmGetParameterTypes(this.cubismModel)

    if (parametersCount == -1 || parametersIds == null || currentValues == null ||
      defaultValues == null || minValues == null || maxValues == null || parameterTypes == null) {
      throw new ParameterInitException
    }

    val range = (0 until parametersCount).toList

    range.map { i =>
      val id = parametersIds(i)
      val minValue = minValues(i)
      val maxValue = maxValues(i)
      val defaultValue = defaultValues(i)
      val currentValuePointer = currentValues.pointerToFloat(i)
      val parameterType = ParameterType(parameterTypes(i))
      val parameter = CPointerParameter(currentValuePointer, id, parameterType, minValue, maxValue, defaultValue)
      parameter
    }

  }

  private def createParameters(): Map[String, Parameter] = {
    createParameterList().map(p => p.id -> p).toMap
  }

  private def createDrawable(): Map[String, Drawable] = {
    val drawableCounts = core.cubismAPI.csmGetDrawableCount(this.cubismModel)
    val drawableIdList = core.cubismAPI.csmGetDrawableIds(this.cubismModel)
    val constantFlagsList = core.cubismAPI.csmGetDrawableConstantFlags(this.cubismModel)
    val dynamicFlagsList = core.cubismAPI.csmGetDrawableDynamicFlags(this.cubismModel)
    val textureIndexList = core.cubismAPI.csmGetDrawableTextureIndices(this.cubismModel)
    val drawOrderList = core.cubismAPI.csmGetDrawableDrawOrders(this.cubismModel)
    val renderOrderList = core.cubismAPI.csmGetDrawableRenderOrders(this.cubismModel)
    val opacityList = core.cubismAPI.csmGetDrawableOpacities(this.cubismModel)

    if (drawableCounts == -1 || drawableIdList == null || constantFlagsList == null ||
      dynamicFlagsList == null || textureIndexList == null || drawOrderList == null ||
      renderOrderList == null || opacityList == null) {
      throw new DrawableInitException
    }

    // Mask Related
    val maskCountList = core.cubismAPI.csmGetDrawableMaskCounts(this.cubismModel)
    val masksList = core.cubismAPI.csmGetDrawableMasks(this.cubismModel)

    if (maskCountList == null || masksList == null) {
      throw new DrawableInitException
    }

    // Vertex Related
    val indexCountList = core.cubismAPI.csmGetDrawableIndexCounts(this.cubismModel)
    val indexList = core.cubismAPI.csmGetDrawableIndices(this.cubismModel)
    val vertexCountList = core.cubismAPI.csmGetDrawableVertexCounts(this.cubismModel)
    val positionList =  core.cubismAPI.csmGetDrawableVertexPositions(this.cubismModel)
    val textureCoordinateList =  core.cubismAPI.csmGetDrawableVertexUvs(this.cubismModel)

    if (indexCountList == null || indexList == null || vertexCountList == null ||
        positionList == null || textureCoordinateList == null) {
      throw new DrawableInitException
    }


    val range = (0 until drawableCounts).toList

    range.map { index =>
      val drawableId = drawableIdList(index)
      val constantFlags = ConstantFlags(constantFlagsList(index))
      val dynamicFlags = new DynamicFlags(dynamicFlagsList.pointerToByte(index))
      val textureIndex = textureIndexList(index)
      val drawOrderPointer = drawOrderList.pointerToInt(index)
      val renderOrderPointer = renderOrderList.pointerToInt(index)
      val opacityPointer = opacityList.pointerToFloat(index)
      val maskCount = maskCountList(index)
      val masks = (0 until maskCount).toList.map(j => masksList(index)(j))
      val vertexInfo = new VertexInfo(
        vertexCountList(index),
        indexCountList(index),
        positionList(index),
        textureCoordinateList(index),
        indexList(index)
      )

      val multiplyColorFetcher: ColorFetcher = () => {
        val nativeColor = core.cubismAPI.csmGetDrawableMultiplyColors(cubismModel)(index)
        DrawableColor(nativeColor.red, nativeColor.green, nativeColor.blue, nativeColor.alpha)
      }

      val screenColorFetcher: ColorFetcher = () => {
        val nativeColor = core.cubismAPI.csmGetDrawableScreenColors(cubismModel)(index)
        DrawableColor(nativeColor.red, nativeColor.green, nativeColor.blue, nativeColor.alpha)
      }

      val drawable = Drawable(
        drawableId, index, constantFlags, dynamicFlags, textureIndex,
        masks, vertexInfo, drawOrderPointer, renderOrderPointer, opacityPointer,
        multiplyColorFetcher, screenColorFetcher
      )

      drawableId -> drawable
    }.toMap
  }
}
