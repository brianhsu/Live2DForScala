package moe.brianhsu.live2d.usecase.renderer.viewport

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewPortMatrixCalculator.{ViewLogicalLeft, ViewLogicalMaxBottom, ViewLogicalMaxLeft, ViewLogicalMaxRight, ViewLogicalMaxTop, ViewLogicalRight, ViewMaxScale, ViewMinScale, ViewScale}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ViewPortMatrix

object ViewPortMatrixCalculator {
  private val ViewScale = 1.0f
  private val ViewMaxScale = 2.0f
  private val ViewMinScale = 0.8f
  private val ViewLogicalLeft = -1.0f
  private val ViewLogicalRight = 1.0f
  private val ViewLogicalMaxLeft = -2.0f
  private val ViewLogicalMaxRight = 2.0f
  private val ViewLogicalMaxTop = -2.0f
  private val ViewLogicalMaxBottom = 2.0f
}

class ViewPortMatrixCalculator {
  private var mDeviceToScreen: GeneralMatrix = new GeneralMatrix
  private var mViewPortMatrix: ViewPortMatrix = new ViewPortMatrix(Rectangle(), Rectangle(), 0, 0)
  private var drawCanvasWidth: Int = 0
  private var drawCanvasHeight: Int = 0

  def deviceToScreen: GeneralMatrix = mDeviceToScreen
  def viewPortMatrix: ViewPortMatrix = mViewPortMatrix

  def updateViewPort(width: Int, height: Int): Unit = {
    this.drawCanvasWidth = width
    this.drawCanvasHeight = height

    if (drawCanvasWidth != 0 && drawCanvasHeight != 0) {
      val ratio = drawCanvasWidth.toFloat / drawCanvasHeight.toFloat
      val left = -ratio
      val right = ratio
      val top = ViewLogicalLeft
      val bottom = ViewLogicalRight

      this.mViewPortMatrix = calculateViewMatrix(left, right, top, bottom)
      this.mDeviceToScreen = calculateDeviceToScreen(left, right, top, bottom)
    }

  }

  private def calculateViewMatrix(left: Float, right: Float, top: Float, bottom: Float): ViewPortMatrix = {
    new ViewPortMatrix(
      Rectangle(left, right, right - left, bottom - top),
      Rectangle(
        ViewLogicalMaxLeft,
        ViewLogicalMaxRight,
        ViewLogicalMaxRight - ViewLogicalMaxLeft,
        ViewLogicalMaxBottom - ViewLogicalMaxTop
      ),
      ViewMinScale,
      ViewMaxScale
    ).scale(ViewScale, ViewScale)
  }

  private def calculateDeviceToScreen(left: Float, right: Float, top: Float, bottom: Float): GeneralMatrix = {
    val (scaleRelativeX, scaleRelativeY) = if (drawCanvasWidth > drawCanvasHeight) {
      val screenW: Float = (right - left).abs
      (screenW / drawCanvasWidth, -screenW / drawCanvasWidth)
    } else {
      val screenH: Float = (bottom - top).abs
      (screenH / drawCanvasHeight, -screenH / drawCanvasHeight)
    }

    (new GeneralMatrix)
      .scaleRelative(scaleRelativeX, scaleRelativeY)
      .translateRelative(-drawCanvasWidth * 0.5f, -drawCanvasHeight * 0.5f)
  }

}
