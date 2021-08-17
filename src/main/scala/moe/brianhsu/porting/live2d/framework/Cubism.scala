package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.adapter.gateway.core.JnaCubismCore
import moe.brianhsu.live2d.adapter.gateway.model.{CubismModelBackend, MocInfoFileReader}
import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.enitiy.core.types.{CsmVersion, MocVersion}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.model.Avatar

import java.io.FileNotFoundException
import scala.util.Try

/**
 * The default Cubism singleton object that could be used to load Live 2D Cubism Avatar.
 */
object Cubism extends Cubism

/**
 * The main Cubism class.
 *
 * @param core  The core library of Cubism
 */
class Cubism(core: CubismCore) {


  def this() = this(new JnaCubismCore)

  /**
   * The current version of Cubism Core C Library.
   *
   * @return  The current version of underlying C Library.
   */
  lazy val coreLibraryVersion: CsmVersion = CsmVersion(core.cubismAPI.csmGetVersion())

  /**
   * Get the latest supported .moc file version.
   *
   * @return  The latest supported version.
   */
  lazy val latestSupportedMocVersion: MocVersion = MocVersion(core.cubismAPI.csmGetLatestMocVersion())

  /**
   * Load .moc files to Live2DModel instance.
   *
   * @param   mocFilename   The .moc file to load into Live2DModel instance.
   * @param   textureFiles  The texture files that should be used to render this model.
   *
   * @return                A Success[Live2DModel] if load successfully, otherwise a Failure
   *
   */
  def loadModel(mocFilename: String, textureFiles: List[String]): Try[Live2DModel] = Try {
    val fileReader = new MocInfoFileReader(mocFilename)(core.memoryAllocator)
    val mocInfo = fileReader.loadMocInfo
    val backend = new CubismModelBackend(mocInfo.get, textureFiles)(core)
    new Live2DModel(backend)
  }

  /**
   * Load Live2D Cubism avatar from a directory on the filesystem.
   *
   * @param     directory   The directory that contains the runtime avatar model.
   * @return                Success[Avatar] if loading successfully, otherwise a Failure.
   */
  def loadAvatar(directory: String): Try[Avatar] = Try {
    import java.io.File

    val directoryFile = new File(directory)

    if (!directoryFile.exists || !directoryFile.isDirectory) {
      throw new FileNotFoundException(directory)
    }

    try {
      new Avatar(directory)(this)
    } catch {
      case e: AssertionError => throw e
      case _: Exception => throw new AssertionError("Cannot load avatar settings")
    }
  }

}
