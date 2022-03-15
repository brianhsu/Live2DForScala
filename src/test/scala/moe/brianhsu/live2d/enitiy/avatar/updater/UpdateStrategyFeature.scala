package moe.brianhsu.live2d.enitiy.avatar.updater

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter, Part}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class UpdateStrategyFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  Feature("Update model according to command operation") {
    Scenario("The operation list is empty") {
      Given("A dummy UpdateStrategy")
      val strategy = new DummyUpdateStrategy

      And("a mocked model do not expect any command")
      val model = mock[Live2DModel]

      When("executeOperations with empty command list to that model")
      strategy.executeOperations(model, Nil)

      Then("it should do nothing")
    }

    Scenario("The operation contains multiple operations that access normal parameter") {
      Given("A dummy UpdateStrategy")
      val strategy = new DummyUpdateStrategy

      And("a stubbed model with some stubbed parameters")
      val model = stub[Live2DModel]
      val mockedParameterId1 = stub[Parameter]
      val mockedParameterId2 = stub[Parameter]
      val mockedParameterId3 = stub[Parameter]
      val mockedParameters = Map(
        "id1" -> mockedParameterId1,
        "id2" -> mockedParameterId2,
        "id3" -> mockedParameterId3
      )
      (() => model.parameters).when().returns(mockedParameters)

      When("executeOperations with a operation list to that model")
      strategy.executeOperations(
        model,
        List(
          ParameterValueAdd("id1", 0.1f, 0.2f),
          ParameterValueUpdate("id2", 0.3f, 0.4f),
          ParameterValueMultiply("id3", 0.5f, 0.6f),
          ParameterValueAdd("id3", 0.7f, 0.8f),
          ParameterValueUpdate("id2", 0.9f, 1.0f),
          ParameterValueMultiply("id1", 1.1f, 1.2f),
        )
      )

      Then("it should delegated it to parameter correctly")
      inSequence {
        (mockedParameterId1.add _).verify(0.1f, 0.2f).once()
        (mockedParameterId2.update _).verify(0.3f, 0.4f).once()
        (mockedParameterId3.multiply _).verify(0.5f, 0.6f).once()
        (mockedParameterId3.add _).verify(0.7f, 0.8f).once()
        (mockedParameterId2.update _).verify(0.9f, 1.0f).once()
        (mockedParameterId1.multiply _).verify(1.1f, 1.2f).once()
      }
    }

    Scenario("The operation contains multiple operations that access fallback parameter") {
      Given("A dummy UpdateStrategy")
      val strategy = new DummyUpdateStrategy

      And("a stubbed model with some stubbed parameters")
      val mockedParameterId1 = stub[Parameter]
      val mockedParameterId2 = stub[Parameter]
      val model = stub[Live2DModel]
      (model.parameterWithFallback _).when("id1").returns(mockedParameterId1)
      (model.parameterWithFallback _).when("id2").returns(mockedParameterId2)

      When("executeOperations with a operation list to that model")
      strategy.executeOperations(
        model,
        List(
          FallbackParameterValueAdd("id1", 0.1f, 0.2f),
          FallbackParameterValueUpdate("id2", 0.3f, 0.4f),
          FallbackParameterValueAdd("id2", 0.5f, 0.6f),
          FallbackParameterValueUpdate("id1", 0.7f, 0.8f),
        )
      )

      Then("it should delegated it to parameter correctly")
      inSequence {
        (mockedParameterId1.add _).verify(0.1f, 0.2f).once()
        (mockedParameterId2.update _).verify(0.3f, 0.4f).once()
        (mockedParameterId2.add _).verify(0.5f, 0.6f).once()
        (mockedParameterId1.update _).verify(0.7f, 0.8f).once()
      }
    }

    Scenario("The operation contains multiple operations that access parts") {
      Given("A dummy UpdateStrategy")
      val strategy = new DummyUpdateStrategy

      And("a stubbed model with some stubbed parts")
      val mockedPart1 = stub[Part]
      val mockedPart2 = stub[Part]
      val parts = Map("id1" -> mockedPart1, "id2" -> mockedPart2)
      val model = stub[Live2DModel]
      (() => model.parts).when().returns(parts)

      When("executeOperations with a operation list to that model")
      strategy.executeOperations(
        model,
        List(
          PartOpacityUpdate("id1", 0.1f),
          PartOpacityUpdate("id2", 0.2f),
          PartOpacityUpdate("id2", 0.3f),
          PartOpacityUpdate("id1", 0.4f),
        )
      )

      Then("it should delegated it to parameter correctly")
      inSequence {
        (mockedPart1.opacity_= _).verify(0.1f).once()
        (mockedPart2.opacity_= _).verify(0.2f).once()
        (mockedPart2.opacity_= _).verify(0.3f).once()
        (mockedPart1.opacity_= _).verify(0.4f).once()
      }
    }

  }

  class DummyUpdateStrategy extends UpdateStrategy {
    override def update(frameTimeInfo: FrameTimeInfo): Unit = {}
    override def executeOperations(model: Live2DModel, operations: List[EffectOperation]): Unit = {
      super.executeOperations(model, operations)
    }
  }
}
