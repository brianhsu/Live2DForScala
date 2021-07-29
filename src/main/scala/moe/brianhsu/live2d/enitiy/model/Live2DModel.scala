package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.porting.live2d.framework.math.ModelMatrix
import moe.brianhsu.porting.live2d.framework.model.drawable.Drawable
import moe.brianhsu.porting.live2d.framework.model.{CanvasInfo, Part}

class Live2DModel(modelBackend: ModelBackend) {
  private var savedParameters: Map[String, Float] = Map.empty
  private var fallbackParameters: Map[String, Parameter] = Map.empty

  lazy val modelMatrix: ModelMatrix = new ModelMatrix(canvasInfo.width, canvasInfo.height)

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
  def canvasInfo: CanvasInfo = modelBackend.canvasInfo

  // TODO: Should delete this and has a better way to do this.
  def validateAllData: Unit = modelBackend.validateAllData()

  /**
   * Drawable sorted by index
   *
   * This will be a list that contains the drawable sorted according to the drawable
   * index inside the model, in ascending order.
   */
  lazy val drawablesByIndex: List[Drawable] = sortDrawableByIndex()

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

  def snapshotParameters(): Unit = {

    for (parameter <- parameters.values) {
      savedParameters += (parameter.id -> parameter.current)
    }

  }

  def restoreParameters(): Unit = {
    savedParameters.foreach { case (id, value) =>
      parameters.get(id).foreach(_.update(value))
    }
  }

  def update(): Unit = modelBackend.update()

  def reset(): Unit = {
    parameters.values.foreach { p => p.update(p.default) }
    update()
  }

  def getParameterWithFallback(parameterId: String): Parameter = {
    parameters.get(parameterId)
      .orElse(fallbackParameters.get(parameterId))
      .getOrElse {
        val newParameter = new JavaVMParameter(parameterId)
        this.fallbackParameters += (parameterId -> newParameter)
        newParameter
      }
  }

  def isHit(drawableId: String, pointX: Float, pointY: Float): Boolean = {
    val isHitHolder = drawables.get(drawableId).map { drawable =>
      val vertices = drawable.vertexInfo.positions

      var left: Float = vertices.head._1
      var right: Float = vertices.head._1
      var top = vertices.head._2
      var bottom = vertices.head._2

      for (vertex <- vertices.drop(1)) {
        val (x, y) = vertex
        if (x < left) {
          left = x; // Min x
        }

        if (x > right) {
          right = x; // Max x
        }

        if (y < top) {
          top = y; // Min y
        }

        if (y > bottom) {
          bottom = y; // Max y
        }
      }
      val tx = modelMatrix.invertTransformX(pointX)
      val ty = modelMatrix.invertTransformY(pointY)

      (left <= tx) &&
        (tx <= right) &&
        (top <= ty) &&
        (ty <= bottom)
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
