package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{Expression, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import moe.brianhsu.live2d.usecase.updater.Updater
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, OptionValues, TryValues}

class BasicUpdateStrategyFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory
                                  with OptionValues with TryValues with TableDrivenPropertyChecks {

  Feature("Simplified constructor") {
    Scenario("Create BasicUpdateStrategy using simplified constructor") {
      noException should be thrownBy {
        Given("A folder path contains json files for Haru Live2D avatar model")
        val folderPath = "src/test/resources/models/Haru"
        val jsonSettingsReader = new JsonSettingsReader(folderPath)

        And("Settings come from that avatar model")
        val settings = jsonSettingsReader.loadSettings().success.value

        When("Create BasicUpdateStrategy using simplified constructor")
        val updateStrategy = new BasicUpdateStrategy(settings, mock[Live2DModel])

        Then("no exception should be thrown")

        And("expression reader should not be null")
        updateStrategy.expressionReader should not be null

        And("expression manager should not be null")
        updateStrategy.expressionManager should not be null

        And("motion manager should not be null")
        updateStrategy.motionManager should not be null

      }
    }
  }

  Feature("Start expression") {
    Scenario("Start expression by Expression object") {
      Given("a dummy Expression object")
      val mockedExpression = new Expression(None, None, Nil)

      And("a mocked expression manager expected start that Expression")
      val expressionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (expressionManager.startMotion: Motion => MotionWithTransition)
        .expects(mockedExpression)
        .returns(mockedMotionWithTransition)

      And("a BasicUpdateStrategy based on that expression manager")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        stub[AvatarExpressionReader], expressionManager,
        motionManager = mock[MotionManager],
        updater = mock[Updater]
      )

      When("start an mocked expression")
      val result = updateStrategy.startExpression(mockedExpression)

      Then("it should return a MotionWithTransition object")
      result shouldBe a[MotionWithTransition]
      result shouldBe mockedMotionWithTransition
    }

    Scenario("Start expression by name and the there is no such expression") {
      Given("a dummy Expression object")
      val mockedExpression = new Expression(None, None, Nil)

      And("a mocked expression manager that expected nothing")
      val expressionManager = mock[MotionManager]

      And("a stubbed expression reader")
      val expressionReader = stub[AvatarExpressionReader]
      (() => expressionReader.loadExpressions).when().returns(
        Map("expressionName" -> mockedExpression)
      )

      And("a BasicUpdateStrategy based on that expression manager and expression reader")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        expressionReader, expressionManager,
        motionManager = mock[MotionManager],
        updater = mock[Updater]
      )

      When("start an expression name")
      val result = updateStrategy.startExpression("unknown expression")

      Then("it should return a None")
      result shouldBe None
    }

    Scenario("Start expression by name successfully") {
      Given("a dummy Expression object")
      val mockedExpression = new Expression(None, None, Nil)

      And("a mocked expression manager that expected nothing")
      val expressionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (expressionManager.startMotion: Motion => MotionWithTransition)
        .expects(mockedExpression)
        .returns(mockedMotionWithTransition)

      And("a stubbed expression reader")
      val expressionReader = stub[AvatarExpressionReader]
      (() => expressionReader.loadExpressions).when().returns(
        Map("expressionName" -> mockedExpression)
      )

      And("a BasicUpdateStrategy based on that expression manager and expression reader")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        expressionReader, expressionManager,
        motionManager = mock[MotionManager],
        updater = mock[Updater]
      )

      When("start an expression name")
      val result = updateStrategy.startExpression("expressionName")

      Then("it should return the correct MotionWithTransition")
      result.value shouldBe a[MotionWithTransition]
      result.value shouldBe mockedMotionWithTransition
    }

  }

  Feature("Start Motion with MotionSetting") {
    Scenario("Start Motion using MotionSetting object") {
      Given("A dummy MotionSetting")
      val motionSetting = MotionSetting("3", None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      And("a mocked motion manager expected start a motion")
      val motionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        updater = mock[Updater]
      )

      When("start a motion")
      val motionWithTransition = updateStrategy.startMotion(motionSetting)

      Then("the motion should be the correct value")
      motionWithTransition shouldBe mockedMotionWithTransition
    }
  }

  Feature("Start Motion with group name and index") {
    Scenario("The group name does not exist") {
      Given("a mocked motion manager expected nothing")
      val motionManager = mock[MotionManager]

      And("a Avatar Setting that has motion group")
      val motionGroups = Map("group1" -> List(mock[MotionSetting], mock[MotionSetting]))
      val avatarSetting = Settings("mocFile", Nil, None, None, Nil, Nil, Map.empty, motionGroups, Nil)

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        avatarSetting, stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        updater = mock[Updater]
      )

      When("Start an non-exist group name")
      val result = updateStrategy.startMotion("unknownGroup", 0)

      Then("the result should be None")
      result shouldBe None
    }

    Scenario("The group name exist but index of out bound") {
      val indexTable = Table(
        "index",
        -1,
        2,
        3
      )

      forAll(indexTable) { index =>
        Given("a mocked motion manager expected nothing")
        val motionManager = mock[MotionManager]

        And("a Avatar Setting that has motion group")
        val motionGroups = Map("group1" -> List(mock[MotionSetting], mock[MotionSetting]))
        val avatarSetting = Settings("mocFile", Nil, None, None, Nil, Nil, Map.empty, motionGroups, Nil)

        And("a BasicUpdateStrategy based on that motion manager")
        val updateStrategy = new BasicUpdateStrategy(
          avatarSetting, stub[Live2DModel],
          stub[AvatarExpressionReader],
          expressionManager = mock[MotionManager],
          motionManager,
          updater = mock[Updater]
        )

        When("Start an non-exist group name")
        val result = updateStrategy.startMotion("group1", index)

        Then("the result should be None")
        result shouldBe None
      }
    }
    Scenario("The group name exist and index is correct") {
      Given("a Avatar Setting that has motion group")
      val motionSetting1 = MotionSetting("3", None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)
      val motionSetting2 = MotionSetting("3", None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      val motionGroups = Map("group1" -> List(motionSetting1, motionSetting2))
      val avatarSetting = Settings("mocFile", Nil, None, None, Nil, Nil, Map.empty, motionGroups, Nil)

      And("a mocked motion manager expected start motion")
      val motionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        avatarSetting, stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        updater = mock[Updater]
      )

      When("Start a motion with group name and index")
      val result = updateStrategy.startMotion("group1", 1)

      Then("the result should have correct MotionWithTransition")
      result.value shouldBe mockedMotionWithTransition
    }

  }

  Feature("Update avatar model status") {
    Scenario("No effects and any motion") {
      Given("A motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      (expressionManager.calculateOperations _).when(*, *, *, *).returns(Nil)
      (motionManager.calculateOperations _).when(*, *, *, *).returns(Nil)

      And("A mocked Live2D model and updater with expectation")
      val model = mock[Live2DModel]
      val updater = mock[Updater]
      inSequence {
        (() => model.restoreParameters()).expects().once()
        (updater.executeOperations _).expects(Nil).once()   // Motions
        (() => model.snapshotParameters()).expects().once()
        (updater.executeOperations _).expects(Nil).once()   // Expressions
        (updater.executeOperations _).expects(Nil).once()   // Effects
        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        updater
      )

      When("update the avatar")
      val frameTimeInfo = stub[FrameTimeInfo]
      (() => frameTimeInfo.deltaTimeInSeconds).when().returns(0.33f)
      (() => frameTimeInfo.totalElapsedTimeInSeconds).when().returns(0.33f)
      updateStrategy.update(frameTimeInfo)

      Then("no exception should be thrown")
    }

    Scenario("There are effects") {
      Given("A motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      (expressionManager.calculateOperations _).when(*, *, *, *).returns(Nil)
      (motionManager.calculateOperations _).when(*, *, *, *).returns(Nil)

      And("Some stubbed effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val effectOperationList1 = ParameterValueUpdate("id1", 0.1f) :: ParameterValueAdd("id2", 0.2f) :: Nil
      val effectOperationList2 = ParameterValueMultiply("id3", 0.3f) :: ParameterValueUpdate("id4", 0.4f) :: Nil
      (stubbedEffect1.calculateOperations _).when(*, *, *).returns(effectOperationList1)
      (stubbedEffect2.calculateOperations _).when(*, *, *).returns(effectOperationList2)

      And("A mocked Live2D model and updater with expectation")
      val model = mock[Live2DModel]
      val updater = mock[Updater]
      inSequence {
        (() => model.restoreParameters()).expects().once()
        (updater.executeOperations _).expects(Nil).once()   // Motion
        (() => model.snapshotParameters()).expects().once()
        (updater.executeOperations _).expects(Nil).once()   // Expression
        (updater.executeOperations _).expects(effectOperationList1 ++ effectOperationList2) // Effect
        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        updater
      )

      And("assign the effects")
      updateStrategy.effects = List(stubbedEffect1, stubbedEffect2)

      When("update the avatar")
      val frameTimeInfo = stub[FrameTimeInfo]
      (() => frameTimeInfo.deltaTimeInSeconds).when().returns(0.33f)
      (() => frameTimeInfo.totalElapsedTimeInSeconds).when().returns(0.33f)
      updateStrategy.update(frameTimeInfo)

      Then("no exception should be thrown")
    }

    Scenario("There are motion / expression and effects") {

      Given("A motion / expression manager that return mocked operations")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]
      val expressionOperations = ParameterValueUpdate("id1", 0.1f) :: ParameterValueAdd("id2", 0.2f) :: Nil
      val motionOperations = ParameterValueMultiply("id3", 0.3f) :: ParameterValueUpdate("id4", 0.4f) :: Nil

      (expressionManager.calculateOperations _).when(*, *, *, *).returns(expressionOperations)
      (motionManager.calculateOperations _).when(*, *, *, *).returns(motionOperations)

      And("Some stubbed effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val effectOperationList1 = ParameterValueUpdate("id5", 0.1f) :: ParameterValueAdd("id6", 0.2f) :: Nil
      val effectOperationList2 = ParameterValueMultiply("id7", 0.3f) :: ParameterValueUpdate("id8", 0.4f) :: Nil
      (stubbedEffect1.calculateOperations _).when(*, *, *).returns(effectOperationList1)
      (stubbedEffect2.calculateOperations _).when(*, *, *).returns(effectOperationList2)

      And("A mocked Live2D model and updater with expectation")
      val model = mock[Live2DModel]
      val updater = mock[Updater]
      inSequence {
        (() => model.restoreParameters()).expects().once()
        (updater.executeOperations _).expects(motionOperations).once()   // Motion
        (() => model.snapshotParameters()).expects().once()
        (updater.executeOperations _).expects(expressionOperations).once()   // Expression
        (updater.executeOperations _).expects(effectOperationList1 ++ effectOperationList2) // Effect
        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        updater
      )

      And("assign the effects")
      updateStrategy.effects = List(stubbedEffect1, stubbedEffect2)

      When("update the avatar")
      val frameTimeInfo = stub[FrameTimeInfo]
      (() => frameTimeInfo.deltaTimeInSeconds).when().returns(0.33f)
      (() => frameTimeInfo.totalElapsedTimeInSeconds).when().returns(0.33f)
      updateStrategy.update(frameTimeInfo)

      Then("no exception should be thrown")
    }
  }
}
