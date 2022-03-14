package moe.brianhsu.porting.live2d.framework

import CubismMotion.{CubismMotionSegmentType_Bezier, CubismMotionSegmentType_InverseStepped, CubismMotionSegmentType_Linear, CubismMotionSegmentType_Stepped}
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.porting.live2d.framework.CubismMotionSegment.{BezierEvaluate, BezierEvaluateCardanoInterpretation, InverseSteppedEvaluate, LinearEvaluate, SteppedEvaluate}

object CubismMotionData {
  def apply(motion: MotionSetting): CubismMotionData = {
    val meta = motion.meta
    val curveCount = meta.curveCount
    val segmentsTotalCount = meta.totalSegmentCount
    val pointCount = meta.totalPointCount
    val curves: Array[CubismMotionCurve] = Array.fill(curveCount)(CubismMotionCurve(null, null))
    val segments: Array[CubismMotionSegment] = Array.fill(segmentsTotalCount)(CubismMotionSegment(null))
    val points: Array[CubismMotionPoint] = Array.fill(pointCount)(CubismMotionPoint(0, 0))

    var totalPointCount = 0
    var totalSegmentCount = 0

    for (i <- curves.indices) {
      val curveJson = motion.curves(i)
      curves(i).targetType = CubismMotionCurveTarget.TargetType(curveJson.target)
      curves(i).id = curveJson.id
      curves(i).baseSegmentIndex = totalSegmentCount
      curves(i).fadeInTime = curveJson.fadeInTime.getOrElse(-1.0f)
      curves(i).fadeOutTime = curveJson.fadeOutTime.getOrElse(-1.0f)

      // Segments
      var segmentPosition = 0
      while (segmentPosition < curveJson.segments.size) {
        if (segmentPosition == 0) {
          segments(totalSegmentCount).BasePointIndex = totalPointCount

          points(totalPointCount) = CubismMotionPoint(
            curveJson.segments(segmentPosition),
            curveJson.segments(segmentPosition + 1)
          )

          totalPointCount += 1
          segmentPosition += 2
        } else {
          segments(totalSegmentCount).BasePointIndex = totalPointCount - 1
        }

        val segment: Int = curveJson.segments(segmentPosition).toInt

        segment match {
          case CubismMotionSegmentType_Linear =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_Linear
            segments(totalSegmentCount).Evaluate = LinearEvaluate

            points(totalPointCount) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_Bezier =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_Bezier
            segments(totalSegmentCount).Evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation

            points(totalPointCount) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )
            points(totalPointCount + 1) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 3),
              curveJson.segments(segmentPosition + 4)
            )
            points(totalPointCount + 2) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 5),
              curveJson.segments(segmentPosition + 6)
            )

            totalPointCount += 3
            segmentPosition += 7
          case CubismMotionSegmentType_Stepped =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_Stepped
            segments(totalSegmentCount).Evaluate = SteppedEvaluate

            points(totalPointCount) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_InverseStepped =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_InverseStepped
            segments(totalSegmentCount).Evaluate = InverseSteppedEvaluate

            points(totalPointCount) = CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case _ =>
        }
        curves(i).segmentCount += 1
        totalSegmentCount += 1
      }
    }

    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))

    new CubismMotionData(
      curves.toList, segments.toList,
      points.toList, events,
      meta.duration, meta.loop,
      curveCount, meta.fps
    )
  }

}

case class CubismMotionData(curvesList: List[CubismMotionCurve],
                            segmentsList: List[CubismMotionSegment],
                            pointsList: List[CubismMotionPoint],
                            eventsList: List[MotionEvent],
                            duration: Float = 0.0f,
                            isLoop: Boolean = false,
                            curveCount: Int = 0,
                            fps: Float = 0.0f) {

  lazy val curves = curvesList.toArray
  lazy val segments = segmentsList.toArray
  lazy val points = pointsList.toArray
  lazy val events = eventsList.toArray
}
