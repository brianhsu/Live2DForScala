package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsSubRig
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class PhysicData(settings: List[CubismPhysicsSubRig],
                      gravity: EuclideanVector,
                      wind: EuclideanVector)
