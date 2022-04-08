package moe.brianhsu.porting

import moe.brianhsu.live2d.adapter.gateway.avatar.physics.AvatarPhysicsReader
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect._
import moe.brianhsu.live2d.enitiy.avatar.physics
import moe.brianhsu.live2d.enitiy.avatar.physics.data.{ParticleUpdateParameter, PhysicsNormalization, PhysicsParticle, PhysicsEffect}
import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysics
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate, UpdateOperation}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel, Parameter}
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, OptionValues, TryValues}

import scala.io.Source
import scala.util.Using

class PhysicsFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues
  with MockFactory with OptionValues with TableDrivenPropertyChecks {
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
    Scenario("s1") {
      Given("A folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Rice"

      When("Create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      physics.windDirection = EuclideanVector(10.0f, 10.0f)

      val testDataFile = Source.fromFile("src/test/resources/expectation/physicsOperations.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { dataPoint =>
        val model = createStubbedModel(dataPoint)
        val operations = physics.evaluate(model, dataPoint.totalElapsedTimeInSeconds, dataPoint.deltaTimeSeconds)
        operations.size shouldBe dataPoint.operations.size
        operations should contain theSameElementsInOrderAs dataPoint.operations
      }
    }
    Scenario("s2") {
      Given("A folder path contains json files for Rice Live2D avatar model")
      val folderPath = "src/test/resources/models/Hiyori"

      When("Create a Physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val physics = new AvatarPhysicsReader(settings).loadPhysics.value

      physics.windDirection = EuclideanVector(10.0f, 10.0f)

      val testDataFile = Source.fromFile("src/test/resources/expectation/HiyoriPhysic.json")
      val dataPointList = Using.resource(testDataFile) { _.getLines().toList.map(parseLog) }

      dataPointList.foreach { dataPoint =>
        val model = createStubbedModel(dataPoint)
        val operations = physics.evaluate(model, dataPoint.totalElapsedTimeInSeconds, dataPoint.deltaTimeSeconds)
        operations.size shouldBe dataPoint.operations.size
        operations should contain theSameElementsInOrderAs dataPoint.operations
      }
    }

  }

  Feature("UpdateParticles") {
    Scenario("delay is not 0") {
      val particle1 = PhysicsParticle(
        mobility = 1,
        delay = 1,
        acceleration = 1,
        radius = 0,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(0, 0),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particle2 = physics.data.PhysicsParticle(
        mobility = 0.95f,
        delay = 0.8f,
        acceleration = 1.5f,
        radius = 15,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(1f, 15),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particles = List(particle1, particle2)
      val setting = PhysicsEffect(
        PhysicsNormalization(0, 2000, 0),
        PhysicsNormalization(0, 1000, 0),
        Nil, Nil, particles
      )

      val List(updatedParticle1, updateParticle2) = setting.calculateNewParticleStatus(
        ParticleUpdateParameter(EuclideanVector(0, 0), 0.0f),
        EuclideanVector(0, 0),
        0.333f,
        0.1f
      )

      updatedParticle1.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.mobility shouldBe 1.0f
      updatedParticle1.delay shouldBe 1.0f
      updatedParticle1.acceleration shouldBe 1.0f
      updatedParticle1.radius shouldBe 0.0f
      updatedParticle1.position shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastGravity shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.velocity shouldBe EuclideanVector(0.0f, 0.0f)

      updateParticle2.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updateParticle2.mobility shouldBe 0.95f
      updateParticle2.delay shouldBe 0.8f
      updateParticle2.acceleration shouldBe 1.5f
      updateParticle2.radius shouldBe 15.0f
      updateParticle2.position shouldBe EuclideanVector(0.0f, 14.998851f)
      updateParticle2.lastPosition shouldBe EuclideanVector(1.0f, 15.0f)
      updateParticle2.lastGravity shouldBe EuclideanVector(0.0f, 1.0f)
      updateParticle2.velocity shouldBe EuclideanVector(-0.118868865f, -1.3660143E-4f)
    }

    Scenario("delay is 0") {
      val particle1 = physics.data.PhysicsParticle(
        mobility = 1,
        delay = 1,
        acceleration = 1,
        radius = 0,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(0, 0),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particle2 = physics.data.PhysicsParticle(
        mobility = 0.95f,
        delay = 0f,
        acceleration = 1.5f,
        radius = 15,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(1f, 15),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particles = List(particle1, particle2)
      val setting = PhysicsEffect(
        PhysicsNormalization(0, 2000, 0),
        PhysicsNormalization(0, 1000, 0),
        Nil, Nil, particles
      )

      val List(updatedParticle1, updateParticle2) = setting.calculateNewParticleStatus(
        physics.data.ParticleUpdateParameter(EuclideanVector(0, 0), 0.0f),
        windDirection = EuclideanVector(0, 0),
        deltaTimeSeconds = 0.0f,
        0.1f
      )

      updatedParticle1.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.mobility shouldBe 1.0f
      updatedParticle1.delay shouldBe 1.0f
      updatedParticle1.acceleration shouldBe 1.0f
      updatedParticle1.radius shouldBe 0.0f
      updatedParticle1.position shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastGravity shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.velocity shouldBe EuclideanVector(0.0f, 0.0f)

      updateParticle2.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updateParticle2.mobility shouldBe 0.95f
      updateParticle2.delay shouldBe 0.0f
      updateParticle2.acceleration shouldBe 1.5f
      updateParticle2.radius shouldBe 15.0f
      updateParticle2.position shouldBe EuclideanVector(0.0f, -14.966778f)
      updateParticle2.lastPosition shouldBe EuclideanVector(1.0f, 15.0f)
      updateParticle2.lastGravity shouldBe EuclideanVector(0.0f, 1.0f)
      updateParticle2.velocity shouldBe EuclideanVector(0.0f, 0.0f)
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

  case class CurrentParameter(current: Float, min: Float, max: Float, default: Float)
  case class LogData(
    totalElapsedTimeInSeconds: Float,
    deltaTimeSeconds: Float,
    parameters: Map[String, CurrentParameter],
    operations: List[UpdateOperation]
  )

}
