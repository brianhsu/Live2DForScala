package moe.brianhsu.live2d.boundary.gateway.openSeeFace

import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData

import scala.util.Try

trait OpenSeeFaceDataReader extends AutoCloseable {
  def open(): Unit
  def readData(): Try[OpenSeeFaceData]
  def close(): Unit
}
