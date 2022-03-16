package moe.brianhsu.porting.live2d.framework

import CubismMotion.{EffectNameEyeBlink, EffectNameLipSync, MaxEffectTargetSize}
import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarMotionDataReader
import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueUpdate, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.{Model, Parameter, PartOpacity}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{MotionCurve, MotionData}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object CubismMotion {
  private val EffectNameEyeBlink = "EyeBlink"
  private val EffectNameLipSync  = "LipSync"
  private val MaxEffectTargetSize: Int = 64

  def apply(motionInfo: MotionSetting, eyeBlinkParameterIds: List[String], lipSyncParameterIds: List[String]): CubismMotion = {
    new CubismMotion(
      new AvatarMotionDataReader(motionInfo).loadMotionData(),
      eyeBlinkParameterIds, lipSyncParameterIds,
      isLoop = false, isLoopFadeIn = false,
      Option(motionInfo.meta.duration).filter(_ > 0.0f),
      motionInfo.fadeInTime.filter(_ >= 0),
      motionInfo.fadeOutTime.filter(_ >= 0).orElse(Some(1.0f))
    )
  }

}

class CubismMotion(motionData: MotionData,
                   val eyeBlinkParameterIds: List[String] = Nil,
                   val lipSyncParameterIds: List[String] = Nil,
                   override val isLoop: Boolean = false,
                   override val isLoopFadeIn: Boolean = false,
                   override val durationInSeconds: Option[Float],
                   override val fadeInTimeInSeconds: Option[Float],
                   override val fadeOutTimeInSeconds: Option[Float]) extends Motion {

  override def events: List[MotionEvent] = this.motionData.events.toList

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float,
                                   weight: Float,
                                   startTimeInSeconds: Float,
                                   fadeInStartTimeInSeconds: Float,
                                   endTimeInSeconds: Option[Float]): List[EffectOperation] = {

    var operations: List[EffectOperation] = Nil
    var lipSyncValue: Float = Float.MaxValue
    var eyeBlinkValue = Float.MaxValue

    //まばたき、リップシンクのうちモーションの適用を検出するためのビット（maxFlagCount個まで
    var lipSyncFlags: Int = 0
    var eyeBlinkFlags: Int = 0

    //瞬き、リップシンクのターゲット数が上限を超えている場合
    if (eyeBlinkParameterIds.size > MaxEffectTargetSize) {
      println(s"too many eye blink targets : ${eyeBlinkParameterIds.size}")
    }
    if (lipSyncParameterIds.size > MaxEffectTargetSize) {
      println(s"too many lip sync targets : ${lipSyncParameterIds.size}")
    }

    val tmpFadeIn = calculateTempFadeIn(totalElapsedTimeInSeconds, startTimeInSeconds)
    val tmpFadeOut = calculateTempFadeOut(totalElapsedTimeInSeconds, endTimeInSeconds)
    val elapsedTimeSinceLastLoop = calculateElapsedTimeSinceLastLoop(totalElapsedTimeInSeconds, startTimeInSeconds)

    // Evaluate model curves.
    var c: Int = 0
    val curves = motionData.curves
    while(c < motionData.curveCount && curves(c).targetType == Model) {
      // Evaluate curve and call handler.
      val value = evaluateCurve(motionData, curves(c), elapsedTimeSinceLastLoop)

      if (curves(c).id == EffectNameEyeBlink) {
        eyeBlinkValue = value
      } else if (curves(c).id == EffectNameLipSync) {
        lipSyncValue = value
      }
      c += 1
    }
    var parameterMotionCurveCount = 0

    while(c < motionData.curveCount && curves(c).targetType == Parameter) {
      parameterMotionCurveCount += 1
      val sourceValue: Float = model.parameters(curves(c).id).current

      // Evaluate curve and apply value.
      var value = evaluateCurve(motionData, curves(c), elapsedTimeSinceLastLoop)
      if (eyeBlinkValue != Float.MaxValue) {
        var isBreak: Boolean = false
        for (i <- eyeBlinkParameterIds.indices if i < MaxEffectTargetSize && !isBreak) {
          if (eyeBlinkParameterIds(i) == curves(c).id) {
            value *= eyeBlinkValue
            eyeBlinkFlags |= (1 << i)
            isBreak = true
          }
        }
      }
      if (lipSyncValue != Float.MaxValue) {
        var isBreak: Boolean = false
        for (i <- lipSyncParameterIds.indices if i < MaxEffectTargetSize && !isBreak) {
          if (lipSyncParameterIds(i) == curves(c).id)
          {
            value += lipSyncValue
            lipSyncFlags |= (1 << i)
            isBreak = true
          }
        }
      }

      var v: Float = 0
      // パラメータごとのフェード
      if (curves(c).fadeInTime < 0.0f && curves(c).fadeOutTime < 0.0f)
      {
        //モーションのフェードを適用
        v = sourceValue + (value - sourceValue) * weight
      } else {
        // パラメータに対してフェードインかフェードアウトが設定してある場合はそちらを適用
        var fin: Float = 0
        var fout: Float = 0
        if (curves(c).fadeInTime < 0.0f) {
          fin = tmpFadeIn
        } else {
          fin = if (curves(c).fadeInTime == 0.0f) {
            1.0f
          } else {
            Easing.sine((totalElapsedTimeInSeconds - fadeInStartTimeInSeconds) / curves(c).fadeInTime)
          }

        }

        if (curves(c).fadeOutTime < 0.0f) {
          fout = tmpFadeOut
        } else {
          fout = if (curves(c).fadeOutTime == 0.0f || endTimeInSeconds.isEmpty) {
            1.0f
          } else {
            Easing.sine((endTimeInSeconds.get - totalElapsedTimeInSeconds) / curves(c).fadeOutTime)
          }
        }
        val paramWeight: Float = fin * fout

        // パラメータごとのフェードを適用
        v = sourceValue + (value - sourceValue) * paramWeight
      }
      operations = operations.appended(FallbackParameterValueUpdate(curves(c).id, v))
      c += 1
    }


    operations = operations ++
      createEyeBlinkOperations(model, weight, eyeBlinkValue, eyeBlinkFlags) ++
      createLipSyncOperations(model, weight, lipSyncValue, lipSyncFlags)

    {
      if (lipSyncValue != Float.MaxValue) {
        for (i <- lipSyncParameterIds.indices if i < MaxEffectTargetSize) {
          val sourceValue = model.parameters(lipSyncParameterIds(i)).current
          //モーションでの上書きがあった時にはリップシンクは適用しない
          if (((lipSyncFlags >> i) & 0x01) != 0) {
            //continue;
          } else {
            val v = sourceValue + (lipSyncValue - sourceValue) * weight
            model.parameters.get(lipSyncParameterIds(i)).foreach { p =>
              operations = operations.appended(ParameterValueUpdate(p.id, v))
            }
          }
        }
      }
    }

    while (c < motionData.curveCount && curves(c).targetType == PartOpacity) {
      // Evaluate curve and apply value.
      val value = evaluateCurve(motionData, curves(c), elapsedTimeSinceLastLoop)
      operations = operations.appended(FallbackParameterValueUpdate(curves(c).id, value))
      c += 1
    }

    operations
  }

  private def createEyeBlinkOperations(model: Live2DModel, weight: Float, eyeBlinkValue: Float, eyeBlinkFlags: Int) = {
    if (eyeBlinkValue != Float.MaxValue) {
      for {
        (parameterId, i) <- eyeBlinkParameterIds.zipWithIndex if ((eyeBlinkFlags >> i) & 0x01) == 0
        parameter <- model.parameters.get(parameterId)
        sourceValue = parameter.current
        newValue = sourceValue + (eyeBlinkValue - sourceValue) * weight
      } yield {
        ParameterValueUpdate(parameter.id, newValue)
      }
    } else {
      Nil
    }

  }
  private def createLipSyncOperations(model: Live2DModel, weight: Float, lipSyncValue: Float, lipSyncFlags: Int) = {
    if (lipSyncValue != Float.MaxValue) {
      for {
        (parameterId, i) <- lipSyncParameterIds.zipWithIndex if ((lipSyncFlags >> i) & 0x01) == 0
        parameter <- model.parameters.get(parameterId)
        sourceValue = parameter.current
        newValue = sourceValue + (lipSyncValue - sourceValue) * weight
      } yield {
        ParameterValueUpdate(parameter.id, newValue)
      }
    } else {
      Nil
    }

  }

  private def calculateElapsedTimeSinceLastLoop(totalElapsedTimeInSeconds: Float, startTimeInSeconds: Float) = {
    // 'Repeat' time as necessary.
    var timeSinceLastLoop: Float = totalElapsedTimeInSeconds - startTimeInSeconds
    if (this.isLoop) {
      while (timeSinceLastLoop > motionData.duration) {
        timeSinceLastLoop -= motionData.duration
      }
    }
    timeSinceLastLoop
  }

  private def calculateTempFadeOut(totalElapsedTimeInSeconds: Float, endTimeInSeconds: Option[Float]) = {
    val fadeOutHolder = for {
      _ <- endTimeInSeconds
      fadeOutTime <- fadeOutTimeInSeconds if fadeOutTime > 0.0f
    } yield {
      Easing.sine((endTimeInSeconds.get - totalElapsedTimeInSeconds) / fadeOutTime)
    }
    fadeOutHolder.getOrElse(1.0f)
  }

  private def calculateTempFadeIn(totalElapsedTimeInSeconds: Float, startTimeInSeconds: Float) = {
    fadeInTimeInSeconds.filter(_ > 0.0f)
      .map(fadeInTime => Easing.sine((totalElapsedTimeInSeconds - startTimeInSeconds) / fadeInTime))
      .getOrElse(1.0f)
  }

  private def evaluateCurve(motionData: MotionData, curve: MotionCurve, time: Float): Float = {

    var target: Int = -1
    val totalSegmentCount: Int = curve.baseSegmentIndex + curve.segmentCount
    var pointPosition: Int = 0
    var isBreak: Boolean = false

    for (i <- curve.baseSegmentIndex until totalSegmentCount if !isBreak) {
      // Get first point of next segment.
      pointPosition = motionData.segments(i).basePointIndex + motionData.segments(i).segmentType.pointCount

      // Break if time lies within current segment.
      if (motionData.points(pointPosition).time > time) {
        target = i
        isBreak = true
      }
    }

    if (target == -1) {
      return motionData.points(pointPosition).value
    }

    val segment = motionData.segments(target)
    segment.segmentType.evaluate(motionData.points.drop(segment.basePointIndex), time)
  }

}
