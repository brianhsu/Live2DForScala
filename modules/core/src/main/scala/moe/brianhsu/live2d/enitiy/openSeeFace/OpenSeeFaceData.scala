package moe.brianhsu.live2d.enitiy.openSeeFace

import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.{EyeOpenness, Features, Point, Point3D, Quaternion, Resolution}

object OpenSeeFaceData {
  case class Point(x: Float, y: Float)
  case class Point3D(x: Float, y: Float, z: Float)

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
  rawEuler: Point3D,
  translation: Point3D,
  confidence: List[Float],
  landmarks: List[Point],
  points3D: List[Point3D],
  features: Features
) {
  def leftEye: List[Point] = landmarks(42) :: landmarks(43) :: landmarks(44) :: landmarks(45) :: landmarks(46) :: landmarks(47) :: Nil
  def rightEye: List[Point] = landmarks(36) :: landmarks(37) :: landmarks(38) :: landmarks(39) :: landmarks(40) :: landmarks(41) :: Nil
  def noseVertical: List[Point] = landmarks(27) :: landmarks(28) :: landmarks(29) :: landmarks(30) :: Nil
  def noseHorizontal: List[Point] = landmarks(31) :: landmarks(32) :: landmarks(33) :: landmarks(34) :: landmarks(35) :: Nil
  def upperLipTop: List[Point] = landmarks(48) :: landmarks(49) :: landmarks(50) :: landmarks(51) :: landmarks(52) :: Nil
  def upperLipBottom: List[Point] = landmarks(59) :: landmarks(60) :: landmarks(61) :: Nil
  def lowerLipTop: List[Point] = landmarks(65) :: landmarks(64) :: landmarks(63) :: Nil

  def leftEdge: List[Point] = landmarks(9) :: landmarks(10) :: landmarks(11) :: landmarks(12) :: landmarks(13) :: landmarks(14) :: landmarks(15) :: landmarks(16) :: Nil
  def rightEdge: List[Point] = landmarks(0) :: landmarks(1) :: landmarks(2) :: landmarks(3) :: landmarks(4) :: landmarks(5) :: landmarks(6) :: landmarks(7) :: Nil
  def lipRight: Point = landmarks(58)
  def lipLeft: Point = landmarks(62)

}
