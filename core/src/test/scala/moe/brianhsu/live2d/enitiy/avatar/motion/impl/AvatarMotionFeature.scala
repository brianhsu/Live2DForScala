package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarMotionDataReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel}
import moe.brianhsu.utils.expectation.ExpectedAvatarMotionOperation
import moe.brianhsu.utils.expectation.ExpectedAvatarMotionOperation.Input
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

class AvatarMotionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with MockFactory {

  Feature("Delegate event list to MotionSettings") {
    Scenario("When there are events inside MotionSettings") {
      Given("a folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle").head
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      Then("the event list in motion should be the same from motion settings")
      val motionData = new AvatarMotionDataReader(motionSetting).loadMotionData()
      motion.events shouldBe motionData.events

      And("the event size should > 0")
      motion.events.size should be > 0
    }

    Scenario("When there are no events inside MotionSettings") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle").head
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
      Given("a folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      And("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle").head
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/markIdel01Motion.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(0) motion") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(1)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/haruTapBody1.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(2) motion") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(2)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/haruTapBody2.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru tapBody(3) motion") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody")(3)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/haruTapBody3.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Haru idle(0) motion") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruForMotionTest"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle").head
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/haruIdle0.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Natori idle(1) motion") {
      Given("a folder path contains json files for Natori Live2D avatar model")
      val folderPath = "src/test/resources/models/Natori/"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("idle")(1)
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/natoriIdle1.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Rice tapBody(0) motion") {
      Given("a folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice/"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody").head
      val motion = AvatarMotion(motionSetting, settings.eyeBlinkParameterIds, settings.lipSyncParameterIds)

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/riceTapBody1.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

    Scenario("Calculate operations for Rice tapBody(0) motion with loop") {
      Given("a folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice/"

      When("create a Motion from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val motionSetting = settings.motionGroups("tapBody").head
      val motion = AvatarMotion(
        motionSetting,
        settings.eyeBlinkParameterIds, settings.lipSyncParameterIds,
        isLoop = true
      )

      And("test data points from recorded file")
      val dataPointList = ExpectedAvatarMotionOperation.fromFile("src/test/resources/expectation/motionOperations/riceTapBody1Loop.json")

      dataPointList.foreach { case ExpectedAvatarMotionOperation(input, output) =>
        When("calculate the operations from data point using motion")
        val model = createStubbedModel(input)
        val operations = motion.calculateOperations(model, input.totalElapsedTimeInSeconds, input.deltaTimeInSeconds, input.weight, input.startTimeInSeconds, input.fadeInStartTimeInSeconds, input.endTimeInSeconds)

        Then("it should be the same as the result in recorded file")
        operations should contain theSameElementsInOrderAs output.operations
      }
    }

  }

  private def createStubbedModel(input: Input): Live2DModel = {
    val model: Live2DModel = stub[Live2DModel]
    val params = input.parameters.map { case (id, currentValue) => (id, new JavaVMParameter(id, value = currentValue)) }
    (() => model.parameters).when().returns(params)
    model
  }
}
