package moe.brianhsu.live2d.framework.model

import moe.brianhsu.live2d.framework.Cubism

import java.io.File
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scala.io.Source
import scala.util.{Try, Using}

/**
 * This class represent a complete Live 2D Cubism Avatar runtime model.
 *
 * The runtime model should be a directory that contains a bunch of json and other files
 * that describe the behavior of the avatar.
 *
 * It should also includes a .moc3 file and texture files.
 *
 * You might obtain sample avatar from https://www.live2d.com/en/download/sample-data/
 *
 * @param   directory   The directory in the filesystem that contains the settings for the avatar
 */
class Avatar(val directory: String)(cubism: Cubism) {
  private val MainSetting = ".model3.json"
  private val mainFileHolder: Option[File] = findFile(MainSetting)
  private val mocFile: Option[String] = getMocFileFromMainJson()

  assert(mainFileHolder.isDefined, s"Cannot find main settings of $directory")
  assert(mocFile.isDefined, s"Cannot find moc file inside the $directory/${mainFileHolder.get}")

  val model: Try[Live2DModel] = {
    cubism
      .loadModel(mocFile.get, getTextureFiles)
      .map(_.validAllDataFromNativeLibrary)
  }

  private lazy val parsedMainJson: Option[JValue] = {
    for {
      file <- mainFileHolder
      rawText <- getRawTextFromFile(file)
      parsedJson = parse(rawText)
    } yield {
      parsedJson
    }
  }

  private def getTextureFiles: List[String] = {
    parsedMainJson
      .map(parseTextureList)
      .getOrElse(Nil)
  }

  private def parseTextureList(mainJson: JValue): List[String] = {
    mainJson \ "FileReferences" \ "Textures" match {
      case JArray(textureList) => textureList.map(filename => s"$directory/${filename.values.toString}")
      case _ => Nil
    }
  }

  private def getMocFileFromMainJson(): Option[String] = {
    for {
      parsedJson <- parsedMainJson
      mocFile <- (parsedJson \ "FileReferences" \ "Moc")
                    .filter(_ != JNothing)
                    .headOption.map(filename => s"$directory/${filename.values.toString}")
    } yield {
      mocFile
    }
  }

  private def getRawTextFromFile(file: File): Option[String] = {
    Using(Source.fromFile(file)) { source => source.mkString }.toOption
  }

  private def findFile(extension: String): Option[File] = {
    val directoryFile = new File(directory)

    directoryFile
      .list((dir: File, name: String) => name.endsWith(extension))
      .toList
      .headOption
      .map(filename => new File(s"$directory/$filename"))
      .filter(_.isFile)
  }
}
