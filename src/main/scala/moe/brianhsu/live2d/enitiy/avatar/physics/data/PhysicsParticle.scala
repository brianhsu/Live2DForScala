package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class PhysicsParticle(mobility: Float,
                           delay: Float,
                           acceleration: Float,
                           radius: Float,
                           initialPosition: EuclideanVector,
                           position: EuclideanVector,
                           lastPosition: EuclideanVector,
                           lastGravity: EuclideanVector,
                           velocity: EuclideanVector)
