package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.openSeeFace.OpenSeeFaceDataReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.OpenSeeFaceDataConverter
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.TrackingNode
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.OpenSeeFaceTracking.DefaultTrackingTaps
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

class OpenSeeFaceTrackingFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory{

  private implicit val jsonFormats: DefaultFormats.type = DefaultFormats

  Feature("Start the reader thread") {
    Scenario("Start the reader thread") {
      Given("a stubbed data reader returns the dummy data")
      val stubbedData = Using.Manager { use =>
        val inputStream = use(this.getClass.getResourceAsStream("/mockData/openSeeFaceData.json"))
        val jsonData = use(Source.fromInputStream(inputStream)).mkString
        JsonMethods.parse(jsonData).extract[OpenSeeFaceData]
      }
      val dataReader = new StubbedOpenFaceDataReader(stubbedData.get)
      val converter = stub[OpenSeeFaceDataConverter]
      val stubbedTrackingNode = TrackingNode(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode)

      When("create a OpenSeeFaceTracking based on this dataReader")
      val tracking = new OpenSeeFaceTracking(dataReader, 1000, DefaultTrackingTaps, converter)

      And("start it")
      tracking.start()
      Thread.sleep(500) // Make sure the thread is started

      Then("the thread should be started")
      tracking.readerThread.isAlive shouldBe true

      And("the data reader should be opened")
      dataReader.isOpened shouldBe true

      When("stop the tracking")
      tracking.stop()

      Then("the data reader should be closed")
      dataReader.isClosed shouldBe true

      And("the readerThread should be stopped")
      tracking.readerThread.isAlive shouldBe false
    }
  }

  Feature("Add to tracking nodes with idle check") {
    Scenario("Add fewer nodes than maxTapping") {
      Given("a data reader that return only 3 nodes")
      val stubbedData = Using.Manager { use =>
        val inputStream = use(this.getClass.getResourceAsStream("/mockData/openSeeFaceData.json"))
        val jsonData = use(Source.fromInputStream(inputStream)).mkString
        JsonMethods.parse(jsonData).extract[OpenSeeFaceData]
      }

      val dataReader = new StubbedOpenFaceDataReader(stubbedData.get, 3)

      And("a data converter that converts OpenSeeFace data to Tracking Node")
      val converter = stub[OpenSeeFaceDataConverter]
      val stubbedTrackingNode1 = TrackingNode(1.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode2 = TrackingNode(2.2f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode3 = TrackingNode(3.3f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)

      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode1).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode2).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode3).noMoreThanOnce()

      When("create a OpenSeeFaceTracking based on this dataReader, and max requested tapping is 10")
      val trackingTaps = DefaultTrackingTaps.copy(maxTaps = 10)
      val tracking = new OpenSeeFaceTracking(dataReader, 1000, trackingTaps, converter)

      And("start it and wait for 0.1 seconds")
      tracking.start()
      Thread.sleep(10)
      tracking.stop()

      Then("the trackingNodes should contain all the nodes")
      tracking.trackingNoes should contain theSameElementsInOrderAs List(stubbedTrackingNode3, stubbedTrackingNode2, stubbedTrackingNode1)
    }

    Scenario("Add more nodes than maxTapping") {
      Given("a data reader that return only 5 nodes")
      val stubbedData = Using.Manager { use =>
        val inputStream = use(this.getClass.getResourceAsStream("/mockData/openSeeFaceData.json"))
        val jsonData = use(Source.fromInputStream(inputStream)).mkString
        JsonMethods.parse(jsonData).extract[OpenSeeFaceData]
      }

      val dataReader = new StubbedOpenFaceDataReader(stubbedData.get, 5)

      And("a data converter that converts OpenSeeFace data to Tracking Node")
      val converter = stub[OpenSeeFaceDataConverter]
      val stubbedTrackingNode1 = TrackingNode(1.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode2 = TrackingNode(2.2f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode3 = TrackingNode(3.3f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode4 = TrackingNode(4.3f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode5 = TrackingNode(5.3f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)

      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode1).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode2).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode3).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode4).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode5).noMoreThanOnce()

      When("create a OpenSeeFaceTracking based on this dataReader, and max requested tapping is 10")
      val trackingTaps = DefaultTrackingTaps.copy(maxTaps = 3)
      val tracking = new OpenSeeFaceTracking(dataReader, 1000, trackingTaps, converter)

      And("start it and wait for 0.1 seconds")
      tracking.start()
      Thread.sleep(10)
      tracking.stop()

      Then("the trackingNodes should contain only last 3 nodes")
      tracking.trackingNoes should contain theSameElementsInOrderAs List(stubbedTrackingNode5, stubbedTrackingNode4, stubbedTrackingNode3)
    }

    Scenario("Idle longer than expected") {
      Given("a data reader that return only 3 nodes")
      val stubbedData = Using.Manager { use =>
        val inputStream = use(this.getClass.getResourceAsStream("/mockData/openSeeFaceData.json"))
        val jsonData = use(Source.fromInputStream(inputStream)).mkString
        JsonMethods.parse(jsonData).extract[OpenSeeFaceData]
      }

      val dataReader = new StubbedOpenFaceDataReader(stubbedData.get, 3)

      And("a data converter that converts OpenSeeFace data to Tracking Node")
      val converter = stub[OpenSeeFaceDataConverter]
      val stubbedTrackingNode1 = TrackingNode(1.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode2 = TrackingNode(2.2f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)
      val stubbedTrackingNode3 = TrackingNode(3.3f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f)

      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode1).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode2).noMoreThanOnce()
      (converter.convert _).when(*, *, *).returns(stubbedTrackingNode3).noMoreThanOnce()

      When("create a OpenSeeFaceTracking based on this dataReader, and considered idle after 100ms")
      val tracking = new OpenSeeFaceTracking(dataReader, 100, DefaultTrackingTaps, converter)

      And("start it and wait for 200ms seconds")
      tracking.start()
      Thread.sleep(200)
      tracking.stop()

      Then("the trackingNodes should clear to Nil")
      tracking.trackingNoes shouldBe Nil

    }

  }


  class StubbedOpenFaceDataReader(data: OpenSeeFaceData, maxCount: Int = 3) extends OpenSeeFaceDataReader {
    var isOpened: Boolean = false
    var isClosed: Boolean = false
    var currentReadCount: Int = 1

    override def open(): Unit = {
      isOpened = true
    }

    override def readData(): Try[OpenSeeFaceData] = {
      if (currentReadCount > maxCount) {
        Failure(new Exception("Exceed max read count"))
      } else {
        currentReadCount += 1
        Success(data)
      }
    }

    override def close(): Unit = {
      isClosed = true
    }
  }
}
