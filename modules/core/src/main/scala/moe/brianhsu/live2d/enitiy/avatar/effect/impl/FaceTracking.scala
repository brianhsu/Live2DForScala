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

abstract class FaceTracking(protected val trackingTaps: TrackingTaps) extends Effect {

  protected[impl] var trackingNoes: List[TrackingNode] = Nil

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {
    trackingNoes match {
      case Nil => Nil
      case _ => calculateOperations()
    }
  }

  private var lastFaceXAngle = 0.0f
  private var lastFaceYAngle = 0.0f
  private var lastFaceZAngle = 0.0f
  private var lastBodyXAngle = 0.0f

  private var isFirst = true

  private def calculateOperations(): List[UpdateOperation] = {
    val faceXAngle = average(trackingNoes.take(trackingTaps.faceXAngle).map(_.faceXAngle))
    val faceYAngle = average(trackingNoes.take(trackingTaps.faceYAngle).map(_.faceYAngle))
    val faceZAngle = average(trackingNoes.take(trackingTaps.faceZAngle).map(_.faceZAngle))
    val leftEyeOpenness = average(trackingNoes.take(trackingTaps.leftEyeOpenness).map(_.leftEyeOpenness))
    val rightEyeOpenness = average(trackingNoes.take(trackingTaps.rightEyeOpenness).map(_.rightEyeOpenness))
    val mouthOpenness = average(trackingNoes.take(trackingTaps.mouthOpenness).map(_.mouthOpenness))
    val mouthForm = average(trackingNoes.take(trackingTaps.mouthForm).map(_.mouthForm))
    val leftEyeSmile = average(trackingNoes.take(trackingTaps.leftEyeSmile).map(_.leftEyeSmile))
    val rightEyeSmile = average(trackingNoes.take(trackingTaps.rightEyeSmile).map(_.rightEyeSmile))

    if (isFirst) {
      this.lastFaceXAngle = faceXAngle
      this.lastFaceYAngle = faceYAngle
      this.lastFaceZAngle = faceZAngle
      this.lastBodyXAngle = faceXAngle
      this.isFirst = false
    }

    this.lastFaceXAngle = lastFaceXAngle + calculateNewDiff(faceXAngle - lastFaceXAngle)
    this.lastFaceYAngle = lastFaceYAngle + calculateNewDiff(faceYAngle - lastFaceYAngle)
    this.lastFaceZAngle = lastFaceZAngle + calculateNewDiff(faceZAngle - lastFaceZAngle)
    this.lastBodyXAngle = lastBodyXAngle + calculateNewDiff(faceXAngle - lastBodyXAngle, 0.85f)

    val result = List(
      ParameterValueUpdate("ParamAngleX", this.lastFaceXAngle),
      ParameterValueUpdate("ParamAngleY", this.lastFaceYAngle),
      ParameterValueUpdate("ParamAngleZ", this.lastFaceZAngle),
      ParameterValueUpdate("ParamBodyAngleX", this.lastBodyXAngle, 0.75f),
      ParameterValueUpdate("ParamEyeLOpen", leftEyeOpenness),
      ParameterValueUpdate("ParamEyeROpen", rightEyeOpenness),
      ParameterValueUpdate("ParamMouthOpenY", mouthOpenness),
      ParameterValueUpdate("ParamMouthForm", mouthForm),
      ParameterValueUpdate("ParamEyeLSmile", leftEyeSmile),
      ParameterValueUpdate("ParamEyeRSmile", rightEyeSmile),
    )


    result
  }

  private def calculateNewDiff(originalDiff: Float, maxDiff: Float = 2.65f): Float = {

    val afterLog = Math.log1p(originalDiff.abs).min(maxDiff).toFloat

    if (originalDiff > 0) {
      originalDiff.min(afterLog)
    } else if (originalDiff < 0){
      originalDiff.max(-afterLog)
    } else {
      originalDiff
    }
  }

  private def average(values: List[Float]): Float = values.sum / values.size
}
