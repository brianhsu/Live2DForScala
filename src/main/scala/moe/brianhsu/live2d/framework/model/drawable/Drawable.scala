package moe.brianhsu.live2d.framework.model.drawable

import com.sun.jna.Pointer

case class Drawable(id: String, constantFlags: ConstantFlags, dynamicFlags: DynamicFlags,
                    textureIndex: Int, masks: List[Int], vertexInfo: VertexInfo,
                    private val drawOrderPointer: Pointer,
                    private val renderOrderPointer: Pointer,
                    private val opacityPointer: Pointer) {

  def drawOrder = drawOrderPointer.getInt(0)
  def renderOrder = renderOrderPointer.getInt(0)
  def opacity = opacityPointer.getFloat(0)
}
