package com.live2d.framework

import com.live2d.core.{CubismCore}
import com.live2d.core.types.{CsmVersion, MocVersion}
import com.live2d.framework.util.MocFileReader

/**
 * The main Cubism class.
 *
 * @param core  The core library of Cubism
 */
class Cubism(core: CubismCore) {

  def this() = this(new CubismCore)

  /**
   * Get current version of Cubism Core C Library.
   *
   * @return  The current version of underlying C Library.
   */
  def getCoreLibraryVersion() = CsmVersion(core.cLibrary.csmGetVersion())

  /**
   * Get the latest supported .moc file version.
   *
   * @return  The latest supported version.
   */
  def getLatestMocVersion() = MocVersion(core.cLibrary.csmGetLatestMocVersion())

  /**
   * Load .moc files to Live2DModel instance.
   *
   * @param   mocFilename   The .moc file to load into Live2DModel instance.
   * @return                The corresponding Live2DModel instance.
   *
   * @throws  exception.MocNotRevivedException   Failed to revive moc file.
   */
  def loadModel(mocFilename: String): Live2DModel = {
    val fileReader = new MocFileReader(core.memoryAllocator)
    val mocInfo = fileReader.readFile("Haru.moc3")
    new Live2DModel(mocInfo)(core)
  }
}
