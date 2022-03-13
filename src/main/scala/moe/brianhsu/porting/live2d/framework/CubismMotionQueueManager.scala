package moe.brianhsu.porting.live2d.framework

import CubismMotionQueueManager.CubismMotionEventFunction
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object CubismMotionQueueManager {
  trait CubismMotionEventFunction {
    def apply(caller: CubismMotionQueueManager, eventValue: String, customData: AnyRef): Unit
  }
}

class CubismMotionQueueManager {
  protected var _userTimeSeconds: Float = 0.0f
  private var _motions: List[CubismMotionQueueEntry] = Nil
  private var _eventCallback: CubismMotionEventFunction = null
  private var _eventCustomData: AnyRef = null

  /**
   * 指定したモーションの開始
   *
   * 指定したモーションを開始する。同じタイプのモーションが既にある場合は、既存のモーションに終了フラグを立て、フェードアウトを開始させる。
   *
   * @param   motion          開始するモーション
   * @return                      開始したモーションの識別番号を返す。個別のモーションが終了したか否かを判定するIsFinished()の引数で使用する。開始できない時は「-1」
   */
  def StartMotion(motion: ACubismMotion): CubismMotionQueueEntry = {

    if (motion == null) {
      return null
    }

    println("motions.size:" + _motions.size)
    this._motions.foreach(e => e.SetFadeout(e._motion.GetFadeOutTime()))
    val entry = new CubismMotionQueueEntry
    entry._motion = motion
    this._motions = this._motions.appended(entry)
    entry
  }

  /**
   * すべてのモーションの終了の確認
   *
   * すべてのモーションが終了しているかどうか。
   *
   * @return  true    すべて終了している / false   終了していない
   */
  def IsFinished(): Boolean = this._motions.forall(_.IsFinished())

  /**
   * すべてのモーションの停止
   *
   * すべてのモーションを停止する。
   */
  def StopAllMotions(): Unit = {
    this._motions = Nil
  }

  /**
   * イベントを受け取るCallbackの登録
   *
   * イベントを受け取るCallbackの登録をする。
   *
   * @param   callback     コールバック関数
   * @param   customData   コールバックに返されるデータ
   */
  def SetEventCallback(callback: CubismMotionEventFunction, customData: AnyRef = null): Unit = {
    this._eventCallback = callback
    this._eventCustomData = customData
  }

  /**
   * モーションの更新
   *
   * モーションを更新して、モデルにパラメータ値を反映する。
   *
   * @param   model   対象のモデル
   * @param   userTimeSeconds   デルタ時間の積算値[秒]
   * @return  true    モデルへパラメータ値の反映あり / false   モデルへパラメータ値の反映なし(モーションの変化なし)
   */
  def DoUpdateMotion(model: Live2DModel, userTimeSeconds: Float): Boolean = {
     if (_motions == Nil) {
       false
     } else {
       this._motions.foreach { queueEntry =>
         val motion = queueEntry._motion

         motion.UpdateParameters(model, queueEntry, userTimeSeconds)

         val firedList = motion.GetFiredEvent(
           queueEntry.GetLastCheckEventTime() - queueEntry.GetStartTime(),
           userTimeSeconds - queueEntry.GetStartTime()
         )

         firedList.foreach { event =>
           if (this._eventCallback != null) {
             this._eventCallback(this, event, _eventCustomData)
           }
         }
         queueEntry.SetLastCheckEventTime(userTimeSeconds)
         if (!queueEntry.IsFinished() && queueEntry.IsTriggeredFadeOut()) {
           queueEntry.StartFadeout(queueEntry.GetFadeOutSeconds(), userTimeSeconds)
         }

       }
       this._motions = this._motions.filterNot(_.IsFinished())
       true
     }
  }

}
