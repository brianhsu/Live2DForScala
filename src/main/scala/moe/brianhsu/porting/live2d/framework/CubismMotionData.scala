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
    var reversedPoints: List[CubismMotionPoint] = Nil
    var reversedSegments: List[CubismMotionSegment] = Nil
    var totalPointCount = 0
    var totalSegmentCount = 0

    for (i <- curves.indices) {
      val curveJson = motion.curves(i)
      curves(i).targetType = CubismMotionCurveTarget.TargetType(curveJson.target)
      curves(i).id = curveJson.id
      curves(i).baseSegmentIndex = totalSegmentCount
      curves(i).fadeInTime = curveJson.fadeInTime.getOrElse(-1.0f)
      curves(i).fadeOutTime = curveJson.fadeOutTime.getOrElse(-1.0f)
      println(s"====${curves(i).id}===========")

      // Segments
      var segmentPosition = 0
      while (segmentPosition < curveJson.segments.size) {
        println("  ==> segmentSize:" + curveJson.segments.size)
        println("  ==> segmentPosition:" + segmentPosition)

        val basePointIndex = if (segmentPosition == 0) totalPointCount else totalPointCount -1
        if (segmentPosition == 0) {
          reversedPoints ::= CubismMotionPoint(
            curveJson.segments(segmentPosition),
            curveJson.segments(segmentPosition + 1)
          )
          totalPointCount += 1
          segmentPosition += 2
        }

        val segmentType: Int = curveJson.segments(segmentPosition).toInt

        segmentType match {
          case CubismMotionSegmentType_Linear =>
            reversedSegments ::= new CubismMotionSegment(LinearEvaluate, basePointIndex, CubismMotionSegmentType_Linear)

            //println("CubismMotionSegmentType_Linear.segmentPosition:" + segmentPosition)
            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_Bezier =>
            val evaluate = if (meta.areBeziersRestricted) BezierEvaluate else BezierEvaluateCardanoInterpretation
            reversedSegments ::= new CubismMotionSegment(evaluate, basePointIndex, CubismMotionSegmentType_Bezier)
            //println("CubismMotionSegmentType_Bezier.segmentPosition:" + segmentPosition)

            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )
            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 3),
              curveJson.segments(segmentPosition + 4)
            )
            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 5),
              curveJson.segments(segmentPosition + 6)
            )
            totalPointCount += 3
            segmentPosition += 7
          case CubismMotionSegmentType_Stepped =>
            reversedSegments ::= new CubismMotionSegment(SteppedEvaluate, basePointIndex, CubismMotionSegmentType_Stepped)

            //println("CubismMotionSegmentType_Stepped.segmentPosition:" + segmentPosition)

            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case CubismMotionSegmentType_InverseStepped =>
            reversedSegments ::= new CubismMotionSegment(InverseSteppedEvaluate, basePointIndex, CubismMotionSegmentType_InverseStepped)
            //println("CubismMotionSegmentType_InverseStepped.segmentPosition:" + segmentPosition)

            reversedPoints ::= CubismMotionPoint(
              curveJson.segments(segmentPosition + 1),
              curveJson.segments(segmentPosition + 2)
            )

            totalPointCount += 1
            segmentPosition += 3

          case _ => println("Do nothing")
        }
        //println("  ==> segmentPosition.new:" + segmentPosition)

        //println("segmentCount++")
        curves(i).segmentCount += 1
        totalSegmentCount += 1
      }

      println(curves(i).segmentCount)
      println(curveJson.segments.size)

      println("===============")
    }

    val events = motion.userData.map(userData => MotionEvent(userData.value, userData.time))

    new CubismMotionData(
      curves.toList, reversedSegments.reverse,
      reversedPoints.reverse, events,
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
