package moe.brianhsu.live2d.adapter.gateway.avatar

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.adapter.gateway.model.{Live2DModelFileReader, MocInfoFileReader}
import moe.brianhsu.live2d.boundary.gateway.avatar.AvatarReader
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.avatar.Avatar

import scala.util.Try

/**
 * Avatar File Loader
 *
 * The modelDirectory should be a directory that contains a bunch of json and other files
 * that describe the behavior of the avatar.
 *
 * It should also includes a .moc3 file and texture files.
 *
 * You might obtain sample avatar from https://www.live2d.com/en/download/sample-data/
 *
 * @param modelDirectory          The directory contains the Cubism model and settings.
 * @param shouldCheckConsistent   Should we check consistent of the .moc3 file after reading it or not.
 * @param core                    The CubismCore to load the data from model file.
 */
class AvatarFileReader(modelDirectory: String, shouldCheckConsistent: Boolean = true)(implicit core: NativeCubismAPILoader) extends AvatarReader {
  override def loadAvatar(): Try[Avatar] = {

    for {
      settings <- new JsonSettingsReader(modelDirectory).loadSettings()
      mocInfoReader = new MocInfoFileReader(settings.mocFile, shouldCheckConsistent)
      live2dModel <- new Live2DModelFileReader(mocInfoReader, settings.textureFiles).loadModel()
    } yield {
      Avatar(settings, live2dModel)
    }

  }
}
