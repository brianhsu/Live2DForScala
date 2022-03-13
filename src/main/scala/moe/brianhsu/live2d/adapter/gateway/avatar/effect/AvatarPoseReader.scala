package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.PoseReader
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Part
import moe.brianhsu.porting.live2d.framework.PartData

class AvatarPoseReader(avatarSettings: Settings) extends PoseReader {

  private val DefaultFadeInSeconds = 0.5f

  private def createPartData(pose: List[Part]): List[PartData] = {
    pose.map { partInfo =>
      PartData(
        partInfo.id,
        partInfo.link.map(id => PartData(id))
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
