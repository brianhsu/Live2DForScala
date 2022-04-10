package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.data.PosePart
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose.{BackOpacityThreshold, Epsilon, Phi}
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{FallbackParameterValueUpdate, PartOpacityUpdate}


object Pose {
  /**
   * The limit of value in fallback parameters
   *
   * If a fallback parameter of a partID has a value less than this constant,
   * The part will not be considered as a first visible part when fading.
   */
  private val Epsilon: Float = 0.001f
  private val Phi: Float = 0.5f
  private val BackOpacityThreshold: Float = 0.15f
}

/**
 * The Pose effect
 *
 * This effect controls the opacity of parts in Live 2D mode, to achieve
 * different pose.
 *
 * For example, when not apply this effect, you will see four arms when
 * using the default Haru avatar Live 2D module come with the Cubism Live
 * 2D SDK.
 *
 * This is because there are actually four arm parts in that model, and we
 * must make two arms invisible by change opacity using this Pose effect.
 *
 * Each element in `posePartGroups` represents a pose, which contains a
 * list of a PartData indicates what parts' opacity should be updated
 * during change pose.
 *
 * @param posePartGroups      The pose part groups.
 * @param fadeTimeInSeconds   The default fade in time when change pose.
 */
case class Pose(posePartGroups: List[List[PosePart]] = Nil,
                fadeTimeInSeconds: Float = 0) extends Effect {


  /**
   * Is the parts in this model already be initialized
   */
  private var isAlreadyInit = false

  /**
   * Create operations to update model.
   *
   * @param model                       The Live2D model.
   * @param totalElapsedTimeInSeconds   The total elapsed time since first frame in seconds
   * @param deltaTimeInSeconds          The elapsed time since last frame in seconds.
   */
  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {

    val actualDeltaTimeSeconds = if (deltaTimeInSeconds < 0.0f) 0 else deltaTimeInSeconds
    val resetModelOperation: List[UpdateOperation] = if (!isAlreadyInit) { resetParts() } else Nil
    val fadeOperation: List[UpdateOperation] = posePartGroups.flatMap(poseParts => doFade(model, actualDeltaTimeSeconds, poseParts))
    val resetAndFade = resetModelOperation ++ fadeOperation
    val copyPartOpacityOperations: List[UpdateOperation] = copyPartOpacitiesToLinkedParts(model, resetAndFade)

    isAlreadyInit = true

    resetAndFade ++ copyPartOpacityOperations
  }

  /**
   * Create operations to actually fade parts.
   *
   * @param   model               The Live2D model.
   * @param   deltaTimeSeconds    How many time in seconds has been passed since last update.
   * @param   poseParts           The parts that should be updated.
   */
  private def doFade(model: Live2DModel, deltaTimeSeconds: Float, poseParts: List[PosePart]): List[UpdateOperation] = {
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

    val otherOperations = for {
      (firstVisiblePart, targetedOpacity) <- visiblePartHolder.toList
      partData <- poseParts if partData != firstVisiblePart
      partId = partData.partId
    } yield {
      val originalOpacity = model.parts(partId).opacity
      val adjustedOpacity = calculateAdjustedOpacity(targetedOpacity)
      PartOpacityUpdate(partId, originalOpacity.min(adjustedOpacity))
    }

    initOperation.toList ++ otherOperations
  }

  /**
   * Adjust calculated opacity according to thresholds.
   *
   * @param targetedOpacity The targeted opacity of a part.
   * @return                The adjusted opacity.
   */
  private def calculateAdjustedOpacity(targetedOpacity: Float): Float = {
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
   * Create operations to copy opacities to linked parts.
   *
   * The linked parts must have same opacity, so this method will
   * retrieve the new opacity from previous calculated operations.
   *
   * @param model         The Live2D model.
   * @param resetAndFade  The reset and fade operations.
   */
  private def copyPartOpacitiesToLinkedParts(model: Live2DModel, resetAndFade: List[UpdateOperation]): List[PartOpacityUpdate] = {
    for {
      pose: List[PosePart] <- posePartGroups
      posePartData <- pose
      posePart <- model.parts.get(posePartData.partId).toList
      updateOpacity <- resetAndFade if isPartOpacityUpdateForSamePart(updateOpacity, posePart.id)
      updatedOpacityValue = updateOpacity.asInstanceOf[PartOpacityUpdate].value
      linkPartData <- posePartData.link
      linkPart <- model.parts.get(linkPartData.partId)
    } yield {
      PartOpacityUpdate(linkPart.id, updatedOpacityValue)
    }
  }

  /**
   * Check if a operation is for update opacity of a specified partId
   * @param operation The operation.
   * @param partId    The specified partId.
   * @return          A boolean indicates if it's an update opacity operation for `partId`.
   */
  private def isPartOpacityUpdateForSamePart(operation: UpdateOperation, partId: String): Boolean = {
    operation.isInstanceOf[PartOpacityUpdate] &&
      operation.asInstanceOf[PartOpacityUpdate].partId == partId
  }

  /**
   * Create initialization operations.
   *
   * @note It will set the opacity to 1 for parameters with a non-zero initial opacity.
   */
  private def resetParts(): List[UpdateOperation] = {
    val operationsForEachPose: List[List[UpdateOperation]] = for {
      poseGroup <- posePartGroups
      posePartData <- poseGroup
      partId = posePartData.partId
    } yield {
      val initOpacity = if (posePartData == poseGroup.head) 1.0f else 0.0f
      val partOperation = FallbackParameterValueUpdate(partId, initOpacity)
      val linkOperation = posePartData.link.map(link => FallbackParameterValueUpdate(link.partId, 1))
      partOperation :: linkOperation
    }

    operationsForEachPose.flatten
  }


  /**
   * Set the status of initialization
   *
   * The should be only used in unit test.
   *
   * @param isAlreadyInit The init status from unit test data point.
   */
  private[impl] def setInitStatusForTest(isAlreadyInit: Boolean): Unit = {
    this.isAlreadyInit = isAlreadyInit
  }

}
