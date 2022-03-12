package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionUpdater}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class MotionManager extends MotionUpdater {
  private var motions: List[MotionWithTransition] = Nil

  def setEventCallbackForAllMotions(callback: Callback): Unit = {
    motions.foreach(_.setEventCallback(callback))
  }

  def startMotion(motion: Motion): MotionWithTransition = {
    this.motions.foreach(e => e.prepareToFadeOut())
    this.motions ::= new MotionWithTransition(motion)
    this.motions.head
  }

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[EffectOperation] = {
    val operations = this.motions.flatMap { _.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, weight) }
    this.motions.foreach { motion =>
      if (!motion.isFinished && motion.isTriggeredFadeOut) {
        motion.startFadeOut(totalElapsedTimeInSeconds)
      }
    }
    this.motions = this.motions.filterNot(_.isFinished)
    operations
  }
}
