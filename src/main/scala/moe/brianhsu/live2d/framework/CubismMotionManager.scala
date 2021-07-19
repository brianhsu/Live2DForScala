package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.model.Live2DModel

class CubismMotionManager extends CubismMotionQueueManager {
  private var _currentPriority: Int = 0   ///<  現在再生中のモーションの優先度
  private var _reservePriority: Int = 0   ///<  再生予定のモーションの優先度。再生中は0になる。モーションファイルを別スレッドで読み込むときの機能。

  /**
   * 再生中のモーションの優先度の取得
   *
   * 再生中のモーションの優先度の取得する。
   *
   * @return  モーションの優先度
   */
  def GetCurrentPriority(): Int = this._currentPriority

  /**
   * 予約中のモーションの優先度の取得
   *
   * 予約中のモーションの優先度を取得する。
   *
   * @return  モーションの優先度
   */
  def GetReservePriority(): Int = this._reservePriority

  /**
   * 予約中のモーションの優先度の設定
   *
   * 予約中のモーションの優先度を設定する。
   *
   * @param   reservePriority     優先度
   */
  def SetReservePriority(reservePriority: Int): Unit = {
    this._reservePriority = reservePriority
  }

  /**
   * 優先度を設定してモーションの開始
   *
   * 優先度を設定してモーションを開始する。
   *
   * @param   motion          モーション
   * @param   autoDelete      再生が狩猟したモーションのインスタンスを削除するならtrue
   * @param   priority        優先度
   * @return                      開始したモーションの識別番号を返す。個別のモーションが終了したか否かを判定するIsFinished()の引数で使用する。開始できない時は「-1」
   */
  def StartMotionPriority(motion: ACubismMotion, autoDelete: Boolean, priority: Int): CubismMotionQueueEntry = {
    if (priority == _reservePriority) {
      _reservePriority = 0           // 予約を解除
    }

    _currentPriority = priority        // 再生中モーションの優先度を設定

    super.StartMotion(motion, autoDelete, _userTimeSeconds)
  }

  /**
   * モーションの更新
   *
   * モーションを更新して、モデルにパラメータ値を反映する。
   *
   * @param   model   対象のモデル
   * @param   deltaTimeSeconds    デルタ時間[秒]
   * @return  true    更新されている / false   更新されていない
   */
  def UpdateMotion(model: Live2DModel, deltaTimeSeconds: Float): Boolean = {
    _userTimeSeconds += deltaTimeSeconds

    val updated = super.DoUpdateMotion(model, _userTimeSeconds)

    if (IsFinished()) {
      _currentPriority = 0           // 再生中モーションの優先度を解除
    }

    updated
  }

  /**
   * モーションの予約
   *
   * モーションを予約する。
   *
   * @param   priority    優先度
   * @return  true    予約できた / false   予約できなかった
   */
  def ReserveMotion(priority: Int): Boolean = {

    if ((priority <= _reservePriority) ||
        (priority <= _currentPriority)) {
      return false
    }

    _reservePriority = priority

    true
  }

}
