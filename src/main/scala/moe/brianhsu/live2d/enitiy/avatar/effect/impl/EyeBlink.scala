package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueUpdate

import scala.util.Random

object EyeBlink {
  sealed trait State
  case object Init extends State
  case object Interval extends State
  case object Closing extends State
  case object Closed extends State
  case object Opening extends State

  case class Parameters(
    blinkingIntervalInSeconds: Float, blinkingIntervalRandomness: () => Float,
    closingInSeconds: Float,
    closedInSeconds: Float, openingInSeconds: Float
  )

}

class EyeBlink (avatarSettings: Settings,
                parameters: EyeBlink.Parameters = EyeBlink.Parameters(4.0f, () => Random.nextFloat(), 0.1f, 0.05f, 0.15f)) extends Effect {

  private var currentBlinkingState: EyeBlink.State = EyeBlink.Init
  private var nextBlinkingTimeInSeconds: Float = 0.0f
  private var currentStateStartTimeInSeconds: Float = 0.0f

  private def determineNextBlinkingTiming(currentTimeInSeconds: Float): Float = {
    val nextBlinkingAfterSeconds = 2.0f * parameters.blinkingIntervalInSeconds - 1.0f
    currentTimeInSeconds + parameters.blinkingIntervalRandomness() * nextBlinkingAfterSeconds
  }

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {

    /*
     * This value indicate the openness of the eye of a Live 2D avatar.
     *  - 0.0f means totally closed
     *  - 1.0f means totally opened
     */
    val eyeOpenness = currentBlinkingState match {
      case EyeBlink.Closing => closingEye(totalElapsedTimeInSeconds)
      case EyeBlink.Closed => closedEye(totalElapsedTimeInSeconds)
      case EyeBlink.Opening => openingEye(totalElapsedTimeInSeconds)
      case EyeBlink.Interval => openedEyeAndWaitNextClosing(totalElapsedTimeInSeconds)
      case EyeBlink.Init => openedEyeAndPrepareToBlink(totalElapsedTimeInSeconds)
    }

    avatarSettings.eyeBlinkParameterIds.map { id =>
      ParameterValueUpdate(id, eyeOpenness)
    }
  }

  private def closedEye(currentTimeInSeconds: Float) = {
    val portion: Float = (currentTimeInSeconds - currentStateStartTimeInSeconds) / parameters.closedInSeconds

    if (portion >= 1.0f) {
      currentBlinkingState = EyeBlink.Opening
      currentStateStartTimeInSeconds = currentTimeInSeconds
    }

    0.0f
  }

  private def openedEyeAndPrepareToBlink(currentTimeInSeconds: Float): Float = {
    currentBlinkingState = EyeBlink.Interval
    nextBlinkingTimeInSeconds = determineNextBlinkingTiming(currentTimeInSeconds)
    1.0f
  }

  private def closingEye(currentTimeInSeconds: Float): Float = {
    var portion: Float = (currentTimeInSeconds - currentStateStartTimeInSeconds) / parameters.closingInSeconds

    if (portion >= 1.0f) {
      portion = 1.0f
      currentBlinkingState = EyeBlink.Closed
      currentStateStartTimeInSeconds = currentTimeInSeconds
    }

    1.0f - portion
  }

  private def openingEye(currentTimeInSeconds: Float): Float = {
    var portion: Float = (currentTimeInSeconds - currentStateStartTimeInSeconds) / parameters.openingInSeconds

    if (portion >= 1.0f) {
      portion = 1.0f
      currentBlinkingState = EyeBlink.Interval
      nextBlinkingTimeInSeconds = determineNextBlinkingTiming(currentTimeInSeconds)
    }

    portion
  }
  private def openedEyeAndWaitNextClosing(currentTimeInSeconds: Float): Float = {
    if (nextBlinkingTimeInSeconds < currentTimeInSeconds) {
      currentBlinkingState = EyeBlink.Closing
      currentStateStartTimeInSeconds = currentTimeInSeconds
    }

    1.0f
  }

}
