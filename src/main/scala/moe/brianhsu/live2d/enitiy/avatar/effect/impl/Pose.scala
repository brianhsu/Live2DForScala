package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose.{BackOpacityThreshold, Epsilon, Phi}
import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueUpdate, Effect, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Part
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.PartData

object Pose {
  private val DefaultFadeInSeconds = 0.5f
  private val Epsilon: Float = 0.001f
  private val Phi: Float = 0.5f
  private val BackOpacityThreshold: Float = 0.15f

  private def createPartData(pose: List[Part]): List[PartData] = {
    pose.map { partInfo =>
      val linkedPart = partInfo.link.map(id => PartData(id))
      PartData(partInfo.id, linkedPart)
    }
  }
  def apply(avatarSettings: Settings): Pose = {
    val poseHolder = avatarSettings.pose.map { poseSettings =>
      val posePartGroups = poseSettings.groups.map(createPartData)
      val fadeTimeInSeconds = poseSettings.fadeInTime.filterNot(_ < 0).getOrElse(DefaultFadeInSeconds)
      Pose(posePartGroups, fadeTimeInSeconds)
    }
    poseHolder.getOrElse(new Pose)
  }
}

case class Pose(posePartGroups: List[List[PartData]] = Nil,
                fadeTimeInSeconds: Float = 0) extends Effect {


  private var isAlreadyInit = false

  /**
   * パーツのフェード操作を実行
   *
   * パーツのフェード操作を行う。
   *
   * @param   model               対象のモデル
   * @param   deltaTimeSeconds    デルタ時間[秒]
   */
  def doFade(model: Live2DModel, deltaTimeSeconds: Float, poseParts: List[PartData]): List[EffectOperation] = {
    val visiblePartHolder = poseParts
      .find(partData => model.parameterWithFallback(partData.partId).current > Epsilon)
      .orElse(poseParts.headOption)
      .map { partData =>
        val newOpacity = model.parts(partData.partId).opacity + (deltaTimeSeconds / fadeTimeInSeconds)
        (partData, Math.min(newOpacity, 1.0f))
      }

    val initOperation = visiblePartHolder.map { case (partData, newOpacity) =>
      PartOpacityUpdate(partData.partId, newOpacity)
    }

    visiblePartHolder.foreach { case (partData, newOpacity) =>
      model.parts(partData.partId).opacity = newOpacity
    }

    val otherOperations = for {
      (firstVisiblePart, targetedOpacity) <- visiblePartHolder.toList
      partData <- poseParts if partData != firstVisiblePart
      partId = partData.partId
    } yield {
      val originalOpacity = model.parts(partId).opacity
      val adjustedOpacity = calculateAdjustedOpacity(targetedOpacity)
      model.parts(partId).opacity = originalOpacity.min(adjustedOpacity)
      PartOpacityUpdate(partId, originalOpacity.min(adjustedOpacity))
    }

    initOperation.toList ++ otherOperations
  }

  def calculateAdjustedOpacity(targetedOpacity: Float): Float = {
    val adjustedOpacity: Float = if (targetedOpacity < Phi) {
      targetedOpacity * (Phi - 1) / Phi + 1.0f // (0,1),(phi,phi)を通る直線式
    } else {
      (1 - targetedOpacity) * Phi / (1.0f - Phi) // (1,0),(phi,phi)を通る直線式
    }

    // 背景の見える割合を制限する場合
    val backOpacity = (1.0f - adjustedOpacity) * (1.0f - targetedOpacity)

    if (backOpacity > BackOpacityThreshold) {
      1.0f - BackOpacityThreshold / (1.0f - targetedOpacity)
    } else {
      adjustedOpacity
    }

  }

  /**
   * パーツの不透明度をコピー
   *
   * パーツの不透明度をコピーし、リンクしているパーツへ設定する。
   *
   * @param   model   対象のモデル
   */
  def copyPartOpacities(model: Live2DModel): List[PartOpacityUpdate] = {
    for {
      pose: List[PartData] <- posePartGroups
      posePartData <- pose
      posePart <- model.parts.get(posePartData.partId).toList
      linkPartData <- posePartData.link
      linkPart <- model.parts.get(linkPartData.partId)
    } yield {
      linkPart.opacity = posePart.opacity
      PartOpacityUpdate(linkPart.id, posePart.opacity)
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
  def resetParts(model: Live2DModel): List[EffectOperation] = {
    val operationsForEachPose: List[List[EffectOperation]] = for {
      poseGroup <- posePartGroups
      posePartData <- poseGroup
      partId = posePartData.partId
      part <- model.parts.get(partId)
    } yield {
      val initOpacity = if (posePartData == poseGroup.head) 1.0f else 0.0f
      part.opacity = initOpacity
      model.parameterWithFallback(partId).update(initOpacity)
      posePartData.link.foreach(link => model.parameterWithFallback(link.partId).update(1))

      val partOperation = FallbackParameterValueUpdate(partId, initOpacity)
      val linkOperation = posePartData.link.map(link => FallbackParameterValueUpdate(link.partId, 1))

      partOperation :: linkOperation
    }

    operationsForEachPose.flatten
  }

  /**
   * モデルのパラメータの更新
   *
   * モデルのパラメータを更新する。
   *
   * @param   model                対象のモデル
   * @param   deltaTimeInSeconds   デルタ時間[秒]
   */
  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[EffectOperation] = {
    val resetModelOperation: List[EffectOperation] = if (!isAlreadyInit) { resetParts(model) } else Nil
    val actualDeltaTimeSeconds = if (deltaTimeInSeconds < 0.0f) 0 else deltaTimeInSeconds
    val fadeOperation: List[EffectOperation] = posePartGroups.flatMap(poseParts => doFade(model, actualDeltaTimeSeconds, poseParts))
    val copyPartOpacityOperations: List[EffectOperation] = copyPartOpacities(model)

    isAlreadyInit = true

    resetModelOperation ++ fadeOperation ++ copyPartOpacityOperations
  }

}
