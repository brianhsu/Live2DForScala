package moe.brianhsu.live2d.framework.effect

import scala.collection.mutable.ListBuffer

case class PartData(partId: String, parameterIndex: Int, partIndex: Int, link: ListBuffer[PartData])
