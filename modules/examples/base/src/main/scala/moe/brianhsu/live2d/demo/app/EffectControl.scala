package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.FaceDirectionByMouse
import moe.brianhsu.live2d.demo.app.DemoApp.{ClickAndDrag, FaceDirectionMode, FollowMouse}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection, LipSyncFromMic, LipSyncFromMotionSound}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.MotionListener

import javax.sound.sampled.Mixer
import scala.annotation.unused

trait EffectControl extends MotionListener {
  this: DemoApp =>

  var faceDirectionMode: FaceDirectionMode = ClickAndDrag

  protected val targetPointCalculator = new FaceDirectionByMouse(60)
  protected val faceDirection = new FaceDirection(targetPointCalculator)

  override def onMotionStart(motion: MotionSetting): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .foreach(_.startWith(motion.sound))
    }

  }

  def updateMotionLipSyncVolume(volume: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].volume = volume
    }

  }

  def updateMotionLipSyncWeight(weight: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].weight = weight / 10.0f
    }
  }

  def disableBreath(): Unit = {

    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[Breath], AfterExpression)
    }
  }

  def updateMicLipSyncWeight(weight: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression)
    } {
      lipSync.asInstanceOf[LipSyncFromMic].weight = weight / 10.0f
    }
  }

  def enableMicLipSync(mixer: Mixer, weight: Int, forceEvenNoSetting: Boolean): Unit = {
    disableMicLipSync()

    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      val lipSyncFromMic = LipSyncFromMic(avatar.avatarSettings, mixer, weight / 10.0f, forceEvenNoSetting)
      lipSyncFromMic.failed.foreach(_.printStackTrace())
      lipSyncFromMic.foreach(effect => strategy.appendAndStartEffects(effect :: Nil, AfterExpression))
    }

  }

  def disableMicLipSync(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression)
    }
  }

  def disableLipSyncFromMotionSound(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
    }
  }

  def enableLipSyncFromMotionSound(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new LipSyncFromMotionSound(avatar.avatarSettings, 100) :: Nil, AfterExpression)
    }
  }

  def enableBreath(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new Breath :: Nil, AfterExpression)
    }
  }

  def disableFaceDirection(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[FaceDirection], AfterExpression)
    }
  }

  def enableFaceDirection(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(faceDirection :: Nil, AfterExpression)
    }
  }

  def resetFaceDirection(): Unit = {
    targetPointCalculator.updateFaceTargetCoordinate(0, 0)
  }

  def disableEyeBlink(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[EyeBlink], BeforeExpression)
    }
  }

  def enableEyeBlink(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new EyeBlink(avatar.avatarSettings) :: Nil, BeforeExpression)
    }
  }

  def onMouseMoved(x: Int, y: Int): Unit = {
    if (faceDirectionMode == FollowMouse) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      targetPointCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    if (faceDirectionMode == ClickAndDrag) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      targetPointCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
  }

  def onMouseReleased(@unused x: Int, @unused y: Int): Unit = {
    if (faceDirectionMode == ClickAndDrag) {
      resetFaceDirection()
    }
  }


}
