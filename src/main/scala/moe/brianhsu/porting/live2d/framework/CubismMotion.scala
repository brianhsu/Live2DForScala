package moe.brianhsu.porting.live2d.framework

import ACubismMotion.FinishedMotionCallback
import CubismMotion.{CubismMotionSegmentType_Bezier, EffectNameEyeBlink, EffectNameLipSync}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.CubismMotionCurveTarget.{CubismMotionCurveTarget_Model, CubismMotionCurveTarget_Parameter, CubismMotionCurveTarget_PartOpacity}
import moe.brianhsu.porting.live2d.framework.math.CubismMath

object CubismMotion {
  private val EffectNameEyeBlink = "EyeBlink"
  private val EffectNameLipSync  = "LipSync"
  val CubismMotionSegmentType_Linear = 0         ///< リニア
  val CubismMotionSegmentType_Bezier = 1         ///< ベジェ曲線
  val CubismMotionSegmentType_Stepped = 2        ///< ステップ
  val CubismMotionSegmentType_InverseStepped = 3  ///< インバースステップ

  def apply(motionInfo: MotionSetting, onFinishHandler: FinishedMotionCallback,
            eyeBlinkParameterIds: List[String], lipSyncParameterIds: List[String]): CubismMotion = {
    val cubismMotion = new CubismMotion
    val motionData = CubismMotionData(motionInfo)
    cubismMotion._sourceFrameRate = motionInfo.meta.fps
    cubismMotion._loopDurationSeconds = motionInfo.meta.duration
    cubismMotion._onFinishedMotion = onFinishHandler
    cubismMotion._fadeInSeconds = motionInfo.fadeInTime.filter(_ >= 0).getOrElse(1.0f)
    cubismMotion._fadeOutSeconds = motionInfo.fadeOutTime.filter(_ >= 0).getOrElse(1.0f)
    cubismMotion._motionData = motionData
    cubismMotion.SetEffectIds(eyeBlinkParameterIds, lipSyncParameterIds)
    cubismMotion
  }

}

class CubismMotion extends ACubismMotion {
  var _sourceFrameRate: Float = 30.0f                   ///< ロードしたファイルのFPS。記述が無ければデフォルト値15fpsとなる
  var _loopDurationSeconds: Float = -1.0f               ///< mtnファイルで定義される一連のモーションの長さ
  var _isLoop: Boolean = false                            ///< ループするか?
  var _isLoopFadeIn: Boolean = true                      ///< ループ時にフェードインが有効かどうかのフラグ。初期値では有効。
  var _lastWeight: Float = 0.0f                        ///< 最後に設定された重み

  var _motionData: CubismMotionData = null                   ///< 実際のモーションデータ本体

  var _eyeBlinkParameterIds: List[String] = Nil   ///< 自動まばたきを適用するパラメータIDハンドルのリスト。  モデル（モデルセッティング）とパラメータを対応付ける。
  var _lipSyncParameterIds: List[String] = Nil    ///< リップシンクを適用するパラメータIDハンドルのリスト。  モデル（モデルセッティング）とパラメータを対応付ける。

  var _modelCurveIdEyeBlink: String = null               ///< モデルが持つ自動まばたき用パラメータIDのハンドル。  モデルとモーションを対応付ける。
  var _modelCurveIdLipSync: String = null                ///< モデルが持つリップシンク用パラメータIDのハンドル。  モデルとモーションを対応付ける。


