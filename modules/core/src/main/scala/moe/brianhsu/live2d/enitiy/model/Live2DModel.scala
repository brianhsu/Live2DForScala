package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.math.matrix.ModelMatrix
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable

class Live2DModel(modelBackend: ModelBackend) {
  private var savedParameters: Map[String, Float] = Map.empty
  private var fallbackParameters: Map[String, Parameter] = Map.empty
  private var modelMatrixHolder: Option[ModelMatrix] = None

  def modelMatrix: ModelMatrix = {
    if (modelMatrixHolder.isEmpty) {
      modelMatrixHolder = Some(ModelMatrix(canvasInfo.width, canvasInfo.height))
    }
    modelMatrixHolder.get
  }

  def modelMatrix_=(matrix: ModelMatrix): Unit = {
    modelMatrixHolder = Some(matrix)
  }

  /**
   * The list of texture file path of this model.
   */
  def textureFiles: List[String] = modelBackend.textureFiles

  /**
   * Parameters of this model
   *
   * This is a map that key is the parameterId, and value is corresponding Parameter object.
   */
  def parameters: Map[String, Parameter] = modelBackend.parameters

  /**
   * Parts of this model
   *
   * This is a map that key is the partId, and value is corresponding Part object.
   */
  def parts: Map[String, Part] = modelBackend.parts

  /**
   * Drawable of this model.
   *
   * This is a map that key is the drawableId, and value is corresponding Drawable object.
   */
  def drawables: Map[String, Drawable] = modelBackend.drawables

  /**
   * Get the canvas info about this Live 2D Model
   *
   * @return The canvas info
   */
  def canvasInfo: ModelCanvasInfo = modelBackend.canvasInfo

  /**
   * Drawable sorted by index
   *
   * This will be a list that contains the drawable sorted according to the drawable
   * index inside the model, in ascending order.
   */
  lazy val drawablesByIndex: List[Drawable] = sortDrawableByIndex()

  /**
   * Is this Parameter ID old format
   *
   * The old format is like `PARAM_EYE_R_OPEN`, and the new format
   * is `ParamEyeROpen`.
   */
  lazy val isOldParameterId = parameters.keySet.exists(_.contains("PARAM_"))

  /**
   * Does the drawables of this model use masking?
   *
   * @return true if any drawable of this model has masks, otherwise false.
   */
  lazy val containMaskedDrawables: Boolean = anyDrawableHasMask

  /**
   * Get the drawables that is sorted by render order.
   *
   * This list is sorted by render order in ascending order.
   */
  def sortedDrawables: List[Drawable] = sortDrawableByRenderOrder()

  /**
   * Snapshot current value of parameters that is backed by model backend.
   *
   * @note This will NOT snapshot the fallback parameters created by [[parameterWithFallback]].
   */
  def snapshotParameters(): Unit = {

    for (parameter <- parameters.values) {
      savedParameters += (parameter.id -> parameter.current)
    }

  }

  /**
   * Restore model backend backed parameters value from previous snapshot.
   *
   * @note This will NOT restore the fallback parameters created by [[parameterWithFallback]].
   */
  def restoreParameters(): Unit = {
    savedParameters.foreach { case (id, value) =>
      parameters.get(id).foreach(_.update(value))
    }
  }

  /**
   * Update model
   *
   * Update the Live2D model status according to current parameters.
   */
  def update(): Unit = {
    modelBackend.update()
  }

  /**
   * Reset all parameters to default value and update model
   */
  def reset(): Unit = {
    parameters.values.foreach { p => p.update(p.default) }
    update()
  }

  /**
   * Get parameter with fallback
   *
   * This method will first look into the model backend, see if there is parameter has
   * the same id of the `parameterId` argument. If so, it will return that parameter.
   *
   * Otherwise, it will create a dummy one inside this `Live2DModel` instance, and
   * return that dummy parameter when user requested that parameterId by this method
   * again.
   *
   * This is essential for motions, as Live 2D's motion use these dummy parameter to
   * track some properties of motions that does not resident inside the .moc model it self.
   *
   * @param parameterId The id of parameter
   *
   * @return The requested parameter, either backed by backend or a in-memory dummy one.
   */
  def parameterWithFallback(parameterId: String): Parameter = {
    parameters.get(parameterId)
      .orElse(fallbackParameters.get(parameterId))
      .getOrElse {
        val newParameter = new JavaVMParameter(parameterId)
        this.fallbackParameters += (parameterId -> newParameter)
        newParameter
      }
  }

  /**
   * Test if the provided coordinate is inside an drawable.
   *
   * @param drawableId  The drawable id wish to test.
   * @param pointX      The X coordinate
   * @param pointY      The Y coordinate
   * @return If the coordinate is inside the boundary of `drawableId`
   */
  def isHit(drawableId: String, pointX: Float, pointY: Float): Boolean = {
    val isHitHolder = drawables.get(drawableId).map { drawable =>
      val xCoordinates = drawable.vertexInfo.positions.map(_._1)
      val yCoordinates = drawable.vertexInfo.positions.map(_._2)
      val left: Float = xCoordinates.min
      val right: Float = xCoordinates.max
      val top: Float = yCoordinates.min
      val bottom = yCoordinates.max

      val transformedX = modelMatrix.invertedTransformedX(pointX)
      val transformedY = modelMatrix.invertedTransformedY(pointY)

      (left <= transformedX) &&
        (transformedX <= right) &&
        (top <= transformedY) &&
        (transformedY <= bottom)
    }
    isHitHolder.getOrElse(false)
  }

  private def sortDrawableByIndex(): List[Drawable] = {
    drawables.values.toList.sortBy(_.index)
  }

  private def sortDrawableByRenderOrder(): List[Drawable] = {
    drawables.values.toList.sortBy(_.renderOrder)
  }

  private def anyDrawableHasMask:Boolean = {
    drawables.values.exists(d => d.masks.nonEmpty)
  }

}
