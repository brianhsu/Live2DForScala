package moe.brianhsu.live2d.framework.effect

import moe.brianhsu.live2d.demo.LAppView

import scala.math.{abs, sqrt}
object FaceDirectionTargetCalculator extends FaceDirectionTargetCalculator(30.0f)

class FaceDirectionTargetCalculator(frameRate: Float) {

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

  /**
   * The last timestamp in seconds that update() method has been called.
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   最後の実行時間[秒]
   * }}}
   */
  private var lastUpdateTimeInSeconds: Float = 0.0f

  /**
   * The total elapsed time in seconds.
   *
   * This is accumulated value from deltaTimeInSeconds of [[update]] method.
   *
   * Original Japanese comment in Cubism Live2D SDK:
   * {{{
   *   デルタ時間の積算値[秒]
   * }}}
   */
  private var totalTimeElapsedInSeconds: Float = 0.0f

  def update(deltaTimeInSeconds: Float): Unit = {

    // デルタ時間を加算する
    totalTimeElapsedInSeconds += deltaTimeInSeconds

    val maxVelocity: Float = calculateMaxVelocity

    if (lastUpdateTimeInSeconds == 0.0f) {
      lastUpdateTimeInSeconds = totalTimeElapsedInSeconds
      return
    }

    val maxAcceleration: Float = calculateMaxAcceleration(maxVelocity, totalTimeElapsedInSeconds, lastUpdateTimeInSeconds)

    lastUpdateTimeInSeconds = totalTimeElapsedInSeconds

    // 目指す向きは、(dx, dy)方向のベクトルとなる
    val dx = faceTargetX - faceX
    val dy = faceTargetY - faceY

    // There is no change.
    if (abs(dx) <= Epsilon && abs(dy) <= Epsilon) {
      return
    }

    // 速度の最大よりも大きい場合は、速度を落とす
    val d: Float = sqrt((dx * dx) + (dy * dy)).toFloat

    // 進行方向の最大速度ベクトル
    val vx: Float = maxVelocity * dx / d
    val vy: Float = maxVelocity * dy / d

    // 現在の速度から、新規速度への変化（加速度）を求める
    var ax = vx - faceVX
    var ay = vy - faceVY

    val a: Float = sqrt((ax * ax) + (ay * ay)).toFloat

    // 加速のとき
    if (a < -maxAcceleration || a > maxAcceleration) {
      ax *= maxAcceleration / a
      ay *= maxAcceleration / a
    }

    // 加速度を元の速度に足して、新速度とする
    faceVX += ax
    faceVY += ay

    // 目的の方向に近づいたとき、滑らかに減速するための処理
    // 設定された加速度で止まることのできる距離と速度の関係から
    // 現在とりうる最高速度を計算し、それ以上のときは速度を落とす
    // ※本来、人間は筋力で力（加速度）を調整できるため、より自由度が高いが、簡単な処理ですませている
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

      val maxV: Float = 0.5f * (sqrt((maxAcceleration * maxAcceleration) + 16.0f * maxAcceleration * d - 8.0f * maxAcceleration * d) - maxAcceleration).toFloat
      val curV: Float = sqrt((faceVX * faceVX) + (faceVY * faceVY)).toFloat

      if (curV > maxV)
      {
        // 現在の速度 > 最高速度のとき、最高速度まで減速
        faceVX *= maxV / curV
        faceVY *= maxV / curV
      }
    }

    faceX += faceVX
    faceY += faceVY
  }


  /**
   * Calculate the max acceleration per frame.
   *
   * @param maxVelocity The max velocity calculate from [[calculateMaxVelocity]].
   * @param totalTimeElapsedInSeconds How long the time has elapsed.
   * @param lastUpdateTimeInSeconds The last timestamp the drawing has been updated.
   * @return The acceleration per frame.
   */
  private def calculateMaxAcceleration(maxVelocity: Float, totalTimeElapsedInSeconds: Float, lastUpdateTimeInSeconds: Float): Float = {
    // 最高速度になるまでの時間を
    val deltaTimeWeight: Float = (totalTimeElapsedInSeconds - lastUpdateTimeInSeconds) * frameRate
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
  private def calculateMaxVelocity: Float = {
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
   * So please refer to the [[LAppView.onMouseDragged]] for detail usage, and figure
   * it out on your own.
   *
   * @param x The invertTransformX coordinate that calculate from current mouse position using viewMatrix.
   * @param y The invertTransformY coordinate that calculate from current mouse position using viewMatrix.
   */
  def setFaceTargetCoordinate(x: Float, y: Float): Unit = {
    this.faceTargetX = x
    this.faceTargetY = y
  }

  /**
   * Get the face direction coordinate.
   *
   * User could use this value to adjust the parameter of Live2D model.
   *
   * Both x / y will between -1.0 to 1.0
   *
   * @return  The (x, y) coordinate of face direction.
   */
  def getFaceCoordinate: (Float, Float) = (faceX, faceY)
}
