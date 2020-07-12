package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.FaceDirectionByMouse
import moe.brianhsu.live2d.demo.app.DemoApp.{ClickAndDrag, FaceDirectionMode, FollowMouse}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection, LipSyncFromMic, LipSyncFromMotionSound}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
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
      strategy.getEffect
        .filter(_.isInstanceOf[LipSyncFromMotionSound])
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .foreach(_.startWith(motion.sound))
    }

  }

  def updateMotionLipSyncVolume(volume: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.getEffect.filter(_.isInstanceOf[LipSyncFromMotionSound])
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].volume = volume
    }

  }

  def updateMotionLipSyncWeight(weight: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.getEffect.filter(_.isInstanceOf[LipSyncFromMotionSound])
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].weight = weight / 10.0f
    }
  }

  def disableBreath(): Unit = {

    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[Breath])
    }
  }

  def updateMicLipSyncWeight(weight: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.getEffect.filter(_.isInstanceOf[LipSyncFromMic])
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
      lipSyncFromMic.foreach(effect => strategy.appendAndStartEffects(effect :: Nil))
    }

  }

  def disableMicLipSync(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[LipSyncFromMic])
    }
  }

  def disableLipSyncFromMotionSound(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[LipSyncFromMotionSound])
    }
  }

  def enableLipSyncFromMotionSound(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new LipSyncFromMotionSound(avatar.avatarSettings, 100) :: Nil)
    }
  }

  def enableBreath(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new Breath :: Nil)
    }
  }

  def disableFaceDirection(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[FaceDirection])
    }
  }

  def enableFaceDirection(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(faceDirection :: Nil)
    }
  }

  def resetFaceDirection(): Unit = {
    targetPointCalculator.updateFaceTargetCoordinate(0, 0)
  }

  def disableEyeBlink(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.stopAndRemoveEffects(_.isInstanceOf[EyeBlink])
    }
  }

  def enableEyeBlink(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.appendAndStartEffects(new EyeBlink(avatar.avatarSettings) :: Nil)
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
