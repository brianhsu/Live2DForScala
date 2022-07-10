package moe.brianhsu.live2d.boundary.gateway.avatar

import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.enitiy.model.{ModelCanvasInfo, Parameter, Part}

import scala.util.Try

trait ModelBackend {

  /**
   * The list of texture file path of this model.
   */
  def textureFiles: List[String]

  /**
   * Parameters of this model
   *
   * This is a map that key is the parameterId, and value is corresponding Parameter object.
   *
   */
  def parameters: Map[String, Parameter]

  /**
   * Parts of this model
   *
   * This is a map that key is the partId, and value is corresponding Part object.
   *
   */
  def parts: Map[String, Part]

  /**
   * Drawable of this model.
   *
   * This is a map that key is the drawableId, and value is corresponding Drawable object.
   *
   */
  def drawables: Map[String, Drawable]

  /**
   * Get the canvas info about this Live 2D Model
   *
   * @return  The canvas info
   */
  def canvasInfo: ModelCanvasInfo

  /**
   * This method will access all lazy member fields that load data from the CubismCore C Library,
   * and return a Failure if there is any corrupted data, otherwise it will return a Success[ModelBackend].
   *
   * @return  The model itself.
   */
  def validatedBackend: Try[ModelBackend]

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  def update(): Unit

}
