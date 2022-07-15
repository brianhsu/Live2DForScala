package moe.brianhsu.live2d.demo.openSeeFace

import moe.brianhsu.live2d.adapter.gateway.openSeeFace.UDPOpenSeeFaceDataReader
import moe.brianhsu.live2d.boundary.gateway.openSeeFace.OpenSeeFaceDataReader
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.Try

object ExternalOpenSeeFaceDataReader {
  def apply(command: String, hostname: String, port: Int, onDataRead: OpenSeeFaceData => Unit): ExternalOpenSeeFaceDataReader = {
    val process = new ProcessBuilder(command.split(" "):_*)
      .redirectErrorStream(true)
      .start()

    new ExternalOpenSeeFaceDataReader(process, hostname, port, onDataRead)
  }
}

class ExternalOpenSeeFaceDataReader(process: Process, hostname: String, port: Int, onDataRead: OpenSeeFaceData => Unit) extends OpenSeeFaceDataReader {

  private val udpDataReader = new UDPOpenSeeFaceDataReader(hostname, port, 10)

  def isProcessAlive: Boolean = process.isAlive

  def ensureStarted(): Try[ExternalOpenSeeFaceDataReader] = Try {
    var count = 0
    var isFail = false

    while (!isFail && count < 10) {
      isFail = Try(process.exitValue).map(x => x != 0 && x != 124).getOrElse(false)
      count += 1
      Thread.sleep(50)
    }

    if (isFail) {
      throw new Exception(s"Cannot start OpenSeeFace, exitValue=${process.exitValue()}")
    }

    this
  }

  override def open(): Unit = {
    this.udpDataReader.open()
  }

  override def readData(): Try[OpenSeeFaceData] = {
    val data = this.udpDataReader.readData()
    data.foreach(onDataRead)
    Future { Source.fromInputStream(process.getInputStream).mkString }
    data
  }

  override def close(): Unit = {
    if (process.isAlive) {
      process.destroy()
    }

    this.udpDataReader.close()
  }
}