  override protected def DoUpdateParameters(model: Live2DModel, userTimeSeconds: Float, fadeWeight: Float, motionQueueEntry: CubismMotionQueueEntry): Unit = {
    if (_modelCurveIdEyeBlink == null) {
      _modelCurveIdEyeBlink = EffectNameEyeBlink
    }

    if (_modelCurveIdLipSync == null) {
      _modelCurveIdLipSync = EffectNameLipSync
    }

    var timeOffsetSeconds: Float = userTimeSeconds - motionQueueEntry.GetStartTime()

    if (timeOffsetSeconds < 0.0f) {
      timeOffsetSeconds = 0.0f; // エラー回避
    }
    var lipSyncValue: Float = Float.MaxValue
    var eyeBlinkValue = Float.MaxValue

    //まばたき、リップシンクのうちモーションの適用を検出するためのビット（maxFlagCount個まで
    val MaxTargetSize: Int = 64
    var lipSyncFlags: Int = 0
    var eyeBlinkFlags: Int = 0

    //瞬き、リップシンクのターゲット数が上限を超えている場合
    if (_eyeBlinkParameterIds.size > MaxTargetSize) {
      println(s"too many eye blink targets : ${_eyeBlinkParameterIds.size}")
    }
    if (_lipSyncParameterIds.size > MaxTargetSize) {
      println(s"too many lip sync targets : ${_lipSyncParameterIds.size}")
    }

    val tmpFadeIn: Float = if (_fadeInSeconds <= 0.0f) {
     1.0f
    } else {
      CubismMath.getEasingSin((userTimeSeconds - motionQueueEntry.GetStartTime()) / _fadeInSeconds)
    }

    val tmpFadeOut: Float = if (_fadeOutSeconds <= 0.0f || motionQueueEntry.GetEndTime() < 0.0f) {
      1.0f
    } else {
      CubismMath.getEasingSin((motionQueueEntry.GetEndTime() - userTimeSeconds) / _fadeOutSeconds)
    }

    var value: Float = 0.0f

    // 'Repeat' time as necessary.
    var time: Float = timeOffsetSeconds

    if (_isLoop) {
      while (time > _motionData.Duration) {
        time -= _motionData.Duration
      }
    }
    // Evaluate model curves.
    var c: Int = 0
    val curves = _motionData.Curves
    while(c < _motionData.CurveCount && curves(c).Type == CubismMotionCurveTarget_Model) {
      // Evaluate curve and call handler.
      value = EvaluateCurve(_motionData, curves(c), time)

      if (curves(c).Id == _modelCurveIdEyeBlink) {
        eyeBlinkValue = value
      } else if (curves(c).Id == _modelCurveIdLipSync) {
        lipSyncValue = value
      }
      c += 1
    }
    var parameterMotionCurveCount = 0

    while(c < _motionData.CurveCount && curves(c).Type == CubismMotionCurveTarget_Parameter) {
      parameterMotionCurveCount += 1
      val sourceValue: Float = model.parameters(curves(c).Id).current

      // Evaluate curve and apply value.
      value = EvaluateCurve(_motionData, curves(c), time)
      if (eyeBlinkValue != Float.MaxValue) {
        var isBreak: Boolean = false
        for (i <- _eyeBlinkParameterIds.indices if i < MaxTargetSize && !isBreak) {
          if (_eyeBlinkParameterIds(i) == curves(c).Id) {
            value *= eyeBlinkValue
            eyeBlinkFlags |= (1 << i)
            isBreak = true
          }
        }
      }
      if (lipSyncValue != Float.MaxValue) {
        var isBreak: Boolean = false
        for (i <- _lipSyncParameterIds.indices if i < MaxTargetSize && !isBreak) {
          if (_lipSyncParameterIds(i) == curves(c).Id)
          {
            value += lipSyncValue
            lipSyncFlags |= (1 << i)
            isBreak = true
          }
        }
      }

      var v: Float = 0
      // パラメータごとのフェード
      if (curves(c).FadeInTime < 0.0f && curves(c).FadeOutTime < 0.0f)
      {
        //モーションのフェードを適用
        v = sourceValue + (value - sourceValue) * fadeWeight
      } else {
        // パラメータに対してフェードインかフェードアウトが設定してある場合はそちらを適用
        var fin: Float = 0
        var fout: Float = 0
        if (curves(c).FadeInTime < 0.0f) {
          fin = tmpFadeIn
        } else {
          fin = if (curves(c).FadeInTime == 0.0f) {
            1.0f
          } else {
            CubismMath.getEasingSin((userTimeSeconds - motionQueueEntry.GetFadeInStartTime()) / curves(c).FadeInTime)
          }

        }

        if (curves(c).FadeOutTime < 0.0f) {
          fout = tmpFadeOut
        } else {
          fout = if (curves(c).FadeOutTime == 0.0f || motionQueueEntry.GetEndTime() < 0.0f) {
            1.0f
          } else {
            CubismMath.getEasingSin((motionQueueEntry.GetEndTime() - userTimeSeconds) / curves(c).FadeOutTime)
          }
        }
        val paramWeight: Float = _weight * fin * fout

        // パラメータごとのフェードを適用
        v = sourceValue + (value - sourceValue) * paramWeight
      }
      //model.setParameterValueUsingIndex(curves(c).Id, model.getParameterIndex(curves(c).Id), v)
      model.getParameterWithFallback(curves(c).Id).update(v)
      //model.setParameterValue(curves(c).Id, v)
      c += 1
    }

    {
      if (eyeBlinkValue != Float.MaxValue) {
        for (i <- _eyeBlinkParameterIds.indices if i < MaxTargetSize) {
          val sourceValue = model.parameters(_eyeBlinkParameterIds(i)).current
          //モーションでの上書きがあった時にはまばたきは適用しない
          if (((eyeBlinkFlags >> i) & 0x01) != 0) {
            //continue;
          } else {

            val v = sourceValue + (eyeBlinkValue - sourceValue) * fadeWeight
            model.parameters.get(_eyeBlinkParameterIds(i)).foreach(_.update(v))
            //model.setParameterValue(_eyeBlinkParameterIds(i), v)
          }
        }
      }

      if (lipSyncValue != Float.MaxValue) {
        for (i <- _lipSyncParameterIds.indices if i < MaxTargetSize) {
          val sourceValue = model.parameters(_lipSyncParameterIds(i)).current
          //モーションでの上書きがあった時にはリップシンクは適用しない
          if (((lipSyncFlags >> i) & 0x01) != 0) {
            //continue;
          } else {
            val v = sourceValue + (lipSyncValue - sourceValue) * fadeWeight
            model.parameters.get(_lipSyncParameterIds(i)).foreach(_.update(v))
            //model.setParameterValue(_lipSyncParameterIds(i), v)
          }
        }
      }
    }

    while (c < _motionData.CurveCount && curves(c).Type == CubismMotionCurveTarget_PartOpacity) {
      // Evaluate curve and apply value.
      value = EvaluateCurve(_motionData, curves(c), time)
      model.getParameterWithFallback(curves(c).Id).update(value)
      //model.setParameterValueUsingIndex(curves(c).Id, model.getParameterIndex(curves(c).Id), value)
      //model.setParameterValue(curves(c).Id, value)
      c += 1
    }

    if (timeOffsetSeconds >= _motionData.Duration) {
      if (_isLoop) {
        motionQueueEntry.SetStartTime(userTimeSeconds) //最初の状態へ
        if (_isLoopFadeIn) {
          //ループ中でループ用フェードインが有効のときは、フェードイン設定し直し
          motionQueueEntry.SetFadeInStartTime(userTimeSeconds)
        }
      }
      else
      {
        if (this->_onFinishedMotion != null) {
          this->_onFinishedMotion(this)
        }

        motionQueueEntry.IsFinished(true)
      }
    }

    _lastWeight = fadeWeight

  }

