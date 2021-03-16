package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.core.{CubismCore, ICubismCore}
import moe.brianhsu.live2d.core.types.{CsmVersion, MocVersion}
import moe.brianhsu.live2d.framework.model.Live2DModel
import moe.brianhsu.live2d.framework.util.MocFileReader

/**
 * The main Cubism class.
 *
 * @param core  The core library of Cubism
 */
class Cubism(core: ICubismCore) {

  def this() = this(new CubismCore)

  /**
   * The current version of Cubism Core C Library.
   *
   * @return  The current version of underlying C Library.
   */
  lazy val coreLibraryVersion: CsmVersion = CsmVersion(core.cLibrary.csmGetVersion())

  /**
   * Get the latest supported .moc file version.
   *
   * @return  The latest supported version.
   */
  lazy val latestSupportedMocVersion: MocVersion = MocVersion(core.cLibrary.csmGetLatestMocVersion())

  /**
   * Load .moc files to Live2DModel instance.
   *
   * @param   mocFilename   The .moc file to load into Live2DModel instance.
   * @param   textureFiles  The texture files that should be used to render this model.
   * @return                The corresponding Live2DModel instance.
   *
   * @throws  exception.MocNotRevivedException   Failed to revive moc file.
   */
  def loadModel(mocFilename: String, textureFiles: List[String]): Live2DModel = {
    val fileReader = new MocFileReader(core.memoryAllocator)
    val mocInfo = fileReader.readFile(mocFilename)
    new Live2DModel(mocInfo, textureFiles)(core)
  }
}
