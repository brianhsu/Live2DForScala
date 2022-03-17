package moe.brianhsu.live2d.adapter.gateway.avatar.motion

import moe.brianhsu.live2d.boundary.gateway.avatar.motion.MotionDataReader
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{CurveTarget, MotionCurve, MotionData, MotionPoint, MotionSegment, SegmentType}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.SegmentType.{Bezier, BezierCardanoInterpretation, InverseStepped, Linear, Stepped}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting

class AvatarMotionDataReader(motion: MotionSetting) extends MotionDataReader {

  case class ParsedSegmentInfo(segment: MotionSegment, offset: Int)

  override def loadMotionData(): MotionData = {
    val meta = motion.meta
    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))
    var reversedCurve: List[MotionCurve] = Nil
    var points: List[MotionPoint] = Nil
    var segments: List[MotionSegment] = Nil

    for (curveJson <- motion.curves) {
      val segmentsInCurve = parseSegmentsAndPoints(
        meta.areBeziersRestricted,
        curveJson.segments
      )

      reversedCurve ::= MotionCurve(curveJson.id, CurveTarget(curveJson.target), curveJson.fadeInTime, curveJson.fadeOutTime, segmentsInCurve)
      segments = segments ++ segmentsInCurve
    }

    MotionData(reversedCurve.reverse, events, meta.duration, meta.loop, meta.curveCount, meta.fps)
  }

  private def parseSegmentsAndPoints(areBeziersRestricted: Boolean, segments: List[Float]): List[MotionSegment] = {

    var remainingSegments = segments
    var segmentsInCurve: List[MotionSegment] = Nil
    var initPoint: List[MotionPoint] = Nil

    if (remainingSegments.nonEmpty) {
      initPoint = List(MotionPoint(remainingSegments(0), remainingSegments(1)))
      remainingSegments = remainingSegments.drop(2)
    }


    var lastPoint = initPoint
    while (remainingSegments.nonEmpty) {
      val segmentType = SegmentType(remainingSegments.head.toInt, areBeziersRestricted)

      val ParsedSegmentInfo(segment, offset) = segmentType match {
        case Linear => parse2Points(lastPoint, remainingSegments, Linear)
        case Bezier => parse3Points(lastPoint, remainingSegments, Bezier)
        case BezierCardanoInterpretation => parse3Points(lastPoint, remainingSegments, BezierCardanoInterpretation)
        case Stepped => parse2Points(lastPoint, remainingSegments, Stepped)
        case InverseStepped => parse2Points(lastPoint, remainingSegments, InverseStepped)
      }
      lastPoint = segment.points.reverse.headOption.toList
      segmentsInCurve ::= segment
      remainingSegments = remainingSegments.drop(offset)
    }

    segmentsInCurve.reverse
  }

  private def parse2Points(initPoint: List[MotionPoint], remainSegments: List[Float], segmentType: SegmentType) = {
    val points = List(MotionPoint(remainSegments(1), remainSegments(2)))
    val segment = MotionSegment(segmentType, initPoint ++ points)
    ParsedSegmentInfo(segment, 3)
  }

  private def parse3Points(initPoint: List[MotionPoint], remainSegments: List[Float], segmentType: SegmentType): ParsedSegmentInfo = {
    val points = List(
      MotionPoint(remainSegments(1), remainSegments(2)),
      MotionPoint(remainSegments(3), remainSegments(4)),
      MotionPoint(remainSegments(5), remainSegments(6))
    )
    val segment = MotionSegment(segmentType, initPoint ++ points)
    ParsedSegmentInfo(segment, 7)
  }
}
