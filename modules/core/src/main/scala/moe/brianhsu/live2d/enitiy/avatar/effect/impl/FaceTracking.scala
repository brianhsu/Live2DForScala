package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.{TrackingNode, TrackingTaps}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueUpdate

object FaceTracking {

  case class TrackingTaps(
    faceXAngle: Int, faceYAngle: Int, faceZAngle: Int,
    leftEyeOpenness: Int, rightEyeOpenness: Int,
    mouthOpenness: Int, mouthForm: Int,
    leftEyeSmile: Int, rightEyeSmile: Int,
    maxTaps: Int
  )

  case class TrackingNode(
    faceXAngle: Float, faceYAngle: Float, faceZAngle: Float,
    leftEyeOpenness: Float, rightEyeOpenness: Float,
    mouthOpenness: Float, mouthForm: Float,
    leftEyeSmile: Float, rightEyeSmile: Float,
  )
}

class FaceTracking(protected val trackingTaps: TrackingTaps) extends Effect {

  protected[impl] var trackingNoes: List[TrackingNode] = Nil

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {
    trackingNoes match {
      case Nil => Nil
      case _ => calculateOperations()
    }
  }

  private def calculateOperations(): List[ParameterValueUpdate] = {
    val faceXAngle = average(trackingNoes.take(trackingTaps.faceXAngle).map(_.faceXAngle))
    val faceYAngle = average(trackingNoes.take(trackingTaps.faceYAngle).map(_.faceYAngle))
    val faceZAngle = average(trackingNoes.take(trackingTaps.faceZAngle).map(_.faceZAngle))
    val leftEyeOpenness = average(trackingNoes.take(trackingTaps.leftEyeOpenness).map(_.leftEyeOpenness))
    val rightEyeOpenness = average(trackingNoes.take(trackingTaps.rightEyeOpenness).map(_.rightEyeOpenness))
    val mouthOpenness = average(trackingNoes.take(trackingTaps.mouthOpenness).map(_.mouthOpenness))
    val mouthForm = average(trackingNoes.take(trackingTaps.mouthForm).map(_.mouthForm))
    val leftEyeSmile = average(trackingNoes.take(trackingTaps.leftEyeSmile).map(_.leftEyeSmile))
    val rightEyeSmile = average(trackingNoes.take(trackingTaps.rightEyeSmile).map(_.rightEyeSmile))

    List(
      ParameterValueUpdate("ParamAngleX", faceXAngle),
      ParameterValueUpdate("ParamAngleY", faceYAngle),
      ParameterValueUpdate("ParamAngleZ", faceZAngle),
      ParameterValueUpdate("ParamEyeLOpen", leftEyeOpenness),
      ParameterValueUpdate("ParamEyeROpen", rightEyeOpenness),
      ParameterValueUpdate("ParamMouthOpenY", mouthOpenness),
      ParameterValueUpdate("ParamMouthForm", mouthForm),
      ParameterValueUpdate("ParamEyeLSmile", leftEyeSmile),
      ParameterValueUpdate("ParamEyeRSmile", rightEyeSmile),
    )
  }

  private def average(values: List[Float]): Float = values.sum / values.size
}
