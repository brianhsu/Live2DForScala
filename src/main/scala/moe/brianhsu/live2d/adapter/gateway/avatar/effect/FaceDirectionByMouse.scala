package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator

import scala.math.{abs, sqrt}

class FaceDirectionByMouse(frameRate: Int) extends FaceDirectionCalculator {

  /**
   * The minimal offset required to calculate a face direction coordinate
   */
  private val Epsilon: Float = 0.01f

  /**
   * The X coordinate of face direction target. (Approximately)
   *
   * Original Japanese comment in Cubism Live2D SDK:
   *
   * {{{
   *     顔の向きのX目標値 (この値に近づいていく)
   * }}}
   *
   */
  private var faceTargetX: Float = 0.0f

  /**
   * The Y coordinate of face direction target. (Approximately)
   *
   * Original Japanese comment in Cubism Live2D SDK:
   *
   * {{{
   *     顔の向きのY目標値 (この値に近づいていく)
   * }}}
   *
   */
  private var faceTargetY: Float = 0.0f

  /**
   * The X coordinate of face direction. (-1.0 - 1.0)
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   顔の向きX (-1.0 - 1.0)
   * }}}
   */
  private var faceX: Float = 0.0f

  /**
   * The Y coordinate of face direction. (-1.0 - 1.0)
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   顔の向きY (-1.0 - 1.0)
   * }}}
   */
  private var faceY: Float = 0.0f

  /**
   * The velocity of face direction X.
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   顔の向きの変化速度X
   * }}}
   */
  private var faceVX: Float = 0.0f

  /**
   * The velocity of face direction Y.
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   顔の向きの変化速度Y
   * }}}
   */
  private var faceVY: Float = 0.0f
  private var isFirstFrame: Boolean = true

  override def updateFrameTimeInfo(totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): Unit = {


    if (this.isFirstFrame) {
      this.isFirstFrame = false
    } else {
      updateFaceCoordinates(deltaTimeInSeconds)
    }

  }


  /**
   * Update face coordinates.
   *
   * @param deltaTimeInSeconds  The elapsed time since last update.
   */
  private def updateFaceCoordinates(deltaTimeInSeconds: Float): Unit = {
    val (dx: Float, dy: Float) = calculateTargetDirectionVector()

    // There is no change.
    if (abs(dx) >= Epsilon || abs(dy) >= Epsilon) {
      val maxVelocity: Float = calculateMaxVelocity()
      val maxAcceleration: Float = calculateMaxAcceleration(maxVelocity, deltaTimeInSeconds)
      // 速度の最大よりも大きい場合は、速度を落とす
      val d: Float = sqrt((dx * dx) + (dy * dy)).toFloat
      var (ax: Float, ay: Float) = calculateNewAcceleration(maxVelocity, maxAcceleration, dx, dy, d)

      // 加速度を元の速度に足して、新速度とする
      this.faceVX += ax
      this.faceVY += ay

      val slowDownScalar = decelerateWhenApproaching(maxAcceleration, d, faceVX, faceVY)

      this.faceVX *= slowDownScalar
      this.faceVY *= slowDownScalar

      this.faceX += faceVX
      this.faceY += faceVY
    }
  }

  /**
   * Decelerate smoothly when approaching the desired direction
   *
   * Calculate the maximum speed that can be taken at present from the relationship between
   * the distance that can be stopped at the set acceleration and the speed,
   * and if it is higher than that, slow down
   *
   * Originally, humans can adjust the force (acceleration) with muscle strength,
   * so it has a higher degree of freedom, but this is a simplified process.
   *
   * Original Japanese comment:
   * {{{
   *   目的の方向に近づいたとき、滑らかに減速するための処理
   *   設定された加速度で止まることのできる距離と速度の関係から
   *   現在とりうる最高速度を計算し、それ以上のときは速度を落とす
   *
   *   ※本来、人間は筋力で力（加速度）を調整できるため、より自由度が高いが、簡単な処理ですませている
   * }}}
   *
   * @return The scalar to slow down.
   */
  private def decelerateWhenApproaching(maxAcceleration: Float, d: Float, faceVX: Float, faceVY: Float): Float = {
    {
      // 加速度、速度、距離の関係式。
      //            2  6           2               3
      //      sqrt(a  t  + 16 a h t  - 8 a h) - a t
      // v = --------------------------------------
      //                    2
      //                 4 t  - 2
      // (t=1)
      //  時刻tは、あらかじめ加速度、速度を1/60(フレームレート、単位なし)で
      //  考えているので、t＝１として消してよい（※未検証）
      val maxVelocity: Float = 0.5f * (sqrt((maxAcceleration * maxAcceleration) + 16.0f * maxAcceleration * d - 8.0f * maxAcceleration * d) - maxAcceleration).toFloat
      val currentVelocity: Float = sqrt((faceVX * faceVX) + (faceVY * faceVY)).toFloat

      if (currentVelocity > maxVelocity) {
        // 現在の速度 > 最高速度のとき、最高速度まで減速
        maxVelocity / currentVelocity
      } else {
        1
      }
    }
  }

