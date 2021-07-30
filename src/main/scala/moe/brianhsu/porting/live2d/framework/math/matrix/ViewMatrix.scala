package moe.brianhsu.porting.live2d.framework.math.matrix

import moe.brianhsu.porting.live2d.framework.math.Rectangle

/**
 *  screen.left: Float = 0        ///< デバイスに対応する論理座標上の範囲（左辺X軸位置）
 *  screen.right: Float = 0       ///< デバイスに対応する論理座標上の範囲（右辺X軸位置）
 *  screen.top: Float = 0         ///< デバイスに対応する論理座標上の範囲（下辺Y軸位置）
 *  screen.bottom: Float = 0      ///< デバイスに対応する論理座標上の範囲（上辺Y軸位置）
 *  max.left: Float = 0           ///< 論理座標上の移動可能範囲（左辺X軸位置）
 *  max.right: Float = 0          ///< 論理座標上の移動可能範囲（右辺X軸位置）
 *  max.top: Float = 0            ///< 論理座標上の移動可能範囲（下辺Y軸位置）
 *  max.bottom: Float = 0         ///< 論理座標上の移動可能範囲（上辺Y軸位置）
 *  maxScale: Float = 0          ///< 拡大率の最大値
 *  minScale: Float = 0          ///< 拡大率の最小値
 */
class ViewMatrix(screen: Rectangle, max: Rectangle,
                 maxScale: Float, minScale: Float,
                 override val dataArray: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4[ViewMatrix] {

  private def getXForTranslate(x: Float): Float = {

    if (this.dataArray(0) * this.max.rightX + (this.dataArray(12) + x) < this.screen.rightX)  {
      this.screen.rightX - this.dataArray(0) * this.max.rightX - this.dataArray(12)
    } else if (this.dataArray(0) * this.max.leftX + (this.dataArray(12) + x) > this.screen.leftX) {
      this.screen.leftX - this.dataArray(0) * this.max.leftX - this.dataArray(12)
    } else {
      x
    }

  }

  private def getYForTranslate(y: Float): Float = {
    if (this.dataArray(5) * this.max.topY + (this.dataArray(13) + y) > this.screen.topY) {
      this.screen.topY - this.dataArray(5) * this.max.topY - this.dataArray(13)
    } else if (this.dataArray(5) * this.max.bottomY + (this.dataArray(13) + y) < this.screen.bottomY) {
      this.screen.bottomY - this.dataArray(5) * this.max.bottomY - this.dataArray(13)
    } else {
      y
    }
  }

  /*
  def adjustTranslate(x: Float, y: Float): Unit = {

    val tr1 = Array[Float](
    1.0f,   0.0f,   0.0f, 0.0f,
        0.0f,   1.0f,   0.0f, 0.0f,
        0.0f,   0.0f,   1.0f, 0.0f,
      getXForTranslate(x),      getYForTranslate(y),      0.0f, 1.0f
    )

    Matrix4x4.staticMultiply(tr1, this.tr, this.tr)
  }

  def adjustScale(cx: Float, cy: Float, scale: Float): Unit = {

    val targetScale = scale * this.tr(0)
    val adjustedScale: Float = if (targetScale < this.minScale) {
      if (this.tr(0) > 0.0f) {
        this.minScale / this.tr(0)
      } else {
        scale
      }
    } else if (targetScale > this.maxScale) {
      if (this.tr(0) > 0.0f) {
        this.maxScale / this.tr(0)
      } else {
        scale
      }
    } else {
      scale
    }

    val tr1 = Array[Float](
    1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      cx,   cy,   0.0f, 1.0f
    )
    val tr2 = Array[Float](
      adjustedScale, 0.0f,  0.0f, 0.0f,
      0.0f,  adjustedScale, 0.0f, 0.0f,
      0.0f,  0.0f,  1.0f, 0.0f,
      0.0f,  0.0f,  0.0f, 1.0f
    )
    val tr3 = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      -cx,  -cy,  0.0f, 1.0f
    )

    Matrix4x4.staticMultiply(tr3, this.tr, this.tr)
    Matrix4x4.staticMultiply(tr2, this.tr, this.tr)
    Matrix4x4.staticMultiply(tr1, this.tr, this.tr)
  }

   */

  override protected def buildFrom(x: Array[Float]): ViewMatrix = {
    new ViewMatrix(screen, max, maxScale, minScale, dataArray)
  }
}
