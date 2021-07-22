package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.framework.model.{AvatarSettings, Live2DModel}

object Pose {
  private val DefaultFadeInSeconds = 0.5f
  def apply(avatarSettings: Settings): Pose = {
    val ret = new Pose
    avatarSettings.pose.foreach { poseSettings =>
      ret._fadeTimeSeconds = poseSettings.fadeInTime.filterNot(_ < 0).getOrElse(DefaultFadeInSeconds)
      val groups = poseSettings.groups
      for (poseIndex <- groups.indices) {
        var groupCount = 0
        for (groupIndex  <- groups(poseIndex).indices) {
          val partInfo = groups(poseIndex)(groupIndex)
          val partData = new PartData
          partData.PartId = partInfo.id
          if (partInfo.link.nonEmpty) {
            for (linkIndex <- partInfo.link.indices) {
              val linkPart = new PartData
              linkPart.PartId = partInfo.link(linkIndex)
              partData.Link = partData.Link.appended(linkPart)
            }

          }
          ret._partGroups = ret._partGroups.appended(partData)
          groupCount += 1
        }
        ret._partGroupCounts = ret._partGroupCounts.appended(groupCount)
      }

    }
    ret
  }
}

class Pose {
  private val Epsilon: Float = 0.001f

  private var _partGroups: List[PartData] = Nil
  private var _partGroupCounts: List[Int] = Nil
  private var _fadeTimeSeconds: Float = 0
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
      val partIndex = _partGroups(i).PartIndex
      val paramIndex = _partGroups(i).ParameterIndex

      val v: Float = model.getParameterValueUsingIndex(paramIndex)
      if (v > Epsilon) {
        if (visiblePartIndex >= 0) {
          isBreak = true
        }

        visiblePartIndex = i
        newOpacity = model.partsList(partIndex).opacity

        // 新しい不透明度を計算
        newOpacity += (deltaTimeSeconds / _fadeTimeSeconds)

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
      val partsIndex = _partGroups(i).PartIndex
      //  表示パーツの設定
      if (visiblePartIndex == i) {
        model.partsList(partsIndex).setOpacity(newOpacity)// 先に設定
      } else {
        var opacity = model.partsList(partsIndex).opacity
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
        model.partsList(partsIndex).setOpacity(opacity)

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
  def CopyPartOpacities(model: Live2DModel): Unit = {
    for (groupIndex <- _partGroups.indices) {
      val partData = _partGroups(groupIndex)
      if (partData.Link.isEmpty) {
        //continue // 連動するパラメータはない
      } else {
        val partIndex = _partGroups(groupIndex).PartIndex
        if (partIndex >= 0) {
          val opacity: Float = model.partsList(partIndex).opacity
          for (linkIndex <- partData.Link.indices) {
            val linkPart = partData.Link(linkIndex)
            val linkPartIndex = linkPart.PartIndex
            if (linkPartIndex >= 0) {
              model.setPartOpacityUsingIndex(linkPartIndex, opacity)
            }
          }
        }
      }
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
  def Reset(model: Live2DModel): Unit = {
    var beginIndex: Int = 0
    for (i <- _partGroupCounts.indices) {
      val groupCount = _partGroupCounts(i)
      for (j <- beginIndex until beginIndex + groupCount) {
        _partGroups(j).Initialize(model)
        val partsIndex = _partGroups(j).PartIndex
        val paramIndex = _partGroups(j).ParameterIndex
        if (partsIndex < 0) {
          // continue
        } else {

          val v = if (j == beginIndex) 1.0f else 0.0f
          model.setPartOpacityUsingIndex(partsIndex, v)
          model.setParameterValueUsingIndex(paramIndex, v)
          for (k <- _partGroups(j).Link.indices) {
            _partGroups(j).Link(k).Initialize(model)
          }
        }
      }
      beginIndex += groupCount
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
  def UpdateParameters(model: Live2DModel, deltaTimeSeconds: Float): Unit = {
    // 前回のモデルと同じではないときは初期化が必要
    if (model != _lastModel) {
      // パラメータインデックスの初期化
      Reset(model)
    }

    _lastModel = model

    var actualDeltaTimeSeconds = deltaTimeSeconds
    // 設定から時間を変更すると、経過時間がマイナスになることがあるので、経過時間0として対応。
    if (actualDeltaTimeSeconds < 0.0f) {
      actualDeltaTimeSeconds = 0.0f
    }
    var beginIndex = 0
    for (i <- _partGroupCounts.indices) {
      val partGroupCount = _partGroupCounts(i)
      DoFade(model, actualDeltaTimeSeconds, beginIndex, partGroupCount)
      beginIndex += partGroupCount
    }

    CopyPartOpacities(model)

  }

}
