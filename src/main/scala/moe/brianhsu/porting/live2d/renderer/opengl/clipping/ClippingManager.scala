package moe.brianhsu.porting.live2d.renderer.opengl.clipping

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.Normal
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.ViewPort
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureManager
import moe.brianhsu.porting.live2d.renderer.opengl.Renderer
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingContext.Layout

class ClippingManager(model: Live2DModel, textureManager: TextureManager)(implicit gl: OpenGLBinding) {

  val clippingMaskBufferSize: Int = 256 ///< クリッピングマスクのバッファサイズ（初期値:256）

  private var contextListForMask: List[ClippingContext] = initContextListForMask(model) ///< マスク用クリッピングコンテキストのリスト

  def getClippingContextByDrawable(drawable: Drawable): Option[ClippingContext] = {
    contextListForMask.find(_.clippedDrawables.map(_.id).contains(drawable.id))
  }

  def initContextListForMask(model: Live2DModel): List[ClippingContext] = {
    val drawablesHasMasks = model.drawables.values.filter(_.masks.nonEmpty)
    val masksToDrawables = drawablesHasMasks.groupBy(_.masks.sorted).toList
    val contextList = masksToDrawables.map { case(masks, drawables) =>
      val maskDrawables = masks.map(model.drawablesByIndex)
      new ClippingContext(maskDrawables, drawables.toList)
    }

    contextList.sortBy(_.clippedDrawables.head.index)
  }

  def setupLayoutBounds(usingClipCount: Int): Unit = {
    if (usingClipCount <= 0) {
      contextListForMask = contextListForMask.map(_.copy(layout = Layout(0, Rectangle(0.0f, 0.0f, 1.0f, 1.0f))))
    } else {

      // ひとつのRenderTextureを極力いっぱいに使ってマスクをレイアウトする
      // マスクグループの数が4以下ならRGBA各チャンネルに１つずつマスクを配置し、5以上6以下ならRGBAを2,2,1,1と配置する

      val div = usingClipCount / ClippingContext.ColorChannelCount //１チャンネルに配置する基本のマスク個数
      val mod = usingClipCount % ClippingContext.ColorChannelCount //余り、この番号のチャンネルまでに１つずつ配分する

      // RGBAそれぞれのチャンネルを用意していく(0:R , 1:G , 2:B, 3:A, )
      var curClipIndex = 0 //順番に設定していく

      for (channelNo <- 0 until ClippingContext.ColorChannelCount) {
        // このチャンネルにレイアウトする数
        val layoutCount = div + (if (channelNo < mod) 1 else 0)

        layoutCount match {
          case 0 =>
          case 1 =>
            val cc = contextListForMask(curClipIndex)
            contextListForMask = contextListForMask.updated(
              curClipIndex,
              cc.copy(layout = Layout(channelNo, Rectangle(0.0f, 0.0f, 1.0f, 1.0f)))
            )
            curClipIndex += 1
          case 2 =>
            //UVを2つに分解して使う
            for (i <- 0 until layoutCount) {
              val xPosition = i % 2
              val cc = contextListForMask(curClipIndex)
              contextListForMask = contextListForMask.updated(
                curClipIndex,
                cc.copy(layout = Layout(channelNo, Rectangle(xPosition * 0.5f, 0.0f, 0.5f, 1.0f)))
              )
              curClipIndex += 1
            }
          case 3|4 =>
            //4分割して使う
            for (i <- 0 until layoutCount) {
              val xPosition = i % 2
              val yPosition = i / 2
              val cc = contextListForMask(curClipIndex)
              contextListForMask = contextListForMask.updated(
                curClipIndex,
                cc.copy(layout = Layout(channelNo, Rectangle(xPosition * 0.5f, yPosition * 0.5f, 0.5f, 0.5f)))
              )
              curClipIndex += 1
            }
          case 5|6|7|8|9 =>
            //9分割して使う
            for (i <- 0 until layoutCount) {
              val xPosition = i % 3
              val yPosition = i / 3
              val cc = contextListForMask(curClipIndex)

              contextListForMask = contextListForMask.updated(
                curClipIndex,
                cc.copy(layout = Layout(channelNo, Rectangle(xPosition / 3.0f, yPosition / 3.0f, 1.0f / 3.0f, 1.0f / 3.0f)))
              )
              curClipIndex += 1
            }
          case _ =>
            printf("not supported mask count : %d\n", layoutCount)

            // 引き続き実行する場合、 SetupShaderProgramでオーバーアクセスが発生するので仕方なく適当に入れておく
            // もちろん描画結果はろくなことにならない
            for (_ <- 0 until layoutCount) {
              val cc = contextListForMask(curClipIndex)
              contextListForMask = contextListForMask.updated(
                curClipIndex,
                cc.copy(layout = Layout(0, Rectangle(0.0f, 0.0f, 1.0f, 1.0f)))
              )
              curClipIndex += 1
            }
        }
      }
    }
  }

  def setupClippingContext(renderer: Renderer, lastFBO: Int, lastViewport: ViewPort): Unit = {

    this.contextListForMask = contextListForMask.map(_.calculateClippedDrawTotalBounds())
    val usingClipCount = contextListForMask.count(_.isUsing)

    // マスク作成処理
    if (usingClipCount > 0) {
      gl.glViewport(0, 0, clippingMaskBufferSize, clippingMaskBufferSize)

      renderer.preDraw()
      renderer.offscreenBufferHolder.foreach(_.beginDraw(lastFBO))
      setupLayoutBounds(usingClipCount)


      // 実際にマスクを生成する
      // 全てのマスクをどの様にレイアウトして描くかを決定し、ClipContext , ClippedDrawContext に記憶する
      this.contextListForMask = this.contextListForMask.map(_.calculateMatrix())
      for (clipContext <- contextListForMask) {

        for (drawable <- clipContext.maskDrawable if drawable.dynamicFlags.vertexPositionChanged) {
          renderer.setIsCulling(drawable.isCulling)
          renderer.setClippingContextBufferForMask(Some(clipContext))

          val textureFile = model.textureFiles(drawable.textureIndex)
          val textureInfo = textureManager.loadTexture(textureFile)
          renderer.drawMesh(
            textureInfo.textureId,
            drawable.vertexInfo,
            drawable.opacity,
            Normal,
            invertedMask = false
          )
        }
      }

      // --- 後処理 ---
      renderer.offscreenBufferHolder.foreach(_.endDraw())
      renderer.setClippingContextBufferForMask(None)
      RichOpenGLBinding.wrapOpenGLBinding(gl).viewPort = lastViewport
    }
  }
}
