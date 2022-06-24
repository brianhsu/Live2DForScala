package moe.brianhsu.live2d.usecase.renderer.opengl.clipping

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext.Layout
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureColor

object ClippingContext {
  private val ChannelColors = Map(
    0 -> TextureColor(1.0f, 0.0f, 0.0f, 0.0f),
    1 -> TextureColor(0.0f, 1.0f, 0.0f, 0.0f),
    2 -> TextureColor(0.0f, 0.0f, 1.0f, 0.0f),
    3 -> TextureColor(0.0f, 0.0f, 0.0f, 1.0f),
  )

  val ColorChannelCount: Int = ChannelColors.size

  case class Layout(channel: Int, bounds: Rectangle) {
    def channelColor: TextureColor = ChannelColors(channel)
  }
}

case class ClippingContext(maskDrawable: List[Drawable], clippedDrawables: List[Drawable],
                           allClippedDrawableBounds: Option[Rectangle] = None,
                           matrixForMask: GeneralMatrix = new GeneralMatrix,
                           matrixForDraw: GeneralMatrix = new GeneralMatrix,
                           layout: Layout = Layout(0, Rectangle())) {

  def isUsing: Boolean = allClippedDrawableBounds.isDefined

  def calculateMatrix(): ClippingContext = {
    val allClippedDrawRect = this.allClippedDrawableBounds.getOrElse(Rectangle())
    val margin = 0.05f
    val tmpBoundsOnModel = allClippedDrawRect.expand(
      allClippedDrawRect.width * margin,
      allClippedDrawRect.height * margin
    )

    val scaleX = this.layout.bounds.width / tmpBoundsOnModel.width
    val scaleY = this.layout.bounds.height / tmpBoundsOnModel.height

    this.copy(
      matrixForMask = calcMaskMatrix(this.layout.bounds, tmpBoundsOnModel, scaleX, scaleY),
      matrixForDraw = calcDrawMatrix(this.layout.bounds, tmpBoundsOnModel, scaleX, scaleY)
    )
  }

  private def calcDrawMatrix(layoutBoundsOnTex01: Rectangle, tmpBoundsOnModel: Rectangle,
                             scaleX: Float, scaleY: Float): GeneralMatrix = {
    new GeneralMatrix()
      .translateRelative(layoutBoundsOnTex01.leftX, layoutBoundsOnTex01.bottomY)
      .scaleRelative(scaleX, scaleY)
      .translateRelative(-tmpBoundsOnModel.leftX, -tmpBoundsOnModel.bottomY)
  }

  private def calcMaskMatrix(layoutBoundsOnTex01: Rectangle, tmpBoundsOnModel: Rectangle,
                             scaleX: Float, scaleY: Float): GeneralMatrix = {

    new GeneralMatrix()
      .translateRelative(-1.0f, -1.0f)
      .scaleRelative(2.0f, 2.0f)
      .translateRelative(layoutBoundsOnTex01.leftX, layoutBoundsOnTex01.bottomY)
      .scaleRelative(scaleX, scaleY)
      .translateRelative(-tmpBoundsOnModel.leftX, -tmpBoundsOnModel.bottomY)
  }

  def calculateAllClippedDrawableBounds(): ClippingContext = {
    val positions = clippedDrawables.flatMap(_.vertexInfo.positions)

    if (positions.isEmpty) {
      this.copy(allClippedDrawableBounds = None)
    } else {
      val xPositions = positions.map(_._1)
      val yPositions = positions.map(_._2)

      val clippedDrawTotalMinX = xPositions.min
      val clippedDrawTotalMinY = yPositions.min
      val clippedDrawTotalMaxX = xPositions.filter(_ > 0).maxOption.getOrElse(0.0f)
      val clippedDrawTotalMaxY = yPositions.filter(_ > 0).maxOption.getOrElse(0.0f)

      val clippedRectangle = Rectangle(
        clippedDrawTotalMinX,
        clippedDrawTotalMinY,
        width = clippedDrawTotalMaxX - clippedDrawTotalMinX,
        height = clippedDrawTotalMaxY - clippedDrawTotalMinY
      )

      this.copy(allClippedDrawableBounds = Some(clippedRectangle))
    }
  }

  def vertexPositionChangedMaskDrawable: List[Drawable] = maskDrawable.filter(_.dynamicFlags.vertexPositionChanged)

}
