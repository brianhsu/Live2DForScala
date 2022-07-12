package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.FaceDirectionByMouse
import moe.brianhsu.live2d.demo.app.DemoApp.{ClickAndDrag, FaceDirectionMode, FollowMouse}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceDirection

import javax.sound.sampled.Mixer
import scala.annotation.unused

trait EffectControl {
  this: DemoApp =>

  var faceDirectionMode: FaceDirectionMode = ClickAndDrag

  protected val faceDirectionCalculator = new FaceDirectionByMouse(60)
  protected val faceDirection = new FaceDirection(faceDirectionCalculator)

  def updateMotionLipSyncVolume(volume: Int): Unit = {
    this.mUpdateStrategyHolder.foreach(_.updateLipSyncFromMotionVolume(volume))
  }

  def updateMotionLipSyncWeight(weight: Int): Unit = {
    this.mUpdateStrategyHolder.foreach(_.updateLipSyncFromMotionWeight(weight))
  }

  def updateMicLipSyncWeight(weight: Int): Unit = {
    this.mUpdateStrategyHolder.foreach(_.updateMicLipSyncWeight(weight))
  }

  def enableMicLipSync(mixer: Mixer, weight: Int, forceEvenNoSetting: Boolean): Unit = {
    this.mUpdateStrategyHolder.foreach(_.enableMicLipSync(mixer, weight, forceEvenNoSetting))
  }

  def disableMicLipSync(): Unit = {
    this.mUpdateStrategyHolder.foreach(_.disableMicLipSync())
  }

  def enableLipSyncFromMotionSound(isEnabled: Boolean): Unit = {
    this.mUpdateStrategyHolder.foreach(_.enableLipSyncFromMotion(isEnabled))
  }

  def enableBreath(isEnabled: Boolean): Unit = {
    this.mUpdateStrategyHolder.foreach(_.enableBreath(isEnabled))
  }

  def enableFaceDirection(isEnabled: Boolean): Unit = {
    this.mUpdateStrategyHolder.foreach(_.enableFaceDirection(isEnabled))
  }

  def resetFaceDirection(): Unit = {
    faceDirectionCalculator.updateFaceTargetCoordinate(0, 0)
  }

  def enableEyeBlink(isEnabled: Boolean): Unit = {
    this.mUpdateStrategyHolder.foreach(_.enableEyeBlink(isEnabled))
  }

  def onMouseMoved(x: Int, y: Int): Unit = {
    if (faceDirectionMode == FollowMouse) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      faceDirectionCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    if (faceDirectionMode == ClickAndDrag) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      faceDirectionCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
  }

  def onMouseReleased(@unused x: Int, @unused y: Int): Unit = {
    if (faceDirectionMode == ClickAndDrag) {
      resetFaceDirection()
    }
  }


}
