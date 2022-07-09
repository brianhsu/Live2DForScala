package moe.brianhsu.live2d.boundary.gateway.openSeeFace

import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData

trait OpenSeeFaceDataReader extends AutoCloseable {
  def open(): Unit
  def readData(): OpenSeeFaceData
  def close(): Unit
}
