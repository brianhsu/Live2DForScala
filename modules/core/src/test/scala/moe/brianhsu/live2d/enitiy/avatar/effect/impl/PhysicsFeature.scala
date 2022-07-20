package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.AvatarPhysicsReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel}
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, OptionValues, TryValues}

import scala.io.Source
import scala.util.Using

class PhysicsFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with MockFactory with OptionValues {
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

  Feature("Calculate operations of Physics effect") {
    Scenario("Calculate physics from Rice avatar") {
      Given("a folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice"

      When("create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      And("set wind direction to (10.0f, 10.0f)")
      physics.windDirection = EuclideanVector(10.0f, 10.0f)

      val testDataFile = Source.fromFile("src/test/resources/expectation/physicOperations/markPhysicsOperations.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { dataPoint =>
        val model = createStubbedModel(dataPoint)

        When("calculate the updated operations")
        val operations = physics.calculateOperations(model, dataPoint.totalElapsedTimeInSeconds, dataPoint.deltaTimeSeconds)

        Then("the result should match the record in log files")
        operations.size shouldBe dataPoint.operations.size
        operations should contain theSameElementsInOrderAs dataPoint.operations
      }
    }

    Scenario("Calculate physics from Hiyori avatar") {
      Given("a folder path contains json files for Hiyori Live2D avatar model")
      val folderPath = "src/test/resources/models/Hiyori"

      When("create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      And("set wind direction to (10.0f, 10.0f)")
      physics.windDirection = EuclideanVector(10.0f, 10.0f)

      val testDataFile = Source.fromFile("src/test/resources/expectation/physicOperations/hiyoriPhysicsOperations.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { dataPoint =>
        val model = createStubbedModel(dataPoint)

        When("calculate the updated operations")
        val operations = physics.calculateOperations(model, dataPoint.totalElapsedTimeInSeconds, dataPoint.deltaTimeSeconds)

        Then("the result should match the record in log files")
        operations.size shouldBe dataPoint.operations.size
        operations should contain theSameElementsInOrderAs dataPoint.operations
      }
    }

  }

  Feature("Start / stop the effects") {
    Scenario("Start the effect") {
      Given("a folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice"

      When("create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      When("start the Physics effect")
      Then("nothing should happen")
      noException shouldBe thrownBy {
        physics.start()
      }
    }

    Scenario("Stop the effect") {
      Given("a folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice"

      When("create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      When("stop the Physics effect")
      Then("nothing should happen")
      noException shouldBe thrownBy {
        physics.stop()
      }
    }

  }

  private def parseLog(line: String): LogData = parse(line).extract[LogData]

  private def createStubbedModel(logData: LogData): Live2DModel = {
    val model: Live2DModel = stub[Live2DModel]
    val mockedParameters = logData.parameters.map { case (id, parameter) =>
      id -> new JavaVMParameter(id, parameter.min, parameter.max, parameter.default, parameter.current)
    }

    (() => model.parameters).when().returns(mockedParameters)
    model
  }

  private case class CurrentParameter(current: Float, min: Float, max: Float, default: Float)
  private case class LogData(
    totalElapsedTimeInSeconds: Float,
    deltaTimeSeconds: Float,
    parameters: Map[String, CurrentParameter],
    operations: List[UpdateOperation]
  )

}
