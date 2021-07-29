package moe.brianhsu.live2d.boundary.gateway.avatar

import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.porting.live2d.framework.model.{CanvasInfo, Part}
import moe.brianhsu.porting.live2d.framework.model.drawable.Drawable

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
  def canvasInfo: CanvasInfo

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
  def validateAllData(): Unit

  /**
   * Update the Live 2D Model and reset all dynamic flags of drawables.
   */
  def update()

}
