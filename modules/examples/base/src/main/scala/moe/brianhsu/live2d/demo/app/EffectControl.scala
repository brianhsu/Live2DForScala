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
      strategy.effects
        .filter(_.isInstanceOf[LipSyncFromMotionSound])
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .foreach(_.startWith(motion.sound))
    }

  }

  def updateMotionLipSyncVolume(volume: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.effects.filter(_.isInstanceOf[LipSyncFromMotionSound])
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].volume = volume
    }

  }

  def updateMotionLipSyncWeight(weight: Int): Unit = {
    for {
      strategy <- this.mUpdateStrategyHolder
      lipSync <- strategy.effects.filter(_.isInstanceOf[LipSyncFromMotionSound])
    } {
      lipSync.asInstanceOf[LipSyncFromMotionSound].weight = weight / 10.0f
    }
  }

  def disableBreath(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[Breath])
    }
  }

  def enableLipSyncFromMic(mixer: Mixer): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      val lipSyncFromMic = LipSyncFromMic(avatar.avatarSettings, mixer)
      lipSyncFromMic.failed.foreach(_.printStackTrace())
      lipSyncFromMic.foreach(l => strategy.effects ::= l )
    }

  }

  def disableLipSyncFromMic(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects
        .filter(_.isInstanceOf[LipSyncFromMic])
        .map(_.asInstanceOf[LipSyncFromMic])
        .foreach(_.stop())

      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[LipSyncFromMic])
    }
  }

  def disableLipSyncFromMotionSound(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects
        .filter(_.isInstanceOf[LipSyncFromMotionSound])
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .foreach(_.stop())

      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[LipSyncFromMotionSound])
    }
  }

  def enableLipSyncFromMotionSound(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= new LipSyncFromMotionSound(avatar.avatarSettings, 100)
    }
  }

  def enableBreath(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= new Breath
    }
  }

  def disableFaceDirection(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[FaceDirection])
    }
  }

  def enableFaceDirection(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= faceDirection
    }
  }

  def resetFaceDirection(): Unit = {
    targetPointCalculator.updateFaceTargetCoordinate(0, 0)
  }

  def disableEyeBlink(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[EyeBlink])
    }
  }

  def enableEyeBlink(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= new EyeBlink(avatar.avatarSettings)
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
