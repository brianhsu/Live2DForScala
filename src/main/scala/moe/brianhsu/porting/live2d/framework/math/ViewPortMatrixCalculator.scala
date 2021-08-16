package moe.brianhsu.porting.live2d.framework.math

import moe.brianhsu.live2d.enitiy.math.matrix.{GeneralMatrix, ViewMatrix}
import moe.brianhsu.live2d.enitiy.model.CanvasInfo
import moe.brianhsu.porting.live2d.framework.math.ProjectionMatrixCalculator.{Horizontal, Vertical, ViewOrientation}

object ProjectionMatrixCalculator {
  sealed trait ViewOrientation
  case object Horizontal extends ViewOrientation
  case object Vertical extends ViewOrientation
}

class ProjectionMatrixCalculator {
  private var previousWindowWidth = 0
  private var previousWindowHeight = 0
  private var previousViewMatrix: Option[ViewMatrix] = None
  private var currentProjection: Option[GeneralMatrix] = None

  def calculateProjection(canvasInfo: CanvasInfo, windowWidth: Int,
                          windowHeight: Int, viewMatrix: ViewMatrix,
                          onUpdated: ViewOrientation => Unit): GeneralMatrix  = {

    val viewOrientation = if (canvasInfo.width > 1.0 && windowWidth < windowHeight) Vertical else Horizontal

    val shouldUpdate =
      currentProjection.isEmpty ||
        !previousViewMatrix.contains(viewMatrix) ||
        previousWindowWidth != windowWidth ||
        previousWindowHeight != windowHeight

    if (shouldUpdate) {
      val projection = if (viewOrientation == Horizontal) {
        new GeneralMatrix
      } else {
        (new GeneralMatrix).scale(1.0f, windowWidth.toFloat / windowHeight.toFloat)
      }

      this.currentProjection = Some(
        viewMatrix * projection.scale(windowHeight.toFloat / windowWidth.toFloat, 1.0f)
      )

      this.previousWindowHeight = windowHeight
      this.previousWindowWidth = windowWidth
      this.previousViewMatrix = Some(viewMatrix)
      onUpdated(viewOrientation)
    }

    this.currentProjection.get
  }

}
class ViewPortMatrixCalculator {

  private val ViewScale = 1.0f
  private val ViewMaxScale = 2.0f
  private val ViewMinScale = 0.8f
  private val ViewLogicalLeft = -1.0f
  private val ViewLogicalRight = 1.0f
  private val ViewLogicalMaxLeft = -2.0f
  private val ViewLogicalMaxRight = 2.0f
  private val ViewLogicalMaxTop = -2.0f
  private val ViewLogicalMaxBottom = 2.0f

  private var deviceToScreen: GeneralMatrix = new GeneralMatrix
  private var viewMatrix: ViewMatrix = new ViewMatrix(Rectangle(), Rectangle(), 0, 0)
  private var surfaceWidth: Int = 0
  private var surfaceHeight: Int = 0

  def getDeviceToScreen: GeneralMatrix = deviceToScreen
  def getViewMatrix: ViewMatrix = viewMatrix

  def updateViewPort(width: Int, height: Int): Unit = {
    this.surfaceWidth = width
    this.surfaceHeight = height

    if ((surfaceWidth != 0) && (surfaceHeight != 0)) {
      val ratio = surfaceWidth.toFloat / surfaceHeight.toFloat
      val left = -ratio
      val right = ratio
      val top = ViewLogicalLeft
      val bottom = ViewLogicalRight

      updateDeviceToScreen(left, right, top, bottom)
      updateViewMatrix(left, right, top, bottom)
    }

  }

  private def updateViewMatrix(left: Float, right: Float, top: Float, bottom: Float): Unit = {
    this.viewMatrix = new ViewMatrix(Rectangle(left, right, right - left, bottom - top), Rectangle(
            ViewLogicalMaxLeft, ViewLogicalMaxRight,
            ViewLogicalMaxRight - ViewLogicalMaxLeft,
            ViewLogicalMaxBottom - ViewLogicalMaxTop
          ), ViewMinScale, ViewMaxScale).scale(ViewScale, ViewScale)
  }

  def updateDeviceToScreen(left: Float, right: Float, top: Float, bottom: Float): Unit = {
    val (scaleRelativeX, scaleRelativeY) = if (surfaceWidth > surfaceHeight) {
      val screenW: Float = (right - left).abs
      (screenW / surfaceWidth, -screenW / surfaceWidth)
    } else {
      val screenH: Float = (bottom - top).abs
      (screenH / surfaceHeight, -screenH / surfaceHeight)
    }

    deviceToScreen =
      (new GeneralMatrix)
        .scaleRelative(scaleRelativeX, scaleRelativeY)
        .translateRelative(-surfaceWidth * 0.5f, -surfaceHeight * 0.5f)
  }

}
