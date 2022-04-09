package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import com.sun.jna.{Memory, Pointer}
import moe.brianhsu.live2d.adapter.gateway.avatar.effect.AvatarPoseReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate, UpdateOperation}
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel, Part}
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization

import scala.util.Using

class PoseFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with MockFactory {
  private implicit val formats: Formats = Serialization.formats(ShortTypeHints(
    List(
      classOf[ParameterValueAdd],
      classOf[ParameterValueMultiply],
      classOf[ParameterValueUpdate],
      classOf[FallbackParameterValueAdd],
      classOf[FallbackParameterValueUpdate],
      classOf[PartOpacityUpdate],
    )
  ))

  Feature("Read pose parts data from Live2D avatar settings") {
    Scenario("Load pose with fade in time specific inside json file") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("Create a Pose effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPoseReader(settings)
      val pose = reader.loadPose.get

      val testDataFile = Source.fromFile("src/test/resources/expectation/pose.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { dataPoint =>
        pose.setInitStatusForTest(dataPoint.isAlreadyInit)
        val operations = pose.calculateOperations(
          createStubbedModel(dataPoint),
          dataPoint.totalElapsedTimeInSeconds,
          dataPoint.deltaTimeInSeconds
        )
        operations should contain theSameElementsInOrderAs operations
      }

    }
  }

  private def parseLog(line: String): LogData = parse(line).extract[LogData]

  private def createStubbedModel(logData: LogData): Live2DModel = {
    val model: Live2DModel = stub[Live2DModel]

    for ((parameterId, currentValue) <- logData.fallbackParameters) {
      (model.parameterWithFallback _)
        .when(parameterId)
        .returning(new JavaVMParameter(parameterId, value = currentValue))
    }

    val parts = logData.partOpacities.map { case (id, opacity) =>
      val pointer: Pointer = new Memory(4)
      pointer.setFloat(0, opacity)
      (id, Part(pointer, id, None))
    }

    (() => model.parts).when().returning(parts)
    model
  }

  case class LogData(totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float,
                     isAlreadyInit: Boolean,
                     fallbackParameters: Map[String, Float],
                     partOpacities: Map[String, Float],
                     operations: List[UpdateOperation])

}
