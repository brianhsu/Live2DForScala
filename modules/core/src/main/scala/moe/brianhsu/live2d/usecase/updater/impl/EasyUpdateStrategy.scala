package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPhysicsReader, AvatarPoseReader}
import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection, LipSyncFromMic, LipSyncFromMotionSound, Physics, Pose}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy.MotionListener

import javax.sound.sampled.Mixer

class EasyUpdateStrategy(avatar: Avatar, eyeBlink: EyeBlink, breath: Breath,
                         lipSyncFromMotionSound: LipSyncFromMotionSound,
                         faceDirection: FaceDirection) extends GenericUpdateStrategy(avatar.avatarSettings, avatar.model, None) {

  def this(avatar: Avatar, faceDirectionCalculator: FaceDirectionCalculator) = this(
    avatar,
    new EyeBlink(avatar.avatarSettings),
    new Breath(),
    new LipSyncFromMotionSound(avatar.avatarSettings, 100),
    new FaceDirection(faceDirectionCalculator)
  )

  private val poseHolder: Option[Pose] = new AvatarPoseReader(avatar.avatarSettings).loadPose
  private val physicsHolder: Option[Physics] = new AvatarPhysicsReader(avatar.avatarSettings).loadPhysics
  private var lipSyncFromMicHolder: Option[LipSyncFromMic] = None

  private val beforeExpressionEffects = List(eyeBlink)
  private val afterExpressionEffects = List(
    Some(faceDirection), Some(breath), Some(lipSyncFromMotionSound),
    physicsHolder, poseHolder
  ).flatten

  override val motionListener: Option[MotionListener] = Some((motion: MotionSetting) => {
    findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
      .map(_.asInstanceOf[LipSyncFromMotionSound])
      .foreach(_.startWith(motion.sound))
  })

  this.appendAndStartEffects(beforeExpressionEffects, BeforeExpression)
  this.appendAndStartEffects(afterExpressionEffects, AfterExpression)


  def isBreathEnabled: Boolean = this.findEffects(_ == breath, AfterExpression).nonEmpty

  def enableBreath(isEnabled: Boolean): Unit = {
    isEnabled match {
      case true if !isBreathEnabled =>
        this.appendAndStartEffects(breath :: Nil, AfterExpression)

      case false if isBreathEnabled =>
        this.stopAndRemoveEffects(_ == breath, AfterExpression)

      case _ =>
      // Do nothing since no changes
    }
  }

  def updateMicLipSyncWeight(weight: Int): Unit = {
    this.lipSyncFromMicHolder.foreach(_.weight = weight / 10.0f)
  }

  def enableMicLipSync(mixer: Mixer, weight: Int, forceEvenNoSetting: Boolean): Unit = {
    disableMicLipSync()
    val lipSyncFromMic = LipSyncFromMic(avatar.avatarSettings, mixer, weight / 10.0f, forceEvenNoSetting)
    lipSyncFromMic.failed.foreach(_.printStackTrace())
    lipSyncFromMic.foreach(effect => this.appendAndStartEffects(effect :: Nil, AfterExpression))
    this.lipSyncFromMicHolder = lipSyncFromMic.toOption
  }

  def disableMicLipSync(): Unit = {
    for {
      lipSyncFromMic <- lipSyncFromMicHolder
    } {
      this.stopAndRemoveEffects(_ == lipSyncFromMic, AfterExpression)
    }
  }

  def isLipSyncFromMotionEnabled: Boolean = this.findEffects(_ == lipSyncFromMotionSound, AfterExpression).nonEmpty

  def enableLipSyncFromMotion(isEnabled: Boolean): Unit = {
    isEnabled match {
      case true if !isLipSyncFromMotionEnabled =>
        this.appendAndStartEffects(lipSyncFromMotionSound :: Nil, AfterExpression)

      case false if isLipSyncFromMotionEnabled =>
        this.stopAndRemoveEffects(_ == lipSyncFromMotionSound, AfterExpression)

      case _ =>
      // Do nothing since no changes
    }
  }

  def updateLipSyncFromMotionVolume(volume: Int): Unit = {
    this.lipSyncFromMotionSound.volume = volume
  }

  def updateLipSyncFromMotionWeight(weight: Int): Unit = {
    this.lipSyncFromMotionSound.weight = weight / 10.0f
  }

  def isFaceDirectionEnabled: Boolean = this.findEffects(_ == faceDirection, AfterExpression).nonEmpty

  def enableFaceDirection(isEnabled: Boolean): Unit = {
    isEnabled match {
      case true if !isFaceDirectionEnabled =>
        this.appendAndStartEffects(faceDirection :: Nil, AfterExpression)

      case false if isFaceDirectionEnabled =>
        this.stopAndRemoveEffects(_ == faceDirection, AfterExpression)

      case _ =>
      // Do nothing since no changes
    }
  }

  def isEyeBlinkEnabled: Boolean = this.findEffects(_ == eyeBlink, BeforeExpression).nonEmpty

  def enableEyeBlink(isEnabled: Boolean): Unit = {
    isEnabled match {
      case true if !isEyeBlinkEnabled =>
        this.appendAndStartEffects(eyeBlink :: Nil, BeforeExpression)

      case false if isEyeBlinkEnabled =>
        this.stopAndRemoveEffects(_ == eyeBlink, BeforeExpression)

      case _ =>
        // Do nothing since no changes
    }
  }
}


