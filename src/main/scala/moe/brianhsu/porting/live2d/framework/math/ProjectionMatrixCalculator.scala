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
                          onUpdated: ViewOrientation => Unit,
                          forceUpdate: Boolean = false): GeneralMatrix  = {

    val viewOrientation = if (canvasInfo.width > 1.0 && windowWidth < windowHeight) Vertical else Horizontal

    val shouldUpdate =
      currentProjection.isEmpty ||
        !previousViewMatrix.contains(viewMatrix) ||
        previousWindowWidth != windowWidth ||
        previousWindowHeight != windowHeight ||
        forceUpdate

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
