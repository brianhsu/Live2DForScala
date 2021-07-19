package moe.brianhsu.live2d.framework.math

object CubismMath {
  /**
   *  イージング処理されたサインを求める
   *
   *  フェードイン・アウト時のイージングに利用できる
   *
   *@param    value  ->  イージングを行う値
   *@return   イージング処理されたサイン値
   */
  def GetEasingSine(value: Float): Float = {

    value match {
      case x if x < 0.0f => 0.0f
      case x if x > 1.0f => 1.0f
      case x => 0.5f - 0.5f * Math.cos(x * Math.PI).toFloat
    }
  }

}
