package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class PhysicsData(effects: List[PhysicsEffect],
                       gravity: EuclideanVector,
                       wind: EuclideanVector)
