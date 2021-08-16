package moe.brianhsu.porting.live2d.framework.math

object CubismMath {
  val Epsilon = 0.00001f

  /**
   * 第一引数の値を最小値と最大値の範囲に収めた値を返す
   *
   * @param value ->  収められる値
   * @param min   ->  範囲の最小値
   * @param max   ->  範囲の最大値
   * @return 最小値と最大値の範囲に収めた値
   */
  private def limitToRange(value: Float, min: Float, max: Float): Float = {
    value.max(min).min(max)
  }

  def quadraticEquation(a: Float, b: Float, c: Float): Float = {
    if (Math.abs(a) < Epsilon) {
      if (Math.abs(b) < Epsilon) {
        return -c
      }
      return -c / b
    }

    -(b + Math.sqrt(b * b - 4.0f * a * c).toFloat) / (2.0f * a)
  }

  def CardanoAlgorithmForBezier(a: Float, b: Float, c: Float, d: Float): Float = {
    if (Math.abs(a) < Epsilon) {
      return limitToRange(quadraticEquation(b, c, d), 0.0f, 1.0f)
    }

    val ba: Float = b / a
    val ca: Float = c / a
    val da: Float = d / a

    val p: Float = (3.0f * ca - ba*ba) / 3.0f
    val p3: Float = p / 3.0f
    val q: Float = (2.0f * ba*ba*ba - 9.0f * ba*ca + 27.0f * da) / 27.0f
    val q2: Float = q / 2.0f
    val discriminant: Float = q2*q2 + p3*p3*p3

    val center: Float = 0.5f
    val threshold: Float = center + 0.01f

    if (discriminant < 0.0f) {
      val mp3: Float = -p / 3.0f
      val mp33: Float = mp3*mp3*mp3
      val r: Float = Math.sqrt(mp33).toFloat
      val t: Float = -q / (2.0f * r)
      val cosphi: Float = limitToRange(t, -1.0f, 1.0f)
      val phi: Float = Math.acos(cosphi).toFloat
      val crtr: Float = Math.cbrt(r).toFloat
      val t1: Float = 2.0f * crtr

      val root1 = t1 * Math.cos(phi / 3.0f).toFloat - ba / 3.0f
      if (Math.abs(root1 - center) < threshold) {
        return limitToRange(root1, 0.0f, 1.0f)
      }

      val root2: Float = t1 * Math.cos((phi + 2.0f * Math.PI) / 3.0f).toFloat - ba / 3.0f
      if (Math.abs(root2 - center) < threshold) {
        return limitToRange(root2, 0.0f, 1.0f)
      }

      val root3: Float = t1 * Math.cos((phi + 4.0f * Math.PI) / 3.0f).toFloat - ba / 3.0f
      return limitToRange(root3, 0.0f, 1.0f)
    }

    if (discriminant == 0.0f) {
      val u1: Float = if (q2 < 0.0f) {
        Math.cbrt(-q2).toFloat
      } else {
         -Math.cbrt(q2).toFloat
      }

      val root1: Float = 2.0f * u1 - ba / 3.0f

      if (Math.abs(root1 - center) < threshold) {
        return limitToRange(root1, 0.0f, 1.0f)
      }

      val root2: Float = -u1 - ba / 3.0f
      return limitToRange(root2, 0.0f, 1.0f)
    }

    val sd: Float = Math.sqrt(discriminant).toFloat
    val u1: Float = Math.cbrt(sd - q2).toFloat
    val v1: Float = Math.cbrt(sd + q2).toFloat
    val root1: Float = u1 - v1 - ba / 3.0f

    limitToRange(root1, 0.0f, 1.0f)
  }
}
