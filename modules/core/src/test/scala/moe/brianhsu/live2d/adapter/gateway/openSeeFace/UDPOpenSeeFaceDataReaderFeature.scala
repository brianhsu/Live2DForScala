package moe.brianhsu.live2d.adapter.gateway.openSeeFace

import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import moe.brianhsu.live2d.exception.OpenSeeFaceException
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.net._
import scala.io.Source
import scala.util.Using

class UDPOpenSeeFaceDataReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  Feature("Create the reader") {
    Scenario("Create from only hostname and port") {
      When("create UDPOpenSeeFaceDataReader using hostname and port")
      Then("no exception should be thrown")
      noException shouldBe thrownBy {
        And("a reader based on that socket")
        Using.Manager { implicit use =>
          new UDPOpenSeeFaceDataReader("localhost", 11572)
        }.get
      }
    }
  }

  Feature("Open the socket") {
    Scenario("Open and bind the UDP socket successfully") {

      Given("a stubbed socket")
      val socket = stub[MockableDatagramSocket]

      And("a reader based on that socket")
      val reader = Using.Manager { implicit use =>
        new UDPOpenSeeFaceDataReader(socket, "localhost", 11572)
      }.get

      When("open the socket")
      reader.open()

      Then("the socket should be bind to the hostname and port")
      (socket.bind _).verify(where { address: SocketAddress =>
        address.asInstanceOf[InetSocketAddress].getHostName == "localhost" &&
          address.asInstanceOf[InetSocketAddress].getPort == 11572
      }).once()
    }

    Scenario("The socket is already bind") {

      Given("a stubbed socket")
      val socket = new MockableDatagramSocket {
        override def bind(addr: SocketAddress): Unit = {
          throw new SocketException("Cannot bind")
        }
      }

      And("a reader based on that socket")
      val reader = Using.Manager { implicit use =>
        new UDPOpenSeeFaceDataReader(socket, "localhost", 11572)
      }.get

      Then("it should throw a OpenSeeFaceException")
      a[OpenSeeFaceException] shouldBe thrownBy {
        When("open the socket")
        reader.open()
      }

    }
  }

  Feature("Convert OpenSeeFaceData from UDP datagram packet") {

    Scenario("Read data from datagram packet") {

      val expectedDataList = Using.Manager { use =>
        val inputStream = use(this.getClass.getResourceAsStream("/expectation/OpenSeeFaceDataExpectation.json"))
        val source = use(Source.fromInputStream(inputStream))
        val lines = source.getLines().toList
        implicit val jsonFormats: DefaultFormats.type = DefaultFormats
        lines.map { line => JsonMethods.parse(line).extract[ExpectedData] }
      }.get


      expectedDataList.foreach { expectation =>
        Given("a stubbed socket return some bytes when requested")
        val stubbedSocket = stub[MockableDatagramSocket]
        (stubbedSocket.receive _).when(*).onCall { packet: DatagramPacket =>
          expectation.rawBytes.zipWithIndex.foreach { case (byte, index) =>
            packet.getData.update(index, byte)
          }
        }


        Using.Manager { implicit use =>

          And("a UDPOpenSeeFaceDataReader based on that socket")
          val reader = new UDPOpenSeeFaceDataReader(stubbedSocket, "localhost", 11527)

          When("Read the data from it")
          val openSeeFaceData = reader.readData()

          Then("it should be the same as in the expectation")
          openSeeFaceData shouldBe expectation.openSeeFaceData
        }.get
      }
    }
  }

  Scenario("Close the UDP socket") {

    Given("a stubbed socket")
    val socket = stub[MockableDatagramSocket]

    And("a reader based on that socket")
    val reader = Using.Manager { implicit use => use(new UDPOpenSeeFaceDataReader(socket, "localhost", 11572)) }.get

    When("close the socket")
    reader.close()

    Then("the socket should be closed")
    (() => socket.close()).verify().atLeastOnce()
  }

  Feature("Close the socket") {
    Scenario("Close the socket by auto resource manager") {
      Given("a stubbed socket")
      val socket = stub[MockableDatagramSocket]

      And("create the instance by UsingManager")
      Using.Manager { implicit use =>
        new UDPOpenSeeFaceDataReader(socket, "localhost", 11572)
      }.get

      Then("the socket should be closed automatically")
      (() => socket.close()).verify().once()
    }
  }

  case class ExpectedData(rawBytes: Array[Byte], openSeeFaceData: OpenSeeFaceData)

  class MockableDatagramSocket extends DatagramSocket(null: SocketAddress)

}
