package moe.brianhsu.live2d.framework.effect.impl

import moe.brianhsu.live2d.demo.LAppView
import moe.brianhsu.live2d.framework.effect.Effect
import moe.brianhsu.live2d.framework.model.Live2DModel

class FaceDirection(frameRate: Int) extends Effect {

  private val targetPointCalculator = new FaceDirectionTargetCalculator(frameRate)

  /**
   * The invertTransformX / invertTransformY coordinate calculate from view matrix.
   *
   * User should pass in the invertTransformX / invertTransformY coordinate of view matrix
   * calculate from current mouse position.
   *
   * I'm not really familiar with all these matrix / view port thing and only port
   * and refactor from the origin Cusbism Live2D SDK.
   *
   * So please refer to the [[LAppView.onMouseDragged]] for detail usage, and figure
   * it out on your own.
   *
   * @param x The invertTransformX coordinate that calculate from current mouse position using viewMatrix.
   * @param y The invertTransformY coordinate that calculate from current mouse position using viewMatrix.
   */
  def setFaceTargetCoordinate(x: Float, y: Float): Unit = {
    targetPointCalculator.setFaceTargetCoordinate(x, y)
  }

  def updateParameters(model: Live2DModel, deltaTimeInSeconds: Float): Unit = {
    targetPointCalculator.update(deltaTimeInSeconds)

    val (dragX, dragY) = targetPointCalculator.getFaceCoordinate

    model.addParameterValue("ParamAngleX", dragX * 30) // -30から30の値を加える
    model.addParameterValue("ParamAngleY", dragY * 30)
    model.addParameterValue("ParamAngleZ", dragX * dragY * -30)

    //ドラッグによる体の向きの調整
    model.addParameterValue("ParamBodyAngleX", dragX * 10) // -10から10の値を加える

    //ドラッグによる目の向きの調整
    model.addParameterValue("ParamEyeBall", dragX) // -1から1の値を加える
    model.addParameterValue("ParamEyeBallY", dragY)
  }
}
