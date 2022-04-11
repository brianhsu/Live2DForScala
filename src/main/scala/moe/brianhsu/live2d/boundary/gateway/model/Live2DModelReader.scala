package moe.brianhsu.live2d.boundary.gateway.model

import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

/**
 * Interface for loading Live2DModel
 */
trait Live2DModelReader {
  /**
   * Load a Live2DModel instance
   *
   * @return A Success[Live2DModel] if the model is created and passed validation,
   *         otherwise a Failure[Throwable] to indicate error.
   */
  def loadModel(): Try[Live2DModel]
}
