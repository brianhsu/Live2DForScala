package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.RepeatedCallback
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{Expression, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.{ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.updater.{FrameTimeInfo, Updater}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.MotionListener
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
        Given("a folder path contains json files for Haru Live2D avatar model")
        val folderPath = "src/test/resources/models/Haru"
        val jsonSettingsReader = new JsonSettingsReader(folderPath)

        And("settings come from that avatar model")
        val settings = jsonSettingsReader.loadSettings().success.value

        When("create BasicUpdateStrategy using simplified constructor")
        val updateStrategy = new BasicUpdateStrategy(settings, mock[Live2DModel], None)

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
        motionListener = None,
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
        motionListener = None,
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
        motionListener = None,
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
    Scenario("Start Motion using MotionSetting object and disable loop") {
      Given("a dummy MotionSetting")
      val motionSetting = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      And("a mocked motion manager expected start a motion")
      val motionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.repeatedCallbackHolder_= _)
        .expects(None)
        .once()

      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)
        .once()

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        motionListener = None,
        updater = mock[Updater]
      )

      When("start a motion")
      val motionWithTransition = updateStrategy.startMotion(motionSetting, isLoop = false)

      Then("the motion should be the correct value")
      motionWithTransition shouldBe mockedMotionWithTransition
    }

    Scenario("Start Motion using MotionSetting object and enable loop") {
      Given("a dummy MotionSetting")
      val motionSetting = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      And("a stubbed motion listener")
      val listener = stub[MotionListener]

      And("a mocked motion manager expected start a motion")
      val motionManager = mock[MotionManager]

      And("pretend the repeated callback will execute once")
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.repeatedCallbackHolder_= _)
        .expects(where { (callback: Option[RepeatedCallback]) => callback.isDefined })
        .onCall((callbackHolder: Option[RepeatedCallback]) => callbackHolder.foreach(_.apply(stub[MotionWithTransition])))
        .once()

      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)
        .once()

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        motionListener = Some(listener),
        updater = mock[Updater]
      )

      When("start a motion")
      val motionWithTransition = updateStrategy.startMotion(motionSetting, isLoop = true)

      Then("the motion should be the correct value")
      motionWithTransition shouldBe mockedMotionWithTransition

      And("the motionListener.onMotionStart should be called twice")
      (listener.onMotionStart _).verify(motionSetting).twice()

    }

    Scenario("Start Motion using MotionSetting object and enable loop - without listener") {
      Given("a dummy MotionSetting")
      val motionSetting = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      And("a mocked motion manager expected start a motion")
      val motionManager = mock[MotionManager]

      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.repeatedCallbackHolder_= _)
        .expects(None)
        .once()

      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)
        .once()

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        motionListener = None,
        updater = mock[Updater]
      )

      When("start a motion")
      val motionWithTransition = updateStrategy.startMotion(motionSetting, isLoop = true)

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
        motionListener = None,
        updater = mock[Updater]
      )

      When("start an non-exist group name")
      val result = updateStrategy.startMotion("unknownGroup", 0, isLoop = false)

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
          motionListener = None,
          updater = mock[Updater]
        )

        When("start an non-exist group name")
        val result = updateStrategy.startMotion("group1", index, isLoop = false)

        Then("the result should be None")
        result shouldBe None
      }
    }

    Scenario("The group name exist and index is correct and disable loop") {
      Given("a Avatar Setting that has motion group")
      val motionSetting1 = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)
      val motionSetting2 = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      val motionGroups = Map("group1" -> List(motionSetting1, motionSetting2))
      val avatarSetting = Settings("mocFile", Nil, None, None, Nil, Nil, Map.empty, motionGroups, Nil)

      And("a mocked motion manager expected start motion")
      val motionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.repeatedCallbackHolder_= _)
        .expects(None)
        .once()

      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)
        .once()

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        avatarSetting, stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        motionListener = None,
        updater = mock[Updater]
      )

      When("start a motion with group name and index")
      val result = updateStrategy.startMotion("group1", 1, isLoop = false)

      Then("the result should have correct MotionWithTransition")
      result.value shouldBe mockedMotionWithTransition
    }

    Scenario("The group name exist and index is correct and enable loop") {
      Given("a Avatar Setting that has motion group")
      val motionSetting1 = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)
      val motionSetting2 = MotionSetting("3", None, None, None, MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)

      val motionGroups = Map("group1" -> List(motionSetting1, motionSetting2))
      val avatarSetting = Settings("mocFile", Nil, None, None, Nil, Nil, Map.empty, motionGroups, Nil)

      And("a mocked motion manager expected start motion")
      val motionManager = mock[MotionManager]
      val mockedMotionWithTransition = stub[MotionWithTransition]
      (motionManager.repeatedCallbackHolder_= _)
        .expects(None)
        .once()

      (motionManager.startMotion: Motion => MotionWithTransition)
        .expects(*)
        .returns(mockedMotionWithTransition)
        .once()

      And("a BasicUpdateStrategy based on that motion manager")
      val updateStrategy = new BasicUpdateStrategy(
        avatarSetting, stub[Live2DModel],
        stub[AvatarExpressionReader],
        expressionManager = mock[MotionManager],
        motionManager,
        motionListener = None,
        updater = mock[Updater]
      )

      When("start a motion with group name and index")
      val result = updateStrategy.startMotion("group1", 1, isLoop = true)

      Then("the result should have correct MotionWithTransition")
      result.value shouldBe mockedMotionWithTransition
    }

  }

  Feature("Access the effect list") {
    Scenario("Ask for all before / after expression effects") {
      Given("a motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      And("A stubbed Live2D model and updater")
      val model = stub[Live2DModel]
      val updater = stub[Updater]

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("add some effects to before / after expression effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val stubbedEffect3 = stub[Effect]
      val stubbedEffect4 = stub[Effect]

      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil, BeforeExpression)
      updateStrategy.appendAndStartEffects(stubbedEffect3 :: stubbedEffect4 :: Nil, AfterExpression)

      When("ask for before expression effects")
      Then("it should contains correct effects")
      updateStrategy.effects(BeforeExpression) should contain theSameElementsInOrderAs List(stubbedEffect1, stubbedEffect2)

      When("ask for after expression effects")
      Then("it should contains correct effects")
      updateStrategy.effects(AfterExpression) should contain theSameElementsInOrderAs List(stubbedEffect3, stubbedEffect4)

    }

    Scenario("Find after / before expression effects") {
      Given("a motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      And("A stubbed Live2D model and updater")
      val model = stub[Live2DModel]
      val updater = stub[Updater]

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("add some effects to before / after expression effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val stubbedEffect3 = stub[Effect]
      val stubbedEffect4 = stub[Effect]

      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil, BeforeExpression)
      updateStrategy.appendAndStartEffects(stubbedEffect3 :: stubbedEffect4 :: Nil, AfterExpression)

      When("filter out before expression effects")
      val result1 = updateStrategy.findEffects(_ == stubbedEffect2, BeforeExpression)

      Then("it should contains correct effects")
      result1 should contain theSameElementsInOrderAs List(stubbedEffect2)

      When("ask for after expression effects")
      val result2 = updateStrategy.findEffects(_ == stubbedEffect3, AfterExpression)

      Then("it should contains correct effects")
      result2 should contain theSameElementsInOrderAs List(stubbedEffect3)
    }

  }

  Feature("Stop and remove effect from the effect list") {
    Scenario("Remove effect that execute before expression") {
      Given("a motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      And("A stubbed Live2D model and updater")
      val model = stub[Live2DModel]
      val updater = stub[Updater]

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("add some effects to before / after expression effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val stubbedEffect3 = stub[Effect]
      val stubbedEffect4 = stub[Effect]

      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil, BeforeExpression)
      updateStrategy.appendAndStartEffects(stubbedEffect3 :: stubbedEffect4 :: Nil, AfterExpression)

      When("remove the stubbedEffect2 from before expression effects")
      val removedEffects = updateStrategy.stopAndRemoveEffects(_ == stubbedEffect2, BeforeExpression)

      Then("it should return the removed effects")
      removedEffects shouldBe List(stubbedEffect2)

      And("the remain effect list should not contain the removed effect")
      updateStrategy.effects(BeforeExpression) shouldBe List(stubbedEffect1)

      And("the removed effect should be stopped")
      (() => stubbedEffect2.stop()).verify().once()

      And("other effects should not be touched")
      (() => stubbedEffect1.stop()).verify().never()
      (() => stubbedEffect3.stop()).verify().never()
      (() => stubbedEffect4.stop()).verify().never()

    }

    Scenario("Remove effect that execute after expression") {
      Given("a motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      And("A stubbed Live2D model and updater")
      val model = stub[Live2DModel]
      val updater = stub[Updater]

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("add some effects to before / after expression effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val stubbedEffect3 = stub[Effect]
      val stubbedEffect4 = stub[Effect]

      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil, BeforeExpression)
      updateStrategy.appendAndStartEffects(stubbedEffect3 :: stubbedEffect4 :: Nil, AfterExpression)

      When("remove the stubbedEffect4 from before expression effects")
      val removedEffects = updateStrategy.stopAndRemoveEffects(_ == stubbedEffect4, AfterExpression)

      Then("it should return the removed effects")
      removedEffects shouldBe List(stubbedEffect4)

      And("the remain effect list should not contain the removed effect")
      updateStrategy.effects(AfterExpression) shouldBe List(stubbedEffect3)

      And("the removed effect should be stopped")
      (() => stubbedEffect4.stop()).verify().once()

      And("other effects should not be touched")
      (() => stubbedEffect1.stop()).verify().never()
      (() => stubbedEffect2.stop()).verify().never()
      (() => stubbedEffect3.stop()).verify().never()
    }

  }

  Feature("Update avatar model status") {
    Scenario("No effects and any motion") {
      Given("a motion / expression manager that does nothing")
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
        (updater.executeOperations _).expects(Nil).once()   // Effects before expression
        (updater.executeOperations _).expects(Nil).once()   // Expressions
        (updater.executeOperations _).expects(Nil).once()   // Effects after expression

        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
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
      Given("a motion / expression manager that does nothing")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]

      (expressionManager.calculateOperations _).when(*, *, *, *).returns(Nil)
      (motionManager.calculateOperations _).when(*, *, *, *).returns(Nil)

      And("some stubbed effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val effectOperationList1 = ParameterValueUpdate("id1", 0.1f) :: ParameterValueAdd("id2", 0.2f) :: Nil
      val effectOperationList2 = ParameterValueMultiply("id3", 0.3f) :: ParameterValueUpdate("id4", 0.4f) :: Nil
      (stubbedEffect1.calculateOperations _).when(*, *, *).returns(effectOperationList1)
      (stubbedEffect2.calculateOperations _).when(*, *, *).returns(effectOperationList2)

      And("a mocked Live2D model and updater with expectation")
      val model = mock[Live2DModel]

      val updater = mock[Updater]
      inSequence {
        (() => model.restoreParameters()).expects().once()
        (updater.executeOperations _).expects(Nil).once()   // Motion
        (() => model.snapshotParameters()).expects().once()
        (updater.executeOperations _).expects(effectOperationList1 ++ effectOperationList2) // Effect before expression
        (updater.executeOperations _).expects(Nil).once()   // Expression
        (updater.executeOperations _).expects(Nil).once()   // Effects after expression

        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("assign the effects")
      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil)

      When("update the avatar")
      val frameTimeInfo = stub[FrameTimeInfo]
      (() => frameTimeInfo.deltaTimeInSeconds).when().returns(0.33f)
      (() => frameTimeInfo.totalElapsedTimeInSeconds).when().returns(0.33f)
      updateStrategy.update(frameTimeInfo)

      And("the effects should be started")
      (() => stubbedEffect1.start()).verify().once()
      (() => stubbedEffect2.start()).verify().once()
    }

    Scenario("There are motion / expression and effects") {

      Given("a motion / expression manager that return mocked operations")
      val expressionManager = stub[MotionManager]
      val motionManager = stub[MotionManager]
      val expressionOperations = ParameterValueUpdate("id1", 0.1f) :: ParameterValueAdd("id2", 0.2f) :: Nil
      val motionOperations = ParameterValueMultiply("id3", 0.3f) :: ParameterValueUpdate("id4", 0.4f) :: Nil

      (expressionManager.calculateOperations _).when(*, *, *, *).returns(expressionOperations)
      (motionManager.calculateOperations _).when(*, *, *, *).returns(motionOperations)

      And("some stubbed effects")
      val stubbedEffect1 = stub[Effect]
      val stubbedEffect2 = stub[Effect]
      val stubbedEffect3 = stub[Effect]
      val stubbedEffect4 = stub[Effect]

      val effectOperationList1 = ParameterValueUpdate("id5", 0.1f) :: ParameterValueAdd("id6", 0.2f) :: Nil
      val effectOperationList2 = ParameterValueMultiply("id7", 0.3f) :: ParameterValueUpdate("id8", 0.4f) :: Nil
      val effectOperationList3 = ParameterValueUpdate("id9", 0.1f) :: ParameterValueAdd("id10", 0.2f) :: Nil
      val effectOperationList4 = ParameterValueMultiply("id11", 0.3f) :: ParameterValueUpdate("id12", 0.4f) :: Nil

      (stubbedEffect1.calculateOperations _).when(*, *, *).returns(effectOperationList1)
      (stubbedEffect2.calculateOperations _).when(*, *, *).returns(effectOperationList2)
      (stubbedEffect3.calculateOperations _).when(*, *, *).returns(effectOperationList3)
      (stubbedEffect4.calculateOperations _).when(*, *, *).returns(effectOperationList4)

      And("a mocked Live2D model and updater with expectation")
      val model = mock[Live2DModel]
      val updater = mock[Updater]
      inSequence {
        (() => model.restoreParameters()).expects().once()
        (updater.executeOperations _).expects(motionOperations).once()   // Motion
        (() => model.snapshotParameters()).expects().once()
        (updater.executeOperations _).expects(effectOperationList1 ++ effectOperationList2) // Effect before expression
        (updater.executeOperations _).expects(expressionOperations).once()   // Expression
        (updater.executeOperations _).expects(effectOperationList3 ++ effectOperationList4).once() // Effects after expression
        (() => model.update()).expects().once()
      }

      And("a BasicUpdateStrategy based on that model")
      val updateStrategy = new BasicUpdateStrategy(
        stub[Settings], model,
        stub[AvatarExpressionReader],
        expressionManager, motionManager,
        motionListener = None,
        updater
      )

      And("assign the effects")
      updateStrategy.appendAndStartEffects(stubbedEffect1 :: stubbedEffect2 :: Nil, BeforeExpression)
      updateStrategy.appendAndStartEffects(stubbedEffect3 :: stubbedEffect4 :: Nil, AfterExpression)

      When("update the avatar")
      val frameTimeInfo = stub[FrameTimeInfo]
      (() => frameTimeInfo.deltaTimeInSeconds).when().returns(0.33f)
      (() => frameTimeInfo.totalElapsedTimeInSeconds).when().returns(0.33f)
      updateStrategy.update(frameTimeInfo)

      And("the effects should be started")
      (() => stubbedEffect1.start()).verify().once()
      (() => stubbedEffect2.start()).verify().once()
      (() => stubbedEffect3.start()).verify().once()
      (() => stubbedEffect4.start()).verify().once()

    }
  }
}
