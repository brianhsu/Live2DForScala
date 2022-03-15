package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{Bezier, InverseStepped, Linear, SegmentType, Stepped}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting.Meta
import moe.brianhsu.porting.live2d.framework.CubismMotionSegment.{BezierEvaluate, BezierEvaluateCardanoInterpretation, InverseSteppedEvaluate, LinearEvaluate, SteppedEvaluate}

object CubismMotionData {
  def apply(motion: MotionSetting): CubismMotionData = {
    val meta = motion.meta
    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))
    var reversedCurve: List[CubismMotionCurve] = Nil
    var points: List[CubismMotionPoint] = Nil
    var segments: List[CubismMotionSegment] = Nil

    for (curveJson <- motion.curves) {
      val baseSegmentIndex = segments.size
      val (segmentsInCurve, pointsInCurve) = parseSegmentsAndPoints(meta, curveJson.segments, points.size)

      reversedCurve ::= CubismMotionCurve(
        curveJson.id,
        CubismMotionCurveTarget.TargetType(curveJson.target),
        segmentsInCurve.size, baseSegmentIndex,
        curveJson.fadeInTime.getOrElse(-1.0f),
        curveJson.fadeOutTime.getOrElse(-1.0f)
      )

      points = points ++ pointsInCurve
      segments = segments ++ segmentsInCurve
    }


    new CubismMotionData(
      reversedCurve.reverse, segments,
      points, events,
      meta.duration, meta.loop,
      meta.curveCount, meta.fps
    )
  }

  def parseSegmentsAndPoints(meta: Meta, segments: List[Float],
                             currentPointSize: Int): (List[CubismMotionSegment], List[CubismMotionPoint]) = {

    var remainingSegments = segments
    var segmentsInCurve: List[CubismMotionSegment] = Nil
    var allPoints: List[CubismMotionPoint] = Nil

    if (remainingSegments.nonEmpty) {
      allPoints = allPoints.appended(CubismMotionPoint(remainingSegments(0), remainingSegments(1)))
      remainingSegments = remainingSegments.drop(2)
    }

    while (remainingSegments.nonEmpty) {
      val basePointIndex = currentPointSize + allPoints.size - 1

      val segmentType = SegmentType(remainingSegments.head.toInt)

      val ParsedResult(segment, remainPoints, offset) = segmentType match {
        case Linear => parseLinear(remainingSegments, basePointIndex)
        case Bezier => parseBezier(meta, remainingSegments, basePointIndex)
        case Stepped => parseStepped(remainingSegments, basePointIndex)
        case InverseStepped => parseInverseStepped(remainingSegments, basePointIndex)
      }

      allPoints = allPoints ++ remainPoints
      segmentsInCurve ::= segment

      remainingSegments = remainingSegments.drop(offset)
    }

    (segmentsInCurve.reverse, allPoints)

  }

  case class ParsedResult(segment: CubismMotionSegment, points: List[CubismMotionPoint], offset: Int)

  private def parseLinear(remainSegments: List[Float], basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(LinearEvaluate, basePointIndex, Linear)
    val points = List(
      CubismMotionPoint(
        remainSegments(1),
        remainSegments(2)
      )
    )
    ParsedResult(segment, points, 3)
  }

  private def parseBezier(meta: Meta, remainSegments: List[Float], basePointIndex: Int): ParsedResult = {
    val evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation
    val segment = new CubismMotionSegment(evaluate, basePointIndex, Bezier)
    val points = List(
      CubismMotionPoint(remainSegments(1), remainSegments(2)),
      CubismMotionPoint(remainSegments(3), remainSegments(4)),
      CubismMotionPoint(remainSegments(5), remainSegments(6))
    )
    ParsedResult(segment, points, 7)
  }

  private def parseStepped(remainSegments: List[Float], basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(SteppedEvaluate, basePointIndex, Stepped)
    val points = List(CubismMotionPoint(remainSegments(1), remainSegments(2)))
    ParsedResult(segment, points, 3)
  }

  private def parseInverseStepped(remainSegments: List[Float], basePointIndex: Int): ParsedResult = {
    val segment = new CubismMotionSegment(InverseSteppedEvaluate, basePointIndex, InverseStepped)
    val points = List(CubismMotionPoint(remainSegments(1), remainSegments(2)))
    ParsedResult(segment, points, 3)
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