  private def calculateNewAcceleration(maxVelocity: Float, maxAcceleration: Float, dx: Float, dy: Float, d: Float) = {
    // 進行方向の最大速度ベクトル
    val vx: Float = maxVelocity * dx / d
    val vy: Float = maxVelocity * dy / d

    // 現在の速度から、新規速度への変化（加速度）を求める
    val ax = vx - faceVX
    val ay = vy - faceVY
    val a: Float = sqrt((ax * ax) + (ay * ay)).toFloat

    // 加速のとき
    if (a < -maxAcceleration || a > maxAcceleration) {
      val scalar = maxAcceleration / a
      (scalar * ax, scalar * ay)
    } else {
      (ax, ay)
    }
  }

  /**
   * Calculate target direction vector.
   *
   * The target direction is a vector in the (dx, dy) direction.
   *
   * @return A (dx, dy) vector indicates the target direction.
   */
  private def calculateTargetDirectionVector(): (Float, Float) = {
    (faceTargetX - faceX, faceTargetY - faceY)
  }

  /**
   * Calculate the max acceleration per frame.
   *
   * @param maxVelocity          The max velocity calculate from [[FaceDirectionByMouse.calculateMaxVelocity]].
   * @param deltaTimeInSeconds   How long the time has elapsed after last update.
   *
   * @return The acceleration per frame.
   */
  private def calculateMaxAcceleration(maxVelocity: Float, deltaTimeInSeconds: Float): Float = {
    // 最高速度になるまでの時間を
    val deltaTimeWeight: Float = deltaTimeInSeconds * frameRate
    val timeToMaxSpeed: Float = 0.15f
    val frameToMaxSpeed: Float = timeToMaxSpeed * frameRate // sec * frame/sec

    deltaTimeWeight * maxVelocity / frameToMaxSpeed // 1frameあたりの加速度
  }

  /**
   * Calculate the max velocity
   *
   * Origin Japanese comments in the Cubism Live2D SDK about the calculation:
   * {{{
   *     首を中央から左右に振るときの平均的な早さは  秒程度。加速・減速を考慮して、その2倍を最高速度とする
   *     顔のふり具合を、中央(0.0)から、左右は(+-1.0)とする
   * }}}
   * Translation using Google Translator:
   *
   * {{{
   *     The average speed when shaking the head from the center to the left and right is
   *     about seconds. Considering acceleration and deceleration, the maximum speed of the
   *     face is twice that, from the center (0.0) to the left and right (+ -1.0).
   * }}}
   *
   * @return Upper limit of speed that can be changed per frame.
   */
  private def calculateMaxVelocity(): Float = {
    // Original comment from Cubism Live2D SDK:
    //     7.5 秒間に 40 分移動（5.3/sc)
    // Translation using Google Translator:
    //     Move 40 minutes in 7.5 seconds (5.3/sec)
    val faceParamMaxV: Float = 40.0f / 10.0f

    // Original comment from Cubism Live2D SDK:
    //     1frameあたりに変化できる速度の上限
    faceParamMaxV * 1.0f / frameRate
  }

  /**
   * The invertTransformX / invertTransformY coordinate calculate from view matrix.
   *
   * User should pass in the invertTransformX / invertTransformY coordinate of view matrix
   * calculate from current mouse position.
   *
   * I'm not really familiar with all these matrix / view port thing and only port
   * and refactor from the origin Cusbism Live2D SDK.
   *
   * So please refer to the [[moe.brianhsu.porting.live2d.demo.LAppView.onMouseDragged]] for detail usage, and figure
   * it out on your own.
   *
   * @param x The invertTransformX coordinate that calculate from current mouse position using viewMatrix.
   * @param y The invertTransformY coordinate that calculate from current mouse position using viewMatrix.
   */
  def setFaceTargetCoordinate(x: Float, y: Float): Unit = {
    this.faceTargetX = x
    this.faceTargetY = y
  }

  def currentFaceCoordinate: (Float, Float) = (faceX, faceY)
}
