package moe.brianhsu.live2d.adapter.gateway.avatar.motion

import moe.brianhsu.live2d.boundary.gateway.avatar.motion.MotionDataReader
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{CurveTarget, MotionCurve, MotionData, MotionPoint, MotionSegment, SegmentType}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.SegmentType.{Bezier, BezierCardanoInterpretation, InverseStepped, Linear, Stepped}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting

class AvatarMotionDataReader(motion: MotionSetting) extends MotionDataReader {

  case class ParsedSegmentInfo(segment: MotionSegment, points: List[MotionPoint], offset: Int)

  override def loadMotionData(): MotionData = {
    val meta = motion.meta
    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))
    var reversedCurve: List[MotionCurve] = Nil
    var points: List[MotionPoint] = Nil
    var segments: List[MotionSegment] = Nil

    for (curveJson <- motion.curves) {
      val baseSegmentIndex = segments.size
      val (segmentsInCurve, pointsInCurve) = parseSegmentsAndPoints(
        meta.areBeziersRestricted,
        curveJson.segments,
        points.size
      )

      reversedCurve ::= MotionCurve(
        curveJson.id,
        CurveTarget(curveJson.target),
        segmentsInCurve.size, baseSegmentIndex,
        curveJson.fadeInTime,
        curveJson.fadeOutTime
      )

      points = points ++ pointsInCurve
      segments = segments ++ segmentsInCurve
    }

    MotionData(
      reversedCurve.reverse, segments,
      points, events,
      meta.duration, meta.loop,
      meta.curveCount, meta.fps
    )
  }

  private def parseSegmentsAndPoints(areBeziersRestricted: Boolean, segments: List[Float],
                             currentPointSize: Int): (List[MotionSegment], List[MotionPoint]) = {

    var remainingSegments = segments
    var segmentsInCurve: List[MotionSegment] = Nil
    var allPoints: List[MotionPoint] = Nil

    if (remainingSegments.nonEmpty) {
      allPoints = allPoints.appended(MotionPoint(remainingSegments(0), remainingSegments(1)))
      remainingSegments = remainingSegments.drop(2)
    }

    while (remainingSegments.nonEmpty) {
      val basePointIndex = currentPointSize + allPoints.size - 1

      val segmentType = SegmentType(remainingSegments.head.toInt, areBeziersRestricted)

      val ParsedSegmentInfo(segment, remainPoints, offset) = segmentType match {
        case Linear => parse2Points(remainingSegments, Linear, basePointIndex)
        case Bezier => parse3Points(remainingSegments, Bezier, basePointIndex)
        case BezierCardanoInterpretation => parse3Points(remainingSegments, BezierCardanoInterpretation, basePointIndex)
        case Stepped => parse2Points(remainingSegments, Stepped, basePointIndex)
        case InverseStepped => parse2Points(remainingSegments, InverseStepped, basePointIndex)
      }

      allPoints = allPoints ++ remainPoints
      segmentsInCurve ::= segment

      remainingSegments = remainingSegments.drop(offset)
    }

    (segmentsInCurve.reverse, allPoints)

  }

  private def parse2Points(remainSegments: List[Float], segmentType: SegmentType, basePointIndex: Int) = {
    val segment = MotionSegment(segmentType, basePointIndex)
    val points = List(MotionPoint(remainSegments(1), remainSegments(2)))
    ParsedSegmentInfo(segment, points, 3)
  }

  private def parse3Points(remainSegments: List[Float], segmentType: SegmentType, basePointIndex: Int): ParsedSegmentInfo = {
    val segment = MotionSegment(segmentType, basePointIndex)
    val points = List(
      MotionPoint(remainSegments(1), remainSegments(2)),
      MotionPoint(remainSegments(3), remainSegments(4)),
      MotionPoint(remainSegments(5), remainSegments(6))
    )
    ParsedSegmentInfo(segment, points, 7)
  }
}
