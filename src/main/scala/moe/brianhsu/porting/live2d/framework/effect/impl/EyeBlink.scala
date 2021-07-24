package moe.brianhsu.porting.live2d.framework.effect.impl

import EyeBlink._
import moe.brianhsu.porting.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.porting.live2d.framework.effect.Effect
import moe.brianhsu.porting.live2d.framework.model.Live2DModel

import scala.util.Random

object EyeBlink {
  sealed trait State
  case object Init extends State        ///< 初期状態
  case object Interval extends State         ///< まばたきしていない状態
  case object Closing extends State          ///< まぶたが閉じていく途中の状態
  case object Closed extends State           ///< まぶたが閉じている状態
  case object Opening extends State           ///< まぶたが開いていく途中の状態
}

class EyeBlink (avatarSettings: Settings,
                blinkingIntervalSeconds: Float = 4.0f,
                closingSeconds: Float = 0.1f,
                closedSeconds: Float = 0.05f,
                openingSeconds: Float = 0.15f) extends Effect {

  var blinkingState: State = EyeBlink.Init        ///< 現在の状態
  var nextBlinkingTime: Float = 0.0f           ///< 次のまばたきの時刻[秒]
  var stateStartTimeSeconds: Float = 0.0f      ///< 現在の状態が開始した時刻[秒]
  var userTimeSeconds: Float = 0.0f            ///< デルタ時間の積算値[秒]

  private def determineNextBlinkingTiming(): Float = {
    val r = Random.nextFloat()
    userTimeSeconds + (r * (2.0f * blinkingIntervalSeconds - 1.0f))
  }

  override def updateParameters(model: Live2DModel, deltaTimeSeconds: Float): Unit = {
    userTimeSeconds += deltaTimeSeconds

    var parameterValue: Float = 0
    var t: Float = 0.0f

    blinkingState match {
      case Closing =>
        t = (userTimeSeconds - stateStartTimeSeconds) / closingSeconds

        if (t >= 1.0f) {
          t = 1.0f
          blinkingState = Closed
          stateStartTimeSeconds = userTimeSeconds
        }

        parameterValue = 1.0f - t

      case Closed =>
        t = (userTimeSeconds - stateStartTimeSeconds) / closedSeconds

        if (t >= 1.0f) {
          blinkingState = Opening
          stateStartTimeSeconds = userTimeSeconds
        }

        parameterValue = 0.0f

      case Opening =>
        t = (userTimeSeconds - stateStartTimeSeconds) / openingSeconds

        if (t >= 1.0f) {
          t = 1.0f
          blinkingState = Interval
          nextBlinkingTime = determineNextBlinkingTiming()
        }

        parameterValue = t

      case Interval =>
        if (nextBlinkingTime < userTimeSeconds) {
          blinkingState = Closing
          stateStartTimeSeconds = userTimeSeconds
        }

        parameterValue = 1.0f

      case Init =>
        blinkingState = Interval
        nextBlinkingTime = determineNextBlinkingTiming()

        parameterValue = 1.0f
    }

    avatarSettings.eyeBlinkParameterIds.foreach { id =>
      model.setParameterValue(id, parameterValue)
    }
  }

}
