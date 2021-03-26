package moe.brianhsu.live2d.framework.math

case class Rectangle(leftX: Float = 0.0f, topY: Float = 0.0f, width: Float = 0.0f, height: Float = 0.0f) {

  /**
   * 右端のX座標を取得する
   */
  val rightX: Float = leftX + width

  /**
   * 下端のY座標を取得する
   */
  val bottomY: Float = {
    topY + height
  }

  /**
   * 矩形中央を軸にして縦横を拡縮する
   *
   * @param width   幅方向に拡縮する量
   * @param height  高さ方向に拡縮する量
   */
  def expand(width: Float, height: Float): Rectangle = {
    Rectangle(leftX - width, topY - height, this.width + width * 2.0f, this.height + height * 2.0f)
  }
}
