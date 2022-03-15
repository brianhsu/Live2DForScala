package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class MotionManager {
  private var motionQueue: List[MotionWithTransition] = Nil

  def currentMotions: List[MotionWithTransition] = motionQueue
  def iaAllFinished: Boolean = this.motionQueue.forall(_.isFinished)

  def setEventCallbackForAllMotions(callback: Callback): Unit = {
    motionQueue.foreach(_.setEventCallback(callback))
  }

  def startMotion(motion: Motion): MotionWithTransition = {
    startMotion(new MotionWithTransition(motion))
  }

  def startMotion(motion: MotionWithTransition): MotionWithTransition = {
    this.motionQueue.foreach(e => e.markAsForceFadeOut())
    this.motionQueue = this.motionQueue.appended(motion)
    motion
  }

  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[EffectOperation] = {

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
