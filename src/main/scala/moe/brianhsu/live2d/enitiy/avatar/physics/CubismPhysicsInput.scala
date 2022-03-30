package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.porting.live2d.physics.{CubismPhysicsParameter, NormalizedPhysicsParameterValueGetter}

case class CubismPhysicsInput(
  source: CubismPhysicsParameter, weight: Float,
  sourceType: CubismPhysicsType, isReflect: Boolean,
  getNormalizedParameterValue: NormalizedPhysicsParameterValueGetter
)
