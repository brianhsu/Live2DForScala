package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionUpdater}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class MotionManager extends MotionUpdater {
  private var motionQueue: List[MotionWithTransition] = Nil

  def currentMotions: List[MotionWithTransition] = motionQueue

  def setEventCallbackForAllMotions(callback: Callback): Unit = {
    motionQueue.foreach(_.setEventCallback(callback))
  }

  def startMotion(motion: Motion): MotionWithTransition = {
    val wrappedMotion = wrapToTransitionalIfNotAlreadyTransitional(motion)
    this.motionQueue.foreach(e => e.markAsForceFadeOut())
    this.motionQueue = this.motionQueue.appended(wrappedMotion)
    wrappedMotion
  }

  private def wrapToTransitionalIfNotAlreadyTransitional(motion: Motion): MotionWithTransition = {
    if (motion.isInstanceOf[MotionWithTransition]) {
      motion.asInstanceOf[MotionWithTransition]
    } else {
      new MotionWithTransition(motion)
    }
  }

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[EffectOperation] = {
    val operations = this.motionQueue.flatMap { _.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, weight) }

    for {
      motion <- this.motionQueue if !motion.isFinished && motion.isForceToFadeOut
    } {
      motion.startFadeOut(totalElapsedTimeInSeconds)
    }

    this.motionQueue = this.motionQueue.filterNot(_.isFinished)
    operations
  }
}
