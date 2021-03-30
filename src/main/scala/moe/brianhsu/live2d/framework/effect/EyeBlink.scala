package moe.brianhsu.live2d.framework.effect

import moe.brianhsu.live2d.framework.effect.EyeBlink._
import moe.brianhsu.live2d.framework.model.Live2DModel

import scala.util.Random


object EyeBlink {
  sealed trait State
  case object Init extends State        ///< 初期状態
  case object Interval extends State         ///< まばたきしていない状態
  case object Closing extends State          ///< まぶたが閉じていく途中の状態
  case object Closed extends State           ///< まぶたが閉じている状態
  case object Opening extends State           ///< まぶたが開いていく途中の状態
}

class EyeBlink(parameterIds: List[String],
               _blinkingIntervalSeconds: Float = 4.0f, _closingSeconds: Float = 0.1f,
               _closedSeconds: Float = 0.05f, _openingSeconds: Float = 0.15f) {

  var _blinkingState: State = EyeBlink.Init        ///< 現在の状態
  var _nextBlinkingTime: Float = 0.0f           ///< 次のまばたきの時刻[秒]
  var _stateStartTimeSeconds: Float = 0.0f      ///< 現在の状態が開始した時刻[秒]
  var _userTimeSeconds: Float = 0.0f            ///< デルタ時間の積算値[秒]

  def determineNextBlinkingTiming(): Float = {
    val r = Random.nextFloat()
    _userTimeSeconds + (r * (2.0f * _blinkingIntervalSeconds - 1.0f))
  }

  def updateParameters(model: Live2DModel, deltaTimeSeconds: Float): Unit = {
    _userTimeSeconds += deltaTimeSeconds

    var parameterValue: Float = 0
    var t: Float = 0.0f

    _blinkingState match {
      case Closing =>
        t = (_userTimeSeconds - _stateStartTimeSeconds) / _closingSeconds

        if (t >= 1.0f) {
           t = 1.0f
          _blinkingState = Closed
          _stateStartTimeSeconds = _userTimeSeconds
        }

        parameterValue = 1.0f - t

      case Closed =>
        t = (_userTimeSeconds - _stateStartTimeSeconds) / _closedSeconds

        if (t >= 1.0f) {
          _blinkingState = Opening
          _stateStartTimeSeconds = _userTimeSeconds
        }

        parameterValue = 0.0f

      case Opening =>
        t = (_userTimeSeconds - _stateStartTimeSeconds) / _openingSeconds

        if (t >= 1.0f) {
          t = 1.0f
          _blinkingState = Interval
          _nextBlinkingTime = determineNextBlinkingTiming()
        }

        parameterValue = t

      case Interval =>
        if (_nextBlinkingTime < _userTimeSeconds) {
          _blinkingState = Closing
          _stateStartTimeSeconds = _userTimeSeconds
        }

        parameterValue = 1.0f

      case Init =>
        _blinkingState = Interval
        _nextBlinkingTime = determineNextBlinkingTiming()

        parameterValue = 1.0f
    }

    parameterIds.foreach { id =>
      model.setParameterValue(id, parameterValue)
    }
  }

}
