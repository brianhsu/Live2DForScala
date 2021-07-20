package moe.brianhsu.live2d.framework

case class CubismMotionData(
  Curves: List[CubismMotionCurve],
  Segments: List[CubismMotionSegment],
  Points: List[CubismMotionPoint],
  Events: List[CubismMotionEvent],
  Duration: Float = 0.0f,
  Loop: Int = 0,
  CurveCount: Int = 0,
  EventCount: Int = 0,
  Fps: Float = 0.0f
)
