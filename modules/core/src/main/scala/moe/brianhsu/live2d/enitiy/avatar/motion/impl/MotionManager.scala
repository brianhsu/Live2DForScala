package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.{EventCallback, FinishedCallback, RepeatedCallback}
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation

class MotionManager {
  private var mEventCallbackHolder: Option[EventCallback] = None
  private var mFinishCallbackHolder: Option[FinishedCallback] = None
  private var mRepeatedCallbackHolder: Option[RepeatedCallback] = None

  private var motionQueue: List[MotionWithTransition] = Nil
  private[impl] def currentMotions: List[MotionWithTransition] = motionQueue

  def isAllFinished: Boolean = this.motionQueue.forall(_.isFinished)

  def eventCallbackHolder: Option[EventCallback] = mEventCallbackHolder
  def eventCallbackHolder_=(callbackHolder: Option[EventCallback]): Unit = {
    this.mEventCallbackHolder = callbackHolder
    this.currentMotions.foreach { _.eventCallbackHolder = callbackHolder }
  }

  def finishedCallbackHolder: Option[FinishedCallback] = mFinishCallbackHolder
  def finishedCallbackHolder_=(callbackHolder: Option[FinishedCallback]): Unit = {
    this.mFinishCallbackHolder = callbackHolder
    this.currentMotions.foreach { _.finishedCallbackHolder = callbackHolder }
  }

  def repeatedCallbackHolder: Option[RepeatedCallback] = mRepeatedCallbackHolder

  def repeatedCallbackHolder_=(callbackHolder: Option[RepeatedCallback]): Unit = {
    this.mRepeatedCallbackHolder = callbackHolder
    this.currentMotions.foreach { motion =>
      motion.repeatedCallbackHolder = callbackHolder
    }
  }

  def startMotion(motion: Motion): MotionWithTransition = {
    startMotion(new MotionWithTransition(motion))
  }

  def startMotion(motion: MotionWithTransition): MotionWithTransition = {
    motion.eventCallbackHolder = mEventCallbackHolder
    motion.finishedCallbackHolder = mFinishCallbackHolder
    motion.repeatedCallbackHolder = mRepeatedCallbackHolder

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
