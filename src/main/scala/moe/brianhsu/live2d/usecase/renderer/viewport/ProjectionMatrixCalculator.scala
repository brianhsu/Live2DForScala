package moe.brianhsu.live2d.usecase.renderer.viewport

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewOrientation.{Horizontal, Vertical}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.{ProjectionMatrix, ViewPortMatrix}

class ProjectionMatrixCalculator(drawCanvasInfoReader: DrawCanvasInfoReader) {

  private var previousDrawCanvasWidthHolder: Option[Int] = None
  private var previousDrawCanvasHeightHolder: Option[Int] = None
  private var previousViewPortMatrixHolder: Option[ViewPortMatrix] = None
  private var currentProjectionHolder: Option[ProjectionMatrix] = None

  def calculate(viewPortMatrix: ViewPortMatrix, isForceUpdate: Boolean = false, onUpdated: ViewOrientation => Unit): ProjectionMatrix  = {

    println(viewPortMatrix)
    val drawCanvasWidth = drawCanvasInfoReader.currentCanvasWidth
    val drawCanvasHeight = drawCanvasInfoReader.currentCanvasHeight

    val shouldUpdate =
      currentProjectionHolder.isEmpty ||
        !previousViewPortMatrixHolder.contains(viewPortMatrix) ||
        !previousDrawCanvasWidthHolder.contains(drawCanvasWidth) ||
        !previousDrawCanvasHeightHolder.contains(drawCanvasHeight) ||
        isForceUpdate

    if (shouldUpdate) {
      val viewOrientation = ViewOrientation(drawCanvasInfoReader)
      val projection = viewOrientation match {
        case Horizontal => new GeneralMatrix
        case Vertical => (new GeneralMatrix).scale(1.0f, drawCanvasWidth.toFloat / drawCanvasWidth.toFloat)
      }

      val xScalar = drawCanvasHeight.toFloat / drawCanvasWidth.toFloat
      val yScalar = 1.0f

      this.currentProjectionHolder = Some(viewPortMatrix * projection.scale(xScalar, yScalar))
      this.previousDrawCanvasWidthHolder = Some(drawCanvasWidth)
      this.previousDrawCanvasHeightHolder = Some(drawCanvasHeight)
      this.previousViewPortMatrixHolder = Some(viewPortMatrix)

      onUpdated(viewOrientation)
    }

    this.currentProjectionHolder.get
  }

}
