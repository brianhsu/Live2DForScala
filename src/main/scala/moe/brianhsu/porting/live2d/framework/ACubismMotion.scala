package moe.brianhsu.porting.live2d.framework

import ACubismMotion.FinishedMotionCallback
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.annotation.unused

object ACubismMotion {
  type FinishedMotionCallback = ACubismMotion => Unit
}

abstract class ACubismMotion {
  protected var _fadeInSeconds: Float = -1.0f        ///< フェードインにかかる時間[秒]
  protected var _fadeOutSeconds: Float = -1.0f       ///< フェードアウトにかかる時間[秒]
  protected var _weight: Float = 1.0f             ///< モーションの重み
  protected var _firedEventValues: List[String] = Nil

  // モーション再生終了コールバック関数
  protected var _onFinishedMotion: FinishedMotionCallback = null

  /**
   * モデルのパラメータ更新
   *
   * モデルのパラメータを更新する。
   *
   * @param   model               対象のモデル
   * @param   motionQueueEntry    CubismMotionQueueManagerで管理されているモーション
   * @param   userTimeSeconds     デルタ時間の積算値[秒]
   */
  def UpdateParameters(model: Live2DModel, motionQueueEntry: CubismMotionQueueEntry, userTimeSeconds: Float): Unit = {

    if (motionQueueEntry.IsFinished()) {
      println("Motion finished.")
      return
    }

    if (!motionQueueEntry.IsStarted()) {
      motionQueueEntry.IsStarted(true)
      motionQueueEntry.SetStartTime(userTimeSeconds) //モーションの開始時刻を記録
      motionQueueEntry.SetFadeInStartTime(userTimeSeconds) //フェードインの開始時刻

      val duration = GetDuration()

      if (motionQueueEntry.GetEndTime() < 0) {
        //開始していないうちに終了設定している場合がある。
        motionQueueEntry.SetEndTime( if (duration <= 0)  -1 else motionQueueEntry.GetStartTime() + duration)
        //duration == -1 の場合はループする
      }
    }

    var fadeWeight: Float = _weight //現在の値と掛け合わせる割合

    //---- フェードイン・アウトの処理 ----
    //単純なサイン関数でイージングする
    val fadeIn: Float = if (_fadeInSeconds == 0.0f) {
      1.0f
    } else {
      Easing.sine((userTimeSeconds - motionQueueEntry.GetFadeInStartTime()) / _fadeInSeconds)
    }

    val fadeOut = if (_fadeOutSeconds == 0.0f || motionQueueEntry.GetEndTime() < 0.0f) {
      1.0f
    } else {
      Easing.sine((motionQueueEntry.GetEndTime() - userTimeSeconds) / _fadeOutSeconds)
    }

    fadeWeight = fadeWeight * fadeIn * fadeOut

    assert(fadeWeight >= 0.0f && fadeWeight <= 1.0f, "fadeWeight is invalid")

    //---- 全てのパラメータIDをループする ----
    DoUpdateParameters(model, userTimeSeconds, fadeWeight, motionQueueEntry)

    //後処理
    //終了時刻を過ぎたら終了フラグを立てる（CubismMotionQueueManager）
    if ((motionQueueEntry.GetEndTime() > 0) && (motionQueueEntry.GetEndTime() < userTimeSeconds)) {
      println("Motion ended.")
      motionQueueEntry.IsFinished(true) //終了
    }
  }

  /**
   * フェードイン
   *
   * フェードインの時間を設定する。
   *
   * @param   fadeInSeconds      フェードインにかかる時間[秒]
   */
  def SetFadeInTime(fadeInSeconds: Float): Unit = {
    this._fadeInSeconds = fadeInSeconds
  }

  /**
   * フェードアウト
   *
   * フェードアウトの時間を設定する。
   *
   * @param   fadeOutSeconds     フェードアウトにかかる時間[秒]
   */
  def SetFadeOutTime(fadeOutSeconds: Float): Unit = {
    this._fadeOutSeconds = fadeOutSeconds
  }

  /**
   * フェードアウトにかかる時間の取得
   *
   * フェードアウトにかかる時間を取得する。
   *
   * @return フェードアウトにかかる時間[秒]
   */
  def GetFadeOutTime(): Float = this._fadeOutSeconds

  /**
   * フェードインにかかる時間の取得
   *
   * フェードインにかかる時間を取得する。
   *
   * @return フェードインにかかる時間[秒]
   */
  def GetFadeInTime(): Float = this._fadeInSeconds

  /**
   * モーション適用の重みの設定
   *
   * モーション適用の重みを設定する。
   *
   * @param   weight      重み(0.0 - 1.0)
   */
  def SetWeight(weight: Float): Unit = {
    this._weight = weight
  }

  /**
   * モーション適用の重みの取得
   *
   * モーション適用の重みを取得する。
   *
   * @return 重み(0.0 - 1.0)
   */
  def GetWeight(): Float = this._weight

  /**
   * モーションの長さの取得
   *
   * モーションの長さを取得する。
   *
   * @return モーションの長さ[秒]
   *
   * @note ループのときは「-1」。
   *       ループではない場合は、オーバーライドする。
   *       正の値の時は取得される時間で終了する。
   *       「-1」のときは外部から停止命令が無い限り終わらない処理となる。
   */
  def GetDuration(): Float = -1.0f

  /**
   * モーションのループ1回分の長さの取得
   *
   * モーションのループ1回分の長さを取得する。
   *
   * @return モーションのループ1回分の長さ[秒]
   *
   * @note ループしない場合は GetDuration()と同じ値を返す。
   *       ループ一回分の長さが定義できない場合（プログラム的に動き続けるサブクラスなど）の場合は「-1」を返す
   */
  def GetLoopDuration(): Float = -1.0f

  /**
   * モデルのパラメータ更新
   *
   * イベント発火のチェック。
   * 入力する時間は呼ばれるモーションタイミングを０とした秒数で行う。
   *
   * @param   beforeCheckTimeSeconds   前回のイベントチェック時間[秒]
   * @param   motionTimeSeconds        今回の再生時間[秒]
   */
  def GetFiredEvent(@unused beforeCheckTimeSeconds: Float, @unused motionTimeSeconds: Float): List[String] = _firedEventValues


  /**
   * モーション再生終了コールバックの登録
   *
   * モーション再生終了コールバックを登録する。
   * IsFinishedフラグを設定するタイミングで呼び出される。
   * 以下の状態の際には呼び出されない:
   *   1. 再生中のモーションが「ループ」として設定されているとき
   *   2. コールバックにNULLが登録されているとき
   *
   * @param   onFinishedMotionHandler     モーション再生終了コールバック関数
   */
  def SetFinishedMotionHandler(onFinishedMotionHandler: FinishedMotionCallback): Unit = {
    this._onFinishedMotion = onFinishedMotionHandler
  }

  /**
   * モーション再生終了コールバックの取得
   *
   * モーション再生終了コールバックを取得する。
   *
   * @return  登録されているモーション再生終了コールバック関数。NULLのとき、関数は何も登録されていない。
   */
  def GetFinishedMotionHandler(): FinishedMotionCallback = this._onFinishedMotion

  protected def DoUpdateParameters(model: Live2DModel, userTimeSeconds: Float, weight: Float,
                                   motionQueueEntry: CubismMotionQueueEntry): Unit

}
