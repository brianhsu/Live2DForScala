package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Part
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.effect.Effect

object Pose {
  private val DefaultFadeInSeconds = 0.5f
  private def createPartData(pose: List[Part]): List[PartData] = {
    pose.map { partInfo =>
      val linkedPart = partInfo.link.map(id => PartData(id))
      PartData(partInfo.id, linkedPart)
    }
  }
  def apply(avatarSettings: Settings): Pose = {
    val poseHolder = avatarSettings.pose.map { poseSettings =>
      val groups = poseSettings.groups
      val reasonablePartGroup = groups.map(createPartData)
      val partGroupCount = groups.map(_.size)
      val fadeTimeInSeconds = poseSettings.fadeInTime.filterNot(_ < 0).getOrElse(DefaultFadeInSeconds)
      new Pose(reasonablePartGroup, reasonablePartGroup.flatten, partGroupCount, fadeTimeInSeconds)
    }
    poseHolder.getOrElse(new Pose)
  }

  def main(args: Array[String]): Unit = {
    println("Hello World")
    val x: List[List[PartData]] = List(
      List(
        PartData("level1", List(PartData("level1-1"), PartData("level1-2"))),
        PartData("level2", List(PartData("level2-1"), PartData("level2-2"))),
      ),
      List(
        PartData("level3", List(PartData("level3-1"), PartData("level3-2"))),
        PartData("level4", List(PartData("level4-1"), PartData("level4-2"))),
      ),
    )

    val y = Map("level2" -> "aa", "level3" -> "bb")

    for {
      pose: List[PartData] <- x
      posePart <- pose
      temp <- y.get(posePart.partId)
      linkPart <- posePart.link
    } {
      println(linkPart)
    }
  }
}

class Pose(val reasonablePartGroups: List[List[PartData]] = Nil,
           val partGroups: List[PartData] = Nil,
           val partGroupCount: List[Int] = Nil,
           val fadeTimeInSeconds: Float = 0) extends Effect {

  private val Epsilon: Float = 0.001f
  private var _lastModel: Live2DModel = null
  /**
   * パーツのフェード操作を実行
   *
   * パーツのフェード操作を行う。
   *
   * @param   model               対象のモデル
   * @param   deltaTimeSeconds    デルタ時間[秒]
   * @param   beginIndex          フェード操作を行うパーツグループの先頭インデックス
   * @param   partGroupCount      フェード操作を行うパーツグループの個数
   */
  def DoFade(model: Live2DModel, deltaTimeSeconds: Float, beginIndex: Int, partGroupCount: Int): Unit = {
    var visiblePartIndex: Int = -1
    var newOpacity: Float = 1.0f

    val Phi: Float = 0.5f
    val BackOpacityThreshold: Float = 0.15f
    var isBreak: Boolean = false

    for (i <- beginIndex until beginIndex + partGroupCount if !isBreak) {
      val partId = partGroups(i).partId

      val v: Float = model.parameterWithFallback(partGroups(i).partId).current
      if (v > Epsilon) {
        if (visiblePartIndex >= 0) {
          isBreak = true
        }

        visiblePartIndex = i
        newOpacity = model.parts(partId).opacity

        // 新しい不透明度を計算
        newOpacity += (deltaTimeSeconds / fadeTimeInSeconds)

        if (newOpacity > 1.0f) {
          newOpacity = 1.0f
        }
      }
    }

    if (visiblePartIndex < 0) {
      visiblePartIndex = 0
      newOpacity = 1.0f
    }

    for (i <- beginIndex until  beginIndex + partGroupCount){
      val partId = partGroups(i).partId
      //  表示パーツの設定
      if (visiblePartIndex == i) {
        model.parts(partId).opacity = newOpacity// 先に設定
      } else {
        var opacity = model.parts(partId).opacity
        var a1: Float = 0          // 計算によって求められる不透明度

        if (newOpacity < Phi) {
          a1 = newOpacity * (Phi - 1) / Phi + 1.0f // (0,1),(phi,phi)を通る直線式
        } else {
          a1 = (1 - newOpacity) * Phi / (1.0f - Phi) // (1,0),(phi,phi)を通る直線式
        }

        // 背景の見える割合を制限する場合
        val backOpacity = (1.0f - a1) * (1.0f - newOpacity)

        if (backOpacity > BackOpacityThreshold) {
          a1 = 1.0f - BackOpacityThreshold / (1.0f - newOpacity)
        }

        if (opacity > a1) {
          opacity = a1 // 計算の不透明度よりも大きければ（濃ければ）不透明度を上げる
        }
        model.parts(partId).opacity = opacity

      }

    }

  }

  /**
   * パーツの不透明度をコピー
   *
   * パーツの不透明度をコピーし、リンクしているパーツへ設定する。
   *
   * @param   model   対象のモデル
   */
  def copyPartOpacities(model: Live2DModel): Unit = {
    for {
      pose: List[PartData] <- reasonablePartGroups
      posePartData <- pose
      posePart <- model.parts.get(posePartData.partId)
      linkPartData <- posePartData.link
      linkPart <- model.parts.get(linkPartData.partId)
    } {
      linkPart.opacity = posePart.opacity
    }
  }

  /**
   * 表示を初期化
   *
   * 表示を初期化する。
   *
   * model   対象のモデル
   *
   * @note 不透明度の初期値が0でないパラメータは、不透明度を1に設定する。
   */
  def resetParts(model: Live2DModel): Unit = {
    for {
      poseGroup <- reasonablePartGroups
      posePartData <- poseGroup
    } {
      model.parameterWithFallback(posePartData.partId).update(value = 1)
      val partId = posePartData.partId
      model.parts.get(partId).foreach { part =>
        val initOpacity = if (posePartData == poseGroup.head) 1.0f else 0.0f
        part.opacity = initOpacity
        model.parameterWithFallback(partId).update(initOpacity)
        posePartData.link.foreach(link => model.parameterWithFallback(link.partId).update(value = 1))
      }
    }
  }
  /**
   * モデルのパラメータの更新
   *
   * モデルのパラメータを更新する。
   *
   * @param   model              対象のモデル
   * @param   deltaTimeSeconds   デルタ時間[秒]
   */
  def updateParameters(model: Live2DModel, deltaTimeSeconds: Float): Unit = {
    // 前回のモデルと同じではないときは初期化が必要
    if (model != _lastModel) {
      // パラメータインデックスの初期化
      resetParts(model)
    }

    _lastModel = model

    val actualDeltaTimeSeconds = if (deltaTimeSeconds < 0.0f) 0 else deltaTimeSeconds
    var beginIndex = 0
    for (i <- partGroupCount.indices) {
      val partGroupCount1 = partGroupCount(i)
      DoFade(model, actualDeltaTimeSeconds, beginIndex, partGroupCount1)
      beginIndex += partGroupCount1
    }

    copyPartOpacities(model)

  }

}
