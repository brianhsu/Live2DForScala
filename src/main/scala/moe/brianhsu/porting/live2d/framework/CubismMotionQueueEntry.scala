package moe.brianhsu.porting.live2d.framework

class CubismMotionQueueEntry {
  private[framework] var _autoDelete: Boolean = false                   ///< 自動削除
  private[framework] var _motion: ACubismMotion = null                        ///< モーション

  private var _available: Boolean = true                    ///< 有効化フラグ
  private var _finished: Boolean = false                     ///< 終了フラグ
  private var _started: Boolean = false                      ///< 開始フラグ（0.9.00以降）
  private var _startTimeSeconds: Float = -1.0f             ///<  モーション再生開始時刻[秒]
  private var _fadeInStartTimeSeconds: Float = 0.0f      ///<  フェードイン開始時刻（ループの時は初回のみ）[秒]
  private var _endTimeSeconds: Float = -1.0f               ///< 終了予定時刻[秒]
  private var _stateTimeSeconds: Float = 0.0f             ///<  時刻の状態[秒]
  private var _stateWeight: Float = 0.0f                  ///<  重みの状態
  private var _lastEventCheckSeconds: Float = 0.0f         ///<   最終のMotion側のチェックした時間
  private var _fadeOutSeconds: Float = 0.0f
  private var _IsTriggeredFadeOut: Boolean = false

  /**
   *  フェードアウト開始の設定
   *
   * フェードアウトの開始を設定する。
   *
   * @param   fadeOutSeconds     フェードアウトにかかる時間[秒]
   */
  def SetFadeout(fadeOutSeconds: Float): Unit = {
    _fadeOutSeconds = fadeOutSeconds
    _IsTriggeredFadeOut = true
  }

  /**
   *  フェードアウトの開始
   *
   * フェードアウトを開始する。
   *
   * @param   fadeOutSeconds     フェードアウトにかかる時間[秒]
   * @param   userTimeSeconds    デルタ時間の積算値[秒]
   */
  def StartFadeout(fadeOutSeconds: Float, userTimeSeconds: Float): Unit = {
    val newEndTimeSeconds = userTimeSeconds + fadeOutSeconds
    _IsTriggeredFadeOut = true

    if (_endTimeSeconds < 0.0f || newEndTimeSeconds < _endTimeSeconds) {
      _endTimeSeconds = newEndTimeSeconds
    }
  }

  /**
   *  モーションの終了の確認
   *
   * モーションが終了したかどうか。
   *
   * @return  true   モーションが終了した false 終了していない
   */
  def IsFinished(): Boolean = _finished

  /**
   *  モーションの開始の確認
   *
   * モーションが開始したかどうか。
   *
   * @return  true モーションが開始した false   終了していない
   */
  def IsStarted(): Boolean = _started

  /**
   *  モーションの開始時刻の取得
   *
   * モーションの開始時刻を取得する。
   *
   * @return  モーションの開始時刻[秒]
   */
  def GetStartTime(): Float = _startTimeSeconds

  /**
   *  フェードインの開始時刻の取得
   *
   * フェードインの開始時刻を取得する。
   *
   * @return  フェードインの開始時刻[秒]
   */
  def GetFadeInStartTime(): Float = _fadeInStartTimeSeconds

  /**
   *  フェードインの終了時刻の取得
   *
   * フェードインの終了時刻を取得する。
   *
   * @return  フェードインの終了時刻[秒]
   */
  def GetEndTime(): Float = _endTimeSeconds

  /**
   *  モーションの開始時刻の設定
   *
   * モーションの開始時刻を設定する。
   *
   * @param   startTime   モーションの開始時刻[秒]
   */
  def SetStartTime(startTime: Float): Unit = {
    this._startTimeSeconds = startTime
  }

  /**
   *  フェードインの開始時刻の設定
   *
   * フェードインの開始時刻を設定する。
   *
   * @param   startTime   フェードインの開始時刻[秒]
   */
  def SetFadeInStartTime(startTime: Float): Unit = {
    this._fadeInStartTimeSeconds = startTime
  }

  /**
   *  フェードインの終了時刻の設定
   *
   * フェードインの終了時刻を設定する。
   *
   * @param   endTime   フェードインの終了時刻[秒]
   */
  def SetEndTime(endTime: Float): Unit = {
    this._endTimeSeconds = endTime
  }

  /**
   *  モーションの終了の設定
   *
   * モーションの終了を設定する。
   *
   * @param   f   trueならモーションの終了
   */
  def IsFinished(f: Boolean): Unit = {
    this._finished = f
  }

  /**
   *  モーションの開始の設定
   *
   * モーションの開始を設定する。
   *
   * @param   f   trueならモーションの開始
   */
  def IsStarted(f: Boolean): Unit = {
    this._started = f
  }

  /**
   *  モーションの有効性の確認
   *
   * モーションの有効・無効を取得する。
   *
   * @return  true    モーションは有効 / false   モーションは無効
   */
  def IsAvailable(): Boolean = _available

  /**
   *  モーションの有効性の設定
   *
   * モーションの有効・無効を設定する。
   *
   * @param   v   trueならモーションは有効
   */
  def IsAvailable(v: Boolean): Unit = {
    this._available = v
  }

  /**
   *  モーションの状態の設定
   *
   * モーションの状態を設定する。
   *
   * @param   timeSeconds    現在時刻[秒]
   * @param   weight  モーションの重み
   */
  def SetState(timeSeconds: Float, weight: Float): Unit = {
    this._stateTimeSeconds = timeSeconds
    this._stateWeight = weight
  }

  /**
   *  モーションの現在時刻の取得
   *
   * モーションの現在時刻を取得する。
   *
   * @return  モーションの現在時刻[秒]
   */
  def GetStateTime(): Float = _stateTimeSeconds

  /**
   *  モーションの重みの取得
   *
   * モーションの重みを取得する。
   *
   * @return  モーションの重み
   */
  def GetStateWeight(): Float = _stateWeight

  /**
   *  最後にイベントの発火をチェックした時間を取得
   *
   * 最後にイベントの発火をチェックした時間を取得する。
   *
   * @return  最後にイベントの発火をチェックした時間[秒]
   */
  def GetLastCheckEventTime(): Float = _lastEventCheckSeconds

  /**
   *  最後にイベントをチェックした時間を設定
   *
   * 最後にイベントをチェックした時間を設定する。
   *
   * @param    checkTime   最後にイベントをチェックした時間[秒]
   */
  def SetLastCheckEventTime(checkTime: Float): Unit = {
    this._lastEventCheckSeconds = checkTime
  }

  /**
   *  フェードアウトが開始しているかを取得
   *
   * モーションがフェードアウトが開始しているかを取得する。
   *
   * @return    フェードアウトが開始しているか
   */
  def IsTriggeredFadeOut(): Boolean = {
    _IsTriggeredFadeOut && _endTimeSeconds < 0.0f
  }

  /**
   *  フェードアウト時間の取得
   *
   * モーションのフェードアウト時間を取得する。
   *
   * @return    フェードアウト開始[秒]
   */
  def GetFadeOutSeconds(): Float = {
    this._fadeOutSeconds
  }

}
