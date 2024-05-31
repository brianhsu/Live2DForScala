package moe.brianhsu.live2d.enitiy.avatar.effect.data

import moe.brianhsu.live2d.enitiy.avatar.effect.data.OpenSeeFaceDataConverter._
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.TrackingNode
import moe.brianhsu.live2d.enitiy.math.Radian
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.Point

object OpenSeeFaceDataConverter {
  val DefaultSettings: Settings = Settings(
    mouthNormalThreshold = 0.65f,
    mouthSmileThreshold = 0.85f,
    mouthClosedThreshold = 0.1f,
    mouthOpenThreshold = 0.4f,
    mouthOpenLaughCorrection = 0.2f,
    faceYAngleXRotCorrection = 0.15f,
    faceYAngleSmileCorrection = 0.075f,
    faceYAngleZeroValue = 1.8f,
    faceYAngleDownThreshold = 2.3f,
    faceYAngleUpThreshold = 1.3f,
    eyeClosedThreshold = 0.18f,
    eyeOpenThreshold = 0.21f,
    eyeSmileEyeOpenThreshold = 0.6f,
    eyeSmileMouthFormThreshold = 0.75f,
    eyeSmileMouthOpenThreshold = 0.5f,
    faceYAngleCorrection = 2.3f
  )

  case class Settings(
    mouthNormalThreshold: Float,
    mouthSmileThreshold: Float,
    mouthClosedThreshold: Float,
    mouthOpenThreshold: Float,
    mouthOpenLaughCorrection: Float,
    faceYAngleXRotCorrection: Float,
    faceYAngleSmileCorrection: Float,
    faceYAngleZeroValue: Float,
    faceYAngleDownThreshold: Float,
    faceYAngleUpThreshold: Float,
    eyeClosedThreshold: Float,
    eyeOpenThreshold: Float,
    eyeSmileEyeOpenThreshold: Float,
    eyeSmileMouthFormThreshold: Float,
    eyeSmileMouthOpenThreshold: Float,
    faceYAngleCorrection: Float
  )
}

class OpenSeeFaceDataConverter(settings: Settings = DefaultSettings) {

  def convert(currentData: OpenSeeFaceData,
              previousLeftEyeNodes: List[TrackingNode],
              previousRightEyeNodes: List[TrackingNode]): TrackingNode = {

    val faceXAngle = calcFaceXAngle(currentData)
    val mouthForm = calcMouthForm(currentData)
    val mouthOpenness = calcMouthOpenness(currentData, mouthForm)
    val faceYAngle = calcFaceYAngle(currentData, faceXAngle, mouthForm)
    val faceZAngle = calcFaceZAngle(currentData)
    val leftEyeOpenness = calcEyeOpenness(currentData.leftEye, faceYAngle)
    val rightEyeOpenness = calcEyeOpenness(currentData.rightEye, faceYAngle)
    val previousLeftEyeOpenness = previousLeftEyeNodes.map(_.leftEyeOpenness)
    val previousRightEyeOpenness = previousRightEyeNodes.map(_.rightEyeOpenness)
    val leftEyeAvg = previousLeftEyeOpenness.appended(leftEyeOpenness).sum / previousLeftEyeOpenness.size + 1
    val rightEyeAvg = previousRightEyeOpenness.appended(rightEyeOpenness).sum / previousRightEyeOpenness.size + 1

    val isEyeSmile = leftEyeAvg <= settings.eyeSmileEyeOpenThreshold &&
      rightEyeAvg <= settings.eyeSmileEyeOpenThreshold &&
      mouthForm > settings.eyeSmileMouthFormThreshold &&
      mouthOpenness > settings.eyeSmileMouthOpenThreshold

    val eyeSmile = if (isEyeSmile) 1.0f else 0.0f

    TrackingNode(
      faceXAngle, faceYAngle, faceZAngle,
      leftEyeOpenness, rightEyeOpenness,
      mouthOpenness, mouthForm,
      eyeSmile, eyeSmile
    )
  }

