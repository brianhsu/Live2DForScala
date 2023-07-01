package moe.brianhsu.utils.mock

import com.sun.jna.Memory
import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable.ColorFetcher
import moe.brianhsu.live2d.enitiy.model.drawable.{ConstantFlags, Drawable, DrawableColor, DynamicFlags}
import moe.brianhsu.live2d.enitiy.model.parameter.Parameter
import moe.brianhsu.live2d.enitiy.model.{ModelCanvasInfo, Part}
import org.scalamock.scalatest.MockFactory

import scala.util.{Success, Try}

trait Live2DModelMock {
  this: MockFactory =>
  private val mockedCanvasInfo = ModelCanvasInfo(1980, 1020, (0, 0), 1)
  private val mockedFetcher: ColorFetcher = () => DrawableColor(1.0f, 1.0f, 1.0f, 1.0f)

  def createDrawable(id: String, index: Int, hasMask: Boolean = false, renderOrder: Int = 0): Drawable = {
    val masks = if (hasMask) List(1, 2, 3) else Nil

    val renderOrderPointer = new Memory(4)
    renderOrderPointer.setInt(0, renderOrder)

    Drawable(
      id, index, None, ConstantFlags(0), new DynamicFlags(null),
      textureIndex = 0, masks, vertexInfo = null, drawOrderPointer = null,
      renderOrderPointer = renderOrderPointer,
      opacityPointer = null, mockedFetcher, mockedFetcher
    )
  }

  def createDrawable(id: String, index: Int, masks: List[Int]): Drawable = {
    Drawable(
      id, index, None, ConstantFlags(0), new DynamicFlags(null), textureIndex = 0,
      masks, vertexInfo = null, drawOrderPointer = null, renderOrderPointer = null,
      opacityPointer = null, mockedFetcher, mockedFetcher
    )
  }

  class MockedBackend(override val textureFiles: List[String] = Nil,
                      override val parameters: Map[String, Parameter] = Map.empty,
                      override val parts: Map[String, Part] = Map.empty,
                      override val drawables: Map[String, Drawable] = Map.empty,
                      override val canvasInfo: ModelCanvasInfo = mockedCanvasInfo) extends ModelBackend {
    var updatedCount: Int = 0
    override def validatedBackend: Try[ModelBackend] = Success(this)
    override def update(): Unit = {
      updatedCount += 1
    }
  }


}