  private def EvaluateCurve(motionData: CubismMotionData, curve: CubismMotionCurve, time: Float): Float = {

    var target: Int = -1
    val totalSegmentCount: Int = curve.BaseSegmentIndex + curve.SegmentCount
    var pointPosition: Int = 0
    var isBreak: Boolean = false

    for (i <- curve.BaseSegmentIndex until totalSegmentCount if !isBreak) {
      // Get first point of next segment.
      pointPosition = motionData.Segments(i).BasePointIndex + ( if (motionData.Segments(i).SegmentType == CubismMotionSegmentType_Bezier)  3 else 1)

      // Break if time lies within current segment.
      if (motionData.Points(pointPosition).Time > time) {
        target = i
        isBreak = true
      }
    }

    if (target == -1) {
      return motionData.Points(pointPosition).Value
    }

    val segment = motionData.Segments(target)
    segment.Evaluate(motionData.Points.drop(segment.BasePointIndex), time)
  }

  /**
   * ループ情報の設定
   *
   * ループ情報を設定する。
   *
   * @param   loop    ループ情報
   */
  def IsLoop(loop: Boolean): Unit = {
    this._isLoop = loop
  }

  /**
   * ループ情報の取得
   *
   * モーションがループするかどうか？
   *
   * @return  true    ループする / false   ループしない
   */
  def IsLoop(): Boolean = this._isLoop

