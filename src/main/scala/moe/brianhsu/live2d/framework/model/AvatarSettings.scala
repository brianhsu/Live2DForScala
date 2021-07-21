package moe.brianhsu.live2d.framework.model

import moe.brianhsu.live2d.framework.model.AvatarSettings.parseJson
import moe.brianhsu.live2d.framework.model.settings.{Expression, Group, ModelSetting, Motion, MotionInfo, PoseSettings}
import org.json4s.{DefaultFormats, Formats, JValue}
import org.json4s.native.JsonMethods.parse

import java.io.File
import scala.io.Source
import scala.util.Using

object AvatarSettings {
  def parseJson(file: File): Option[JValue] = {
    for {
      rawText <- getRawTextFromFile(file)
      parsedJson = parse(rawText)
    } yield {
      parsedJson
    }
  }

  private def getRawTextFromFile(file: File): Option[String] = {
    Using(Source.fromFile(file)) { source => source.mkString }.toOption
  }

  def main(args: Array[String]): Unit = {
    val avatarSettings = new AvatarSettings("/Users/bhsu/Downloads/CubismSdkForNative-4-r.3/Samples/Resources/Mark")
    println(avatarSettings.pose)
  }
}

class AvatarSettings(directory: String) {
  private implicit val formats: Formats = DefaultFormats
  private lazy val mainFileHolder: Option[File] = findFile(".model3.json")
  private lazy val settingHolder: Option[ModelSetting] = parsedMainJson.map(_.camelizeKeys.extract[ModelSetting])

  assert(mainFileHolder.isDefined, s"Cannot find main settings of $directory")

  lazy val mocFile: Option[String] = {
    settingHolder.map(_.fileReferences.moc)
      .map(file => s"$directory/$file")
  }

  lazy val textureFiles: List[String] = {
    settingHolder.map(_.fileReferences.textures)
      .getOrElse(Nil)
      .map(file => s"$directory/$file")
  }

  lazy val pose: Option[PoseSettings] = {
    println("QQQ")
    for {
      setting <- settingHolder
    } yield {
      println(setting)
      /*
            pose <- setting.fileReferences.pose
      parsedJson <- AvatarSettings.parseJson(new File(s"$directory/$pose"))

      parsedJson.camelizeKeys.extract[PoseSettings]

       */
    }
    None
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

  lazy val expressions: Map[String, Expression] = {
    val nameToExpressionList = for {
      setting <- settingHolder.toList
      expressionFileInfo <- setting.fileReferences.expressions
      expressionJsonFilePath = s"$directory/${expressionFileInfo.file}"
      jsonFile <- List(new File(expressionJsonFilePath)).filter(_.exists)
      parsedJson <- parseJson(jsonFile)
    } yield {
      expressionFileInfo.name -> parsedJson.camelizeKeys.extract[Expression]
    }
    nameToExpressionList.toMap
  }

  lazy val motions: Map[String, List[MotionInfo]] = {
    val nameToExpressionList = for {
      setting <- settingHolder.toList
      (groupName, motionList) <- setting.fileReferences.motions
    } yield {
      val motionJsonList = for {
        motionFile <- motionList
        paredJson <- motionFile.loadMotion(directory)
      } yield MotionInfo(motionFile, paredJson)

      groupName -> motionJsonList
    }

    nameToExpressionList.toMap
  }

  private lazy val parsedMainJson: Option[JValue] = for {
    file <- mainFileHolder
    parsedJson <- parseJson(file)
  } yield parsedJson

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
