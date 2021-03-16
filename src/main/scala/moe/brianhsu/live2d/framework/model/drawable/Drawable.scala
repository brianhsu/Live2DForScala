package moe.brianhsu.live2d.framework.model.drawable

import com.sun.jna.Pointer

/**
 * This class represent the drawable objects inside a Live2D model.
 *
 * @param id                  The drawable id.
 * @param constantFlags       The constant flags.
 * @param dynamicFlags        The dynamic flags.
 * @param textureIndex        The texture index indicates which texture should be used when rendering.
 * @param masks               The masks information.
 * @param vertexInfo          The vertex info for rendering.
 * @param drawOrderPointer    The pointer to the actual memory address of draw order value.
 * @param renderOrderPointer  The pointer to the actual memory address of render order value.
 * @param opacityPointer      The pointer to the actual memory address of opacity order value.
 */
case class Drawable(id: String, constantFlags: ConstantFlags, dynamicFlags: DynamicFlags,
                    textureIndex: Int, masks: List[Int], vertexInfo: VertexInfo,
                    private val drawOrderPointer: Pointer,
                    private val renderOrderPointer: Pointer,
                    private val opacityPointer: Pointer) {

  /**
   * Get draw order of this drawable.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.core.CubismCore]].
   *
   * @return  The draw order of this drawable.
   */
  def drawOrder = drawOrderPointer.getInt(0)

  /**
   * Get render order of this drawable.
   *
   * The higher the order, the more up front a drawable is.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.core.CubismCore]].
   *
   * @return  The render order of this drawable.
   */
  def renderOrder = renderOrderPointer.getInt(0)

  /**
   * Get opacity of this drawable.
   *
   * This value will be read directly from the native C memory allocated by [[moe.brianhsu.live2d.core.CubismCore]].
   *
   * @return  The opacity of this drawable.
   */
  def opacity = opacityPointer.getFloat(0)
}
