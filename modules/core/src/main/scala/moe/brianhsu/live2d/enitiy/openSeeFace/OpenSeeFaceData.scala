package moe.brianhsu.live2d.enitiy.openSeeFace

import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.{EyeOpenness, Features, Quaternion, Resolution}

object OpenSeeFaceData {
  case class Quaternion[T](x: T, y: T, z: T, w: T)
  case class Resolution(width: Float, height: Float)
  case class EyeOpenness(right: Float, left: Float)
  case class Features(
    eyeLeft: Float, eyeRight: Float,
    eyebrowSteepnessLeft: Float,
    eyebrowUpDownLeft: Float,
    eyebrowQuirkLeft: Float,
    eyebrowSteepnessRight: Float,
    eyebrowUpDownRight: Float,
    eyebrowQuirkRight: Float,
    mouthCornerUpDownLeft: Float,
    mouthCornerInOutLeft: Float,
    mouthCornerUpDownRight: Float,
    mouthCornerInOutRight: Float,
    mouthOpen: Float,
    mouthWide: Float
  )
}

case class OpenSeeFaceData(
  time: Double, id: Int,
  resolution: Resolution, eyeOpenness: EyeOpenness,
  got3DPoints: Boolean, fit3DError: Float,
  rawQuaternion: Quaternion[Float],
  rawEuler: (Float, Float, Float),
  translation: (Float, Float, Float),
  confidence: List[Float],
  landmarks: List[(Float, Float)],
  points3D: List[(Float, Float, Float)],
  features: Features
)
