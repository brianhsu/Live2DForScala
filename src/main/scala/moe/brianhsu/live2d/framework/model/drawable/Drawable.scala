package moe.brianhsu.live2d.framework.model.drawable

import com.sun.jna.Pointer
import moe.brianhsu.live2d.framework.model.Live2DModel

/**
 * This class represent the drawable objects inside a Live2D model.
 *
 * @param belongsTo           Which Live2D model this drawable belongs to.
 * @param id                  The drawable id.
 * @param index               The zero-based index that the drawable inside the original C drawables array.
 * @param constantFlags       The constant flags.
 * @param dynamicFlags        The dynamic flags.
 * @param textureIndex        The texture index indicates which texture should be used when rendering.
 * @param masks               The masks information.
 * @param vertexInfo          The vertex info for rendering.
 * @param drawOrderPointer    The pointer to the actual memory address of draw order value.
 * @param renderOrderPointer  The pointer to the actual memory address of render order value.
 * @param opacityPointer      The pointer to the actual memory address of opacity order value.
 */
case class Drawable(belongsTo: Live2DModel, id: String, index: Int, constantFlags: ConstantFlags, dynamicFlags: DynamicFlags,
                    textureIndex: Int, masks: List[Int], vertexInfo: VertexInfo,
                    private val drawOrderPointer: Pointer,
                    private val renderOrderPointer: Pointer,
                    private val opacityPointer: Pointer) {

  def isCulling: Boolean = !constantFlags.isDoubleSided


  /**
   * Get draw order of this drawable.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.enitiy.core.CubismCore]].
   *
   * @return  The draw order of this drawable.
   */
  def drawOrder: Int = drawOrderPointer.getInt(0)

  /**
   * Get render order of this drawable.
   *
   * The higher the order, the more up front a drawable is.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.enitiy.core.CubismCore]].
   *
   * @return  The render order of this drawable.
   */
  def renderOrder: Int = renderOrderPointer.getInt(0)

  /**
   * Get opacity of this drawable.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.enitiy.core.CubismCore]].
   *
   * @return  The opacity of this drawable.
   */
  def opacity: Float = opacityPointer.getFloat(0)
}
