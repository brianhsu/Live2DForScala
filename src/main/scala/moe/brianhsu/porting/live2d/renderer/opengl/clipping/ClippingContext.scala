package moe.brianhsu.porting.live2d.renderer.opengl.clipping

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureColor
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingContext.Layout

object ClippingContext {
  private val ChannelColors = Map(
    0 -> TextureColor(1.0f, 0.0f, 0.0f, 0.0f),
    1 -> TextureColor(0.0f, 1.0f, 0.0f, 0.0f),
    2 -> TextureColor(0.0f, 0.0f, 1.0f, 0.0f),
    3 -> TextureColor(0.0f, 0.0f, 0.0f),
  )

  val ColorChannelCount: Int = ChannelColors.size

  case class Layout(channel: Int, bounds: Rectangle) {
    def channelColor: TextureColor = ChannelColors(channel)
  }
}

case class ClippingContext(maskDrawable: List[Drawable], clippedDrawables: List[Drawable],
                           allClippedDrawRect: Option[Rectangle] = None,
                           matrixForMask: GeneralMatrix = new GeneralMatrix,
                           matrixForDraw: GeneralMatrix = new GeneralMatrix,
                           layout: Layout = Layout(0, Rectangle())) {

  def isUsing: Boolean = allClippedDrawRect.isDefined

  def calculateMatrix(): ClippingContext = {
    val allClippedDrawRect = this.allClippedDrawRect.getOrElse(Rectangle())
    val layoutBoundsOnTex01 = this.layout.bounds

    val margin = 0.05f
    val tmpBoundsOnModel = allClippedDrawRect.expand(
      allClippedDrawRect.width * margin,
      allClippedDrawRect.height * margin
    )

    val scaleX = layoutBoundsOnTex01.width / tmpBoundsOnModel.width
    val scaleY = layoutBoundsOnTex01.height / tmpBoundsOnModel.height

    this.copy(matrixForMask = calcMaskMatrix(layoutBoundsOnTex01, tmpBoundsOnModel, scaleX, scaleY), matrixForDraw = calcDrawMatrix(layoutBoundsOnTex01, tmpBoundsOnModel, scaleX, scaleY))
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

  def calculateClippedDrawTotalBounds(): ClippingContext = {
    val FLT_MAX = Float.MaxValue
    val FLT_MIN = 0.0f

    val positions = clippedDrawables.flatMap(_.vertexInfo.positions)
    val xPositions = positions.map(_._1)
    val yPositions = positions.map(_._2)

    val clippedDrawTotalMinX = xPositions.minOption.getOrElse(FLT_MAX)
    val clippedDrawTotalMinY = yPositions.minOption.getOrElse(FLT_MAX)
    val clippedDrawTotalMaxX = xPositions.filter(_ > 0).maxOption.getOrElse(FLT_MIN)
    val clippedDrawTotalMaxY = yPositions.filter(_ > 0).maxOption.getOrElse(FLT_MIN)

    if (clippedDrawTotalMinX == FLT_MAX) {
      this.copy(allClippedDrawRect = None)
    } else {
      val clippedRectangle = Rectangle(
        clippedDrawTotalMinX,
        clippedDrawTotalMinY,
        width = clippedDrawTotalMaxX - clippedDrawTotalMinX,
        height = clippedDrawTotalMaxY - clippedDrawTotalMinY
      )
      this.copy(allClippedDrawRect = Some(clippedRectangle))
    }
  }

}
