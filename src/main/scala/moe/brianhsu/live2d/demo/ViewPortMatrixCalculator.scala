package moe.brianhsu.live2d.demo

import moe.brianhsu.live2d.math.{Rectangle, Matrix4x4, ModelMatrix, ViewMatrix}

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

  private val deviceToScreen: Matrix4x4 = new Matrix4x4
  private var viewMatrix: ViewMatrix = new ViewMatrix(Rectangle(0, 0, 0, 0), Rectangle(0, 0, 0, 0), 0, 0)
  private var surfaceWidth: Int = 0
  private var surfaceHeight: Int = 0

  def getViewMatrix: ViewMatrix = viewMatrix
  def getDeviceToScreen: Matrix4x4 = deviceToScreen

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

  private def updateViewMatrix(left: Float, right: Float, top: Float, bottom: Float) = {
    this.viewMatrix = new ViewMatrix(
      Rectangle(left, right, right - left, bottom - top),
      Rectangle(
        ViewLogicalMaxLeft, ViewLogicalMaxRight,
        ViewLogicalMaxRight - ViewLogicalMaxLeft,
        ViewLogicalMaxBottom - ViewLogicalMaxTop
      ),
      ViewMaxScale, ViewMinScale
    )

    this.viewMatrix.scale(ViewScale, ViewScale)
  }

  private def updateDeviceToScreen(left: Float, right: Float, top: Float, bottom: Float) = {
    deviceToScreen.loadIdentity()

    if (surfaceWidth > surfaceHeight) {
      val screenW: Float = (right - left).abs
      deviceToScreen.scaleRelative(screenW / surfaceWidth, -screenW / surfaceWidth)
    } else {
      val screenH: Float = (bottom - top).abs
      deviceToScreen.scaleRelative(screenH / surfaceHeight, -screenH / surfaceHeight)
    }
    deviceToScreen.translateRelative(-surfaceWidth * 0.5f, -surfaceHeight * 0.5f)
  }

  def getProjection(windowWidth: Int, windowHeight: Int,
                    modelCanvasWidth: Float, modelMatrix: ModelMatrix): Matrix4x4 = {
    val projection = new Matrix4x4

    if (modelCanvasWidth > 1.0 && windowWidth < windowHeight) {
      modelMatrix.setWidth(2.0f)
      projection.scale(1.0f, windowWidth.toFloat / windowHeight.toFloat)
    }

    projection.scale(windowHeight.toFloat / windowWidth.toFloat, 1.0f)
    projection.multiplyByMatrix(viewMatrix)
    projection
  }

}
