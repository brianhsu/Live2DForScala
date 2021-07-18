package moe.brianhsu.live2d.framework.model

import moe.brianhsu.live2d.framework.model.settings.{Group, ModelSetting}
import org.json4s.{DefaultFormats, Formats, JValue}
import org.json4s.native.JsonMethods.parse

import java.io.File
import scala.io.Source
import scala.util.Using

class AvatarSettings(directory: String) {
  private implicit val formats: Formats = DefaultFormats
  private val mainFileHolder: Option[File] = findFile(".model3.json")

  assert(mainFileHolder.isDefined, s"Cannot find main settings of $directory")

  lazy val settingHolder: Option[ModelSetting] = parsedMainJson.map(_.camelizeKeys.extract[ModelSetting])
  lazy val mocFile: Option[String] = {
    settingHolder.map(_.fileReferences.moc)
      .map(file => s"$directory/$file")
  }

  lazy val textureFiles: List[String] = {
    settingHolder.map(_.fileReferences.textures)
      .getOrElse(Nil)
      .map(file => s"$directory/$file")
  }

  lazy val eyeBlinkParameterIds: List[String] = {
    for {
      setting <- settingHolder.toList
      group <- setting.groups if group.name == "EyeBlink" && group.target == "Parameter"
      parameterId <- group.ids
    } yield {
      parameterId
    }
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

  private def getRawTextFromFile(file: File): Option[String] = {
    Using(Source.fromFile(file)) { source => source.mkString }.toOption
  }

  private def findFile(extension: String): Option[File] = {
    val directoryFile = new File(directory)

    directoryFile
      .list((_: File, name: String) => name.endsWith(extension))
      .toList
      .headOption
      .map(filename => new File(s"$directory/$filename"))
      .filter(_.isFile)
  }

}
