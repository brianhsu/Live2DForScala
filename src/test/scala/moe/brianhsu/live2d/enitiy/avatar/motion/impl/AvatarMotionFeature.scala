package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarMotionDataReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect._
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel}
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import scala.io.Source
import scala.util.Using

class AvatarMotionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with MockFactory {
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

  Feature("Delegate event list to MotionSettings") {
    Scenario("When there are events inside MotionSettings") {
      Given("A folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(0)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      Then("the event list in motion should be the same from motion settings")
      val motionData = new AvatarMotionDataReader(motionSetting).loadMotionData()
      motion.events shouldBe motionData.events

      And("the event size should > 0")
      motion.events.size should be > 0
    }

    Scenario("When there are no events inside MotionSettings") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(0)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      Then("the event list in motion should be the same from motion settings")
      val motionData = new AvatarMotionDataReader(motionSetting).loadMotionData()
      motion.events shouldBe motionData.events

      And("the event size should be 0")
      motion.events.size shouldBe 0
    }

  }

  Feature("Create update operations correctly") {
    Scenario("Calculate operations for Mark idle(0) motion") {
      Given("A folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      And("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(0)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/markIdel01Motion.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        //When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        //Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(0) motion") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(1)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/haruTapBody1.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(2) motion") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(2)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/haruTapBody2.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(3) motion") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(3)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/haruTapBody3.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru idle(0) motion") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(0)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/haruIdle0.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Natori idle(1) motion") {
      Given("A folder path contains json files for Natori Live2D avatar model")
      val folderPath = "src/test/resources/models/Natori/"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(1)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/natoriIdle1.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Rice tapBody(0) motion") {
      Given("A folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice/"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(0)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/riceTapBody1.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Rice tapBody(0) motion with loop") {
      Given("A folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice/"

      When("Create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(0)
      val motion = AvatarMotion(
        motionSetting,
        settings.eyeBlinkParameterIds, settings.lipSyncParameterIds,
        isLoop = true
      )

      And("Test data points from recorded file")
      val testDataFile = Source.fromFile("src/test/resources/expectation/riceTapBody1Loop.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { case LogData(input, output) =>
        When("Calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

  }

  private def parseLog(line: String): LogData = parse(line).extract[LogData]

  private def createStubbedModel(input: Input): Live2DModel = {
    val model: Live2DModel = stub[Live2DModel]
    val params = input.parameters.map { case (id, currentValue) => (id, new JavaVMParameter(id, value = currentValue)) }
    (() => model.parameters).when().returns(params)
    model
  }

  case class LogData(input: Input, output: Output)
  case class Output(operations: List[EffectOperation])
  case class Input(totalElapsedTimeInSeconds: Float,
                   deltaTimeInSeconds: Float, weight: Float,
                   startTimeInSeconds: Float,
                   fadeInStartTimeInSeconds: Float,
                   endTimeInSeconds: Option[Float],
                   parameters: Map[String, Float])

}