  private def calcFaceXAngle(currentData: OpenSeeFaceData): Float = {
    // This function will be easier to understand if you refer to the diagram in
    // https://raw.githubusercontent.com/adrianiainlam/facial-landmarks-for-cubism/master/src/faceXAngle.png

    // Construct the y-axis using (1) average of four points on the nose and
    // (2) average of five points on the upper lip.

    val y0 = centroid(currentData.noseVertical)
    val y1 = centroid(currentData.upperLipTop)

    // Now drop a perpendicular from the left and right edges of the face,
    // and calculate the ratio between the lengths of these perpendiculars

    val leftEdgeCentroid = centroid(currentData.leftEdge.takeRight(3))
    val rightEdgeCentroid = centroid(currentData.rightEdge.take(3))

    // Constructing a perpendicular:
    // Join the left/right point and the upper lip. The included angle
    // can now be determined using cosine rule.
    // Then sine of this angle is the perpendicular divided by the newly
    // created line.
    val adj1 = distance(y0, y1)

    val perpRight = calculatePerpendicular(rightEdgeCentroid, y0, y1, adj1)
    val perpLeft = calculatePerpendicular(leftEdgeCentroid, y0, y1, adj1)

    // Model the head as a sphere and look from above.
    val theta = Math.asin((perpRight - perpLeft) / (perpRight + perpLeft)).toFloat

    Radian.radianToDegrees(theta) match {
      case degree if degree < -30 => -30
      case degree if degree >  30 => 30
      case degree => degree
    }
  }

  private def calculatePerpendicular(edgeCentroid: Point, y0: Point, y1: Point, adj1: Float): Float = {
    val opp = distance(edgeCentroid, y0)
    val adj2 = distance(y1, edgeCentroid)
    val angle = solveCosineRuleAngle(opp, adj1, adj2)

    adj2 * Math.sin(angle).toFloat
  }

  private def calcFaceYAngle(currentData: OpenSeeFaceData, faceXAngle: Float, mouthForm: Float) = {
    // Use the nose
    // angle between the two left/right points and the tip
    val noseLeft = currentData.noseHorizontal.last
    val noseRight = currentData.noseHorizontal.head
    val noseTip = currentData.noseVertical.last

    val c = distance(noseRight, noseLeft)
    val a = distance(noseTip, noseRight)
    val b = distance(noseTip, noseLeft)

    val angle = solveCosineRuleAngle(c, a, b)

    // This probably varies a lot from person to person...

    // Best is probably to work out some trigonometry again,
    // but just linear interpolation seems to work ok...

    // Correct for X rotation
    val afterXCorrection = angle * (1 + (Math.abs(faceXAngle) / 30 * settings.faceYAngleXRotCorrection))

    // Correct for smiles / laughs - this increases the angle
    val afterSmileCorrection = afterXCorrection * (1 - mouthForm * settings.faceYAngleSmileCorrection)
    val rawFaceY = if (afterSmileCorrection >= settings.faceYAngleZeroValue) {
      linearScaleFrom0To1(afterSmileCorrection, settings.faceYAngleZeroValue, settings.faceYAngleDownThreshold, clipMin = false, clipMax = false)
    } else {
      (1 - linearScaleFrom0To1(afterSmileCorrection, settings.faceYAngleUpThreshold, settings.faceYAngleZeroValue, clipMin = false, clipMax = false))
    }

    val scalar = if (afterSmileCorrection >= settings.faceYAngleZeroValue) -30 else 30
    scalar * rawFaceY
  }

  private def calcFaceZAngle(currentData: OpenSeeFaceData): Float = {
    // Use average of eyes and nose

    val eyeRight = centroid(currentData.rightEye)
    val eyeLeft  = centroid(currentData.leftEye)

    val noseLeft  = currentData.noseHorizontal.last
    val noseRight = currentData.noseHorizontal.head

    val eyeYDiff = eyeRight.y - eyeLeft.y
    val eyeXDiff = eyeRight.x - eyeLeft.x

    val angle1 = Math.atan(eyeYDiff / eyeXDiff).toFloat

    val noseYDiff = noseRight.y - noseLeft.y
    val noseXDiff = noseRight.x - noseLeft.x

    val angle2 = Math.atan(noseYDiff / noseXDiff).toFloat

    Radian.radianToDegrees((angle1 + angle2) / 2)
  }

