package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.PoseReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.PosePart
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Part

/**
 * Reader to read Pose effect from [[moe.brianhsu.live2d.enitiy.avatar.settings.Settings]]
 *
 * @param avatarSettings The avatar settings from Live2D model folder.
 */
class AvatarPoseReader(avatarSettings: Settings) extends PoseReader {

  private val DefaultFadeInSeconds = 0.5f

  /**
   * Create PartData list for Pose effect.
   *
   * @param   pose  The Part setting from avatar settings.
   * @return        The transformed PartData will be used to create Pose effect.
   */
  private def createPartData(pose: List[Part]): List[PosePart] = {
    pose.map { partInfo =>
      PosePart(
        partInfo.id,
        partInfo.link.map(id => PosePart(id))
      )
    }
  }

  override def loadPose: Option[Pose] = {
    avatarSettings.pose.map { poseSettings =>
      val posePartGroups = poseSettings.groups.map(createPartData)
      val fadeTimeInSeconds = poseSettings.fadeInTime.filterNot(_ < 0).getOrElse(DefaultFadeInSeconds)
      Pose(posePartGroups, fadeTimeInSeconds)
    }
  }
}
