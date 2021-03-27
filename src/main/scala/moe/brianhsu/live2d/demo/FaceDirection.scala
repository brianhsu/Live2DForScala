package moe.brianhsu.live2d.demo
import scala.math.sqrt

object FaceDirection {
  val FrameRate: Float = 30.0f
  val Epsilon: Float = 0.01f
  var _faceTargetX: Float = 0.0f       ///< 顔の向きのX目標値(この値に近づいていく)
  var _faceTargetY: Float = 0.0f       ///< 顔の向きのY目標値(この値に近づいていく)
  var _faceX: Float = 0.0f             ///< 顔の向きX(-1.0 - 1.0)
  var _faceY: Float = 0.0f             ///< 顔の向きY(-1.0 - 1.0)
  var _faceVX: Float = 0.0f            ///< 顔の向きの変化速度X
  var _faceVY: Float = 0.0f            ///< 顔の向きの変化速度Y
  var _lastTimeSeconds: Float = 0.0f   ///< 最後の実行時間[秒]
  var _userTimeSeconds: Float = 0.0f   ///< デルタ時間の積算値[秒]

  def update(deltaTimeSeconds: Float): Unit = {
    _userTimeSeconds += deltaTimeSeconds

    // 首を中央から左右に振るときの平均的な早さは  秒程度。加速・減速を考慮して、その2倍を最高速度とする
    // 顔のふり具合を、中央(0.0)から、左右は(+-1.0)とする
    val FaceParamMaxV: Float = 40.0f / 10.0f;                                      // 7.5秒間に40分移動（5.3/sc)
    val MaxV: Float = FaceParamMaxV * 1.0f / FrameRate;  // 1frameあたりに変化できる速度の上限

    if (_lastTimeSeconds == 0.0f)
    {
      _lastTimeSeconds = _userTimeSeconds
      return
    }

    val deltaTimeWeight: Float = (_userTimeSeconds - _lastTimeSeconds) * FrameRate
    _lastTimeSeconds = _userTimeSeconds
    val TimeToMaxSpeed: Float = 0.15f
    val FrameToMaxSpeed: Float = TimeToMaxSpeed * FrameRate     // sec * frame/sec
    val MaxA: Float = deltaTimeWeight * MaxV / FrameToMaxSpeed                          // 1frameあたりの加速度
    val dx: Float = _faceTargetX - _faceX
    val dy: Float = _faceTargetY - _faceY

    if (dx.abs <= Epsilon && dy.abs <= Epsilon)
    {
      return; // 変化なし
    }

    // 速度の最大よりも大きい場合は、速度を落とす
    val d: Float = sqrt((dx * dx) + (dy * dy)).toFloat
    // 進行方向の最大速度ベクトル
    val vx: Float = MaxV * dx / d
    val vy: Float = MaxV * dy / d


    // 現在の速度から、新規速度への変化（加速度）を求める
    var ax: Float = vx - _faceVX
    var ay: Float = vy - _faceVY
    val a: Float = sqrt((ax * ax) + (ay * ay)).toFloat
    // 加速のとき
    if (a < -MaxA || a > MaxA)
    {
      ax *= MaxA / a
      ay *= MaxA / a
    }
    // 加速度を元の速度に足して、新速度とする
    _faceVX += ax
    _faceVY += ay
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

      val maxV: Float = 0.5f * (sqrt((MaxA * MaxA) + 16.0f * MaxA * d - 8.0f * MaxA * d).toFloat - MaxA)
      val curV: Float = sqrt((_faceVX * _faceVX) + (_faceVY * _faceVY)).toFloat

      if (curV > maxV)
      {
        // 現在の速度 > 最高速度のとき、最高速度まで減速
        _faceVX *= maxV / curV
        _faceVY *= maxV / curV
      }
      _faceX += _faceVX
      _faceY += _faceVY

    }

  }

  def set(x: Float, y: Float): Unit = {
    println(s"TargetPoint.set($x, $y)")
    this._faceTargetX = x
    this._faceTargetY = y
  }
  def getX: Float = _faceX
  def getY: Float = _faceY
}