  /**
   * ループ時のフェードイン情報の設定
   *
   * ループ時のフェードイン情報を設定する。
   *
   * @param   loopFadeIn  ループ時のフェードイン情報
   */
  def IsLoopFadeIn(loopFadeIn: Boolean): Unit = {
    this._isLoopFadeIn = loopFadeIn
  }

  /**
   * ループ時のフェードイン情報の取得
   *
   * ループ時にフェードインするかどうか？
   *
   * @return  true    する / false   しない
   */
  def IsLoopFadeIn(): Boolean = this._isLoopFadeIn

  /**
   * モーションの長さの取得
   *
   * モーションの長さを取得する。
   *
   * @return  モーションの長さ[秒]
   */
  override def GetDuration(): Float = if (_isLoop) -1.0f else _loopDurationSeconds

  /**
   * モーションのループ時の長さの取得
   *
   * モーションのループ時の長さを取得する。
   *
   * @return  モーションのループ時の長さ[秒]
   */
  override def GetLoopDuration(): Float = this._loopDurationSeconds

  /**
   * パラメータに対するフェードインの時間の設定
   *
   * パラメータに対するフェードインの時間を設定する。
   *
   * @param   parameterId     パラメータID
   * @param   value           フェードインにかかる時間[秒]
   */
  def SetParameterFadeInTime(parameterId: String, value: Float): Unit = {
    this._motionData.Curves
      .find(_.Id == parameterId)
      .foreach(_.FadeInTime = value)
  }

  /**
   * パラメータに対するフェードアウトの時間の設定
   *
   * パラメータに対するフェードアウトの時間を設定する。
   *
   * @param   parameterId     パラメータID
   * @param   value           フェードアウトにかかる時間[秒]
   */
  def SetParameterFadeOutTime(parameterId: String, value: Float): Unit = {
    this._motionData.Curves
      .find(_.Id == parameterId)
      .foreach(_.FadeOutTime = value)
  }

  /**
   * パラメータに対するフェードインの時間の取得
   *
   * パラメータに対するフェードインの時間を取得する。
   *
   * @param   parameterId     パラメータID
   * @return   フェードインにかかる時間[秒]
   */
  def GetParameterFadeInTime(parameterId: String): Float = {
    this._motionData.Curves
      .find(_.Id == parameterId)
      .map(_.FadeInTime)
      .getOrElse(-1.0f)
  }

  /**
   * パラメータに対するフェードアウトの時間の取得
   *
   * パラメータに対するフェードアウトの時間を取得する。
   *
   * @param   parameterId     パラメータID
   * @return   フェードアウトにかかる時間[秒]
   */
  def GetParameterFadeOutTime(parameterId: String): Float = {
    this._motionData.Curves
      .find(_.Id == parameterId)
      .map(_.FadeOutTime)
      .getOrElse(-1.0f)
  }

  /**
   * 自動エフェクトがかかっているパラメータIDリストの設定
   *
   * 自動エフェクトがかかっているパラメータIDリストを設定する。
   *
   * @param   eyeBlinkParameterIds    自動まばたきがかかっているパラメータIDのリスト
   * @param   lipSyncParameterIds     リップシンクがかかっているパラメータIDのリスト
   */
  def SetEffectIds(eyeBlinkParameterIds: List[String], lipSyncParameterIds: List[String]): Unit = {
    this._eyeBlinkParameterIds = eyeBlinkParameterIds
    this._lipSyncParameterIds = lipSyncParameterIds
  }

  /**
   * モデルのパラメータ更新
   *
   * イベント発火のチェック。
   * 入力する時間は呼ばれるモーションタイミングを０とした秒数で行う。
   *
   * @param   beforeCheckTimeSeconds   前回のイベントチェック時間[秒]
   * @param   motionTimeSeconds        今回の再生時間[秒]
   */
  override def GetFiredEvent(beforeCheckTimeSeconds: Float, motionTimeSeconds: Float): List[String] = {

    this._firedEventValues = this._motionData.Events
      .filter(e => e.FireTime >= beforeCheckTimeSeconds && e.FireTime <= motionTimeSeconds)
      .map(_.Value).toList

    this._firedEventValues
  }


}
