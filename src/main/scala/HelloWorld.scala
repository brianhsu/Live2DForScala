

import com.live2d.core.{CubismCore}
import com.live2d.core.types.ModelAlignment
import com.live2d.core.utils.DefaultMemoryAllocator
import com.live2d.framework.util.MocFileReader
import com.sun.jna._

case class Moc(size: Int, buffer: Memory)

object HelloWorld {

  val allocator = new DefaultMemoryAllocator
  val fileReader = new MocFileReader(allocator)

  def main(args: Array[String]): Unit = {
    val core = new CubismCore(x => println(x))
    val coreLib = core.cLibrary
    val mocInfo = fileReader.readFile("Haru.moc3")
    val moc = coreLib.csmReviveMocInPlace(mocInfo.memory, mocInfo.originalSize)
    val modelSize = coreLib.csmGetSizeofModel(moc)
    val modelMemory = allocator.allocate(modelSize, ModelAlignment)
    val modelPointer = coreLib.csmInitializeModelInPlace(moc, modelMemory, modelSize)
    coreLib.csmUpdateModel(modelPointer)
    val count = coreLib.csmGetDrawableCount(modelPointer)
    val indexCounts = coreLib.csmGetDrawableIndexCounts(modelPointer)
    val shorts = coreLib.csmGetDrawableIndices(modelPointer)
    for (i <- 0 until count) {
      val indexCount = indexCounts.apply(i)
      println(s"vecCount[$i]: $indexCount")
      for (j <- 0 until indexCount) {
        println(s"  shorts.[$i][$j] = ${shorts(i)(j)}")
      }
    }


  }
}
