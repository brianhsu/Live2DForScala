package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.model.{AvatarSettings, Live2DModel}

object Pose {
  private val DefaultFadeInSeconds = 0.5f
  def apply(avatarSettings: AvatarSettings): Pose = {
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
          println(partData.PartId)
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
    println(ret._partGroups)
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
              model.partsList(linkPartIndex).setOpacity(opacity)
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

          if (partsIndex >= 0) {
            model.partsList(partsIndex).setOpacity(if (j == beginIndex) 1.0f else 0.0f)
          }
          if (paramIndex >= 0) {
            model.parameterList(paramIndex).update(if (j == beginIndex) 1.0f else 0.0f)
          }
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
