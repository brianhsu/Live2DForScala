package moe.brianhsu.porting.live2d.framework

import CubismMotion.{CubismMotionSegmentType_Bezier, CubismMotionSegmentType_InverseStepped, CubismMotionSegmentType_Linear, CubismMotionSegmentType_Stepped}
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting.Meta
import moe.brianhsu.porting.live2d.framework.CubismMotionSegment.{BezierEvaluate, BezierEvaluateCardanoInterpretation, InverseSteppedEvaluate, LinearEvaluate, SteppedEvaluate}

object CubismMotionData {
  def apply(motion: MotionSetting): CubismMotionData = {
    val meta = motion.meta
    val curveCount = meta.curveCount

    var reversedCurve: List[CubismMotionCurve] = Nil
    var points: List[CubismMotionPoint] = Nil
    var segments: List[CubismMotionSegment] = Nil

    for (i <- 0 until curveCount) {
      var segmentsInCurve: List[CubismMotionSegment] = Nil
      val curveJson = motion.curves(i)
      var segmentPosition = 0
      while (segmentPosition < curveJson.segments.size) {
        var pointsInSegment: List[CubismMotionPoint] = Nil
        val basePointIndex = if (segmentPosition == 0) { points.size } else { points.size - 1}

        if (segmentPosition == 0) {
          pointsInSegment ::= CubismMotionPoint(
            curveJson.segments(segmentPosition),
            curveJson.segments(segmentPosition + 1)
          )
          segmentPosition += 2
        }

        val segmentType: Int = curveJson.segments(segmentPosition).toInt

        val ParsedResult(segment, remainPoints, offset) = segmentType match {
          case CubismMotionSegmentType_Linear => parseLinear(curveJson, segmentPosition, basePointIndex)
          case CubismMotionSegmentType_Bezier => parseBezier(meta, curveJson, segmentPosition, basePointIndex)
          case CubismMotionSegmentType_Stepped => parseStepped(curveJson, segmentPosition, basePointIndex)
          case CubismMotionSegmentType_InverseStepped => parseInverseStepped(curveJson, segmentPosition, basePointIndex)
        }
        segmentPosition += offset

        pointsInSegment = pointsInSegment ++ remainPoints
        segmentsInCurve ::= segment
        points = points ++ pointsInSegment
      }

      reversedCurve ::= CubismMotionCurve(
        curveJson.id, CubismMotionCurveTarget.TargetType(curveJson.target),
        segmentsInCurve.size, segments.size,
        curveJson.fadeInTime.getOrElse(-1.0f),
        curveJson.fadeOutTime.getOrElse(-1.0f)
      )
      segments = segments ++ segmentsInCurve.reverse

    }

    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))

    new CubismMotionData(
      reversedCurve.reverse, segments,
      points, events,
      meta.duration, meta.loop,
      curveCount, meta.fps
    )
  }

  case class ParsedResult(segment: CubismMotionSegment, points: List[CubismMotionPoint], offset: Int)

  private def parseLinear(curveJson: MotionSetting.Curve, segmentPosition: Int,
                           basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(LinearEvaluate, basePointIndex, CubismMotionSegmentType_Linear)
    val points = List(
      CubismMotionPoint(
        curveJson.segments(segmentPosition + 1),
        curveJson.segments(segmentPosition + 2)
      )
    )
    ParsedResult(segment, points, 3)
  }

  private def parseStepped(curveJson: MotionSetting.Curve, segmentPosition: Int,
                           basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(SteppedEvaluate, basePointIndex, CubismMotionSegmentType_Stepped)
    val points = List(CubismMotionPoint(curveJson.segments(segmentPosition + 1), curveJson.segments(segmentPosition + 2)))
    ParsedResult(segment, points, 3)
  }

  private def parseInverseStepped(curveJson: MotionSetting.Curve, segmentPosition: Int,
                            basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(InverseSteppedEvaluate, basePointIndex, CubismMotionSegmentType_InverseStepped)
    val points = List(CubismMotionPoint(curveJson.segments(segmentPosition + 1), curveJson.segments(segmentPosition + 2)))
    ParsedResult(segment, points, 3)
  }

  private def parseBezier(meta: Meta, curveJson: MotionSetting.Curve, segmentPosition: Int,
                           basePointIndex: Int): ParsedResult = {
    val evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation
    val segment = new CubismMotionSegment(evaluate, basePointIndex, CubismMotionSegmentType_Bezier)
    val points = List(
      CubismMotionPoint(curveJson.segments(segmentPosition + 1), curveJson.segments(segmentPosition + 2)),
      CubismMotionPoint(curveJson.segments(segmentPosition + 3), curveJson.segments(segmentPosition + 4)),
      CubismMotionPoint(curveJson.segments(segmentPosition + 5), curveJson.segments(segmentPosition + 6))
    )
    ParsedResult(segment, points, 7)
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
