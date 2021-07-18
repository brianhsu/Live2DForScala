package moe.brianhsu.live2d.framework.effect.impl

import moe.brianhsu.live2d.framework.effect.Effect
import moe.brianhsu.live2d.framework.model.Live2DModel

class FaceDirection extends Effect {

  def updateParameters(model: Live2DModel, deltaTimeInSeconds: Float): Unit = {
    FaceDirectionTargetCalculator.update(deltaTimeInSeconds)

    val (dragX, dragY) = FaceDirectionTargetCalculator.getFaceCoordinate

    model.addParameterValue("ParamAngleX", dragX * 30); // -30から30の値を加える
    model.addParameterValue("ParamAngleY", dragY * 30);
    model.addParameterValue("ParamAngleZ", dragX * dragY * -30);

    //ドラッグによる体の向きの調整
    model.addParameterValue("ParamBodyAngleX", dragX * 10); // -10から10の値を加える

    //ドラッグによる目の向きの調整
    model.addParameterValue("ParamEyeBall", dragX); // -1から1の値を加える
    model.addParameterValue("ParamEyeBallY", dragY);
  }
}