  private def calcMouthForm(currentData: OpenSeeFaceData): Float = {
    /* Mouth form parameter: 0 for normal mouth, 1 for fully smiling / laughing.
     * Compare distance between the two corners of the mouth
     * to the distance between the two eyes.
     */

    /* An alternative (my initial attempt) was to compare the corners of
     * the mouth to the top of the upper lip - they almost lie on a
     * straight line when smiling / laughing. But that is only true
     * when facing straight at the camera. When looking up / down,
     * the angle changes. So here we'll use the distance approach instead.
     */

    val rightEyeCentroid = centroid(currentData.rightEye)
    val leftEyeCentroid = centroid(currentData.leftEye)

    val distEyes = distance(rightEyeCentroid, leftEyeCentroid)
    val distMouth = distance(currentData.lipRight, currentData.lipLeft)

    linearScaleFrom0To1(distMouth / distEyes, settings.mouthNormalThreshold, settings.mouthSmileThreshold)
  }

  private def calcMouthOpenness(currentData: OpenSeeFaceData, mouthForm: Float): Float = {
    // Use points for the bottom of the upper lip, and top of the lower lip
    // We have 3 pairs of points available, which give the mouth height
    // on the left, in the middle, and on the right, resp.
    // First let's try to use an average of all three.
    val heightLeft   = distance(currentData.upperLipBottom(2), currentData.lowerLipTop(2))
    val heightMiddle = distance(currentData.upperLipBottom(1), currentData.lowerLipTop(1))
    val heightRight  = distance(currentData.upperLipBottom(0), currentData.lowerLipTop(0))

    val avgHeight = (heightLeft + heightMiddle + heightRight) / 3

    // Now, normalize it with the width of the mouth.
    val width = distance(currentData.lipRight, currentData.lipLeft)

    val normalized = avgHeight / width
    val scaled = linearScaleFrom0To1(normalized, settings.mouthClosedThreshold, settings.mouthOpenThreshold, clipMax = false)

    // Apply correction according to mouthForm
    // Notice that when you smile / laugh, width is increased
    scaled * (1 + settings.mouthOpenLaughCorrection * mouthForm)
  }

  private def calcEyeOpenness(eyeLandmarks: List[Point], faceYAngle: Float): Float = {

    val eyeAspectRatio = calcEyeAspectRatio(eyeLandmarks)

    // Apply correction due to faceYAngle
    val corrEyeAspRat = eyeAspectRatio / Math.cos(Radian.degreesToRadian(faceYAngle)).toFloat

    linearScaleFrom0To1(corrEyeAspRat, settings.eyeClosedThreshold, settings.eyeOpenThreshold)
  }

  private def calcEyeAspectRatio(eyeLandmarks: List[Point]): Float = {
    val eyeWidth = distance(eyeLandmarks(0), eyeLandmarks(3))
    val eyeHeight1 = distance(eyeLandmarks(1), eyeLandmarks(5))
    val eyeHeight2 = distance(eyeLandmarks(2), eyeLandmarks(4))

    (eyeHeight1 + eyeHeight2) / (2 * eyeWidth)
  }

  private def centroid(landmarks: List[Point]): Point = {
    val sumX = landmarks.map(_.x).sum
    val sumY = landmarks.map(_.y).sum

    Point(sumX / landmarks.size, sumY / landmarks.size)
  }

  private def solveCosineRuleAngle(opposite: Float, adjacent1: Float, adjacent2: Float): Float = {
    // c^2 = a^2 + b^2 - 2 a b cos(C)
    val cosC = (opposite * opposite - adjacent1 * adjacent1 - adjacent2 * adjacent2) / (-2 * adjacent1 * adjacent2)
    Math.acos(cosC).toFloat
  }

  private def distance(p1: Point, p2: Point): Float = {
    val xDist = p1.x - p2.x
    val yDist = p1.y - p2.y

    Math.hypot(xDist, yDist).toFloat
  }

  private def linearScaleFrom0To1(value: Float, min: Float, max: Float, clipMin: Boolean = true, clipMax: Boolean = true): Float = {
    value match {
      case _ if value < min && clipMin => 0.0f
      case _ if value > max && clipMax => 1.0f
      case _ => (value - min) / (max - min)
    }
  }

}
