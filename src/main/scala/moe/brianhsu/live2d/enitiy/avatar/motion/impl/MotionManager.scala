package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation

class MotionManager {
  private var motionQueue: List[MotionWithTransition] = Nil
  private var callbackHolder: Option[Callback] = None

  def currentMotions: List[MotionWithTransition] = motionQueue
  def iaAllFinished: Boolean = this.motionQueue.forall(_.isFinished)

  def setEventCallbackForAllMotions(callback: Callback): Unit = {
    this.callbackHolder = Some(callback)
  }

  def startMotion(motion: Motion): MotionWithTransition = {
    startMotion(new MotionWithTransition(motion))
  }

  def startMotion(motion: MotionWithTransition): MotionWithTransition = {
    this.callbackHolder.foreach(motion.setEventCallback)
    this.motionQueue.foreach(e => e.markAsForceFadeOut())
    this.motionQueue = this.motionQueue.appended(motion)
    motion
  }

  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[UpdateOperation] = {

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
