package moe.brianhsu.porting.live2d.framework

import CubismMotion.{CubismMotionSegmentType_Bezier, CubismMotionSegmentType_InverseStepped, CubismMotionSegmentType_Linear, CubismMotionSegmentType_Stepped}
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.porting.live2d.framework.CubismMotionSegment.{BezierEvaluate, BezierEvaluateCardanoInterpretation, InverseSteppedEvaluate, LinearEvaluate, SteppedEvaluate}

import scala.collection.mutable.ListBuffer

object CubismMotionData {
  def apply(motion: MotionSetting): CubismMotionData = {
    val meta = motion.meta
    val curveCount = meta.curveCount
    val curves: Array[CubismMotionCurve] = Array.fill(curveCount)(CubismMotionCurve(null, null))
    var points: List[CubismMotionPoint] = Nil
    var segments: List[CubismMotionSegment] = Nil
    var totalPointCount = 0
    var totalSegmentCount = 0

    for (i <- curves.indices) {
      var segmentsInCurve: List[CubismMotionSegment] = Nil
      var pointsInCurve: List[CubismMotionPoint] = Nil
      val curveJson = motion.curves(i)
      val baseSegmentIndex = totalSegmentCount
      println(s"====${curves(i).id}===========")


      // Segments
      var segmentPosition = 0
      while (segmentPosition < curveJson.segments.size) {
        println("  ==> segmentSize:" + curveJson.segments.size)
        println("  ==> segmentPosition:" + segmentPosition)

        val basePointIndex = if (segmentPosition == 0) totalPointCount else totalPointCount -1
        if (segmentPosition == 0) {
          pointsInCurve ::= CubismMotionPoint(
            curveJson.segments(segmentPosition),
            curveJson.segments(segmentPosition + 1)
          )
          totalPointCount += 1
          segmentPosition += 2
        }

        val segmentType: Int = curveJson.segments(segmentPosition).toInt

        segmentType match {
          case CubismMotionSegmentType_Linear =>
            segmentsInCurve ::= new CubismMotionSegment(LinearEvaluate, basePointIndex, CubismMotionSegmentType_Linear)

            //println("CubismMotionSegmentType_Linear.segmentPosition:" + segmentPosition)
            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_Bezier =>
            val evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation
            segmentsInCurve ::= new CubismMotionSegment(evaluate, basePointIndex, CubismMotionSegmentType_Bezier)
            //println("CubismMotionSegmentType_Bezier.segmentPosition:" + segmentPosition)

            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )
            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 3),
              curveJson.segments(segmentPosition + 4)
            )
            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 5),
              curveJson.segments(segmentPosition + 6)
            )
            totalPointCount += 3
            segmentPosition += 7
          case CubismMotionSegmentType_Stepped =>
            segmentsInCurve ::= new CubismMotionSegment(SteppedEvaluate, basePointIndex, CubismMotionSegmentType_Stepped)

            //println("CubismMotionSegmentType_Stepped.segmentPosition:" + segmentPosition)

            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_InverseStepped =>
            segmentsInCurve ::= new CubismMotionSegment(InverseSteppedEvaluate, basePointIndex, CubismMotionSegmentType_InverseStepped)
            //println("CubismMotionSegmentType_InverseStepped.segmentPosition:" + segmentPosition)

            pointsInCurve ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case _ => println("Do nothing")
        }
        totalSegmentCount += 1
      }



      segments = segments ++ segmentsInCurve.reverse
      points = points ++ pointsInCurve.reverse
      println(curves(i).segmentCount)
      println(segmentsInCurve.size)
      curves(i) = CubismMotionCurve(
        curveJson.id, CubismMotionCurveTarget.TargetType(curveJson.target),
        segmentsInCurve.size, baseSegmentIndex,
        curveJson.fadeInTime.getOrElse(-1.0f),
        curveJson.fadeOutTime.getOrElse(-1.0f)
      )
      println("===============")
    }

    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))

    new CubismMotionData(
      curves.toList, segments,
      points, events,
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
