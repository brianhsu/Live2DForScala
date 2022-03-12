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
    val eventCount = meta.userDataCount
    val curves: Array[CubismMotionCurve] = Array.fill(curveCount)(CubismMotionCurve(null, null))
    val segments: Array[CubismMotionSegment] = Array.fill(segmentsTotalCount)(CubismMotionSegment(null))
    val points: Array[CubismMotionPoint] = Array.fill(pointCount)(CubismMotionPoint(0, 0))
    val events: Array[MotionEvent] = Array.fill(eventCount)(null)

    var totalPointCount = 0
    var totalSegmentCount = 0

    for (i <- curves.indices) {
      val curveJson = motion.curves(i)
      curves(i).Type = CubismMotionCurveTarget.TargetType(curveJson.target)
      curves(i).Id = curveJson.id
      curves(i).BaseSegmentIndex = totalSegmentCount
      curves(i).FadeInTime = curveJson.fadeInTime.getOrElse(-1.0f)
      curves(i).FadeOutTime = curveJson.fadeOutTime.getOrElse(-1.0f)

      // Segments
      var segmentPosition = 0
      while (segmentPosition < curveJson.segments.size) {
        if (segmentPosition == 0) {
          segments(totalSegmentCount).BasePointIndex = totalPointCount

          points(totalPointCount).Time = curveJson.segments(segmentPosition)
          points(totalPointCount).Value = curveJson.segments(segmentPosition + 1)

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

            points(totalPointCount).Time = curveJson.segments(segmentPosition + 1)
            points(totalPointCount).Value = curveJson.segments(segmentPosition + 2)

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_Bezier =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_Bezier
            segments(totalSegmentCount).Evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation

            points(totalPointCount).Time = curveJson.segments(segmentPosition + 1)
            points(totalPointCount).Value = curveJson.segments(segmentPosition + 2)

            points(totalPointCount + 1).Time = curveJson.segments(segmentPosition + 3)
            points(totalPointCount + 1).Value = curveJson.segments(segmentPosition + 4)

            points(totalPointCount + 2).Time = curveJson.segments(segmentPosition + 5)
            points(totalPointCount + 2).Value = curveJson.segments(segmentPosition + 6)

            totalPointCount += 3
            segmentPosition += 7
          case CubismMotionSegmentType_Stepped =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_Stepped
            segments(totalSegmentCount).Evaluate = SteppedEvaluate

            points(totalPointCount).Time = curveJson.segments(segmentPosition + 1)
            points(totalPointCount).Value = curveJson.segments(segmentPosition + 2)

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_InverseStepped =>
            segments(totalSegmentCount).SegmentType = CubismMotionSegmentType_InverseStepped
            segments(totalSegmentCount).Evaluate = InverseSteppedEvaluate

            points(totalPointCount).Time = curveJson.segments(segmentPosition + 1)
            points(totalPointCount).Value = curveJson.segments(segmentPosition + 2)

            totalPointCount += 1
            segmentPosition += 3

          case _ =>
        }
        curves(i).SegmentCount += 1
        totalSegmentCount += 1
      }
    }

    for (i <- events.indices) {
      events(i) = MotionEvent(motion.userData(i).value, motion.userData(i).time)
    }

    new CubismMotionData(
      curves.toList, segments.toList,
      points.toList, events.toList,
      meta.duration, meta.loop,
      curveCount, meta.userDataCount,
      meta.fps
    )
  }
}

case class CubismMotionData(
                             CurvesList: List[CubismMotionCurve],
                             SegmentsList: List[CubismMotionSegment],
                             PointsList: List[CubismMotionPoint],
                             EventsList: List[MotionEvent],
                             Duration: Float = 0.0f,
                             Loop: Boolean = false,
                             CurveCount: Int = 0,
                             EventCount: Int = 0,
                             Fps: Float = 0.0f
) {
  lazy val Curves = CurvesList.toArray
  lazy val Segments = SegmentsList.toArray
  lazy val Points = PointsList.toArray
  lazy val Events = EventsList.toArray
}
