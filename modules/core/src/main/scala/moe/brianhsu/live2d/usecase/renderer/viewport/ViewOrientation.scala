package moe.brianhsu.live2d.usecase.renderer.viewport

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader

sealed trait ViewOrientation

object ViewOrientation {
  case object Horizontal extends ViewOrientation
  case object Vertical extends ViewOrientation

  def apply(drawCanvasInfoReader: DrawCanvasInfoReader): ViewOrientation = {
    if (drawCanvasInfoReader.currentCanvasWidth >= drawCanvasInfoReader.currentCanvasHeight) {
      Horizontal
    } else {
      Vertical
    }
  }
}