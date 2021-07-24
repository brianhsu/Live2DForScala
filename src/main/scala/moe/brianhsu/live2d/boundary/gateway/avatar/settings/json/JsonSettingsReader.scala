package moe.brianhsu.live2d.boundary.gateway.avatar.settings.json

import moe.brianhsu.live2d.RichPath.convertFromPath
import moe.brianhsu.live2d.boundary.gateway.avatar.settings.SettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.{ExpressionSetting, MotionSetting, PoseSetting}
import moe.brianhsu.live2d.boundary.gateway.avatar.settings.json.model.{Group, ModelSetting}
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

import java.io.FileNotFoundException
import java.nio.file.{Files, Path, Paths}
import scala.jdk.StreamConverters._
import scala.util.{Failure, Success, Try}

class JsonSettingsReader(directory: String) extends SettingsReader {
  private implicit val formats: Formats = DefaultFormats

  override def loadSettings(): Try[Settings] = {
    for {
      settings <- loadMainModelSettings()
      mocFile <- parseMocFile(settings)
      textureFiles <- parseTextureFiles(settings)
      pose <- parsePose(settings)
      eyeBlinkParameterIds <- parseEyeBlinkParameterIds(settings)
      expressions <- parseExpressions(settings)
      motionGroups <- parseMotionGroups(settings)
    } yield {

      Settings(
        mocFile, textureFiles, pose,
        eyeBlinkParameterIds, expressions,
        motionGroups,
        settings.hitAreas
      )
    }
  }

  private def loadMainModelSettings(): Try[ModelSetting] = {

    for {
      directory <- findModelDirectory
      jsonContent <- findMainModelJson(directory)
      parsedJson <- Try(parse(jsonContent))
    } yield {
      parsedJson.camelizeKeys.extract[ModelSetting]
    }
  }

  private def findModelDirectory: Try[Path] = {
    val directoryPath = Paths.get(directory)

    if (Files.notExists(directoryPath)) {
      Failure(new FileNotFoundException(s"The folder $directory does not exist."))
    } else {
      Success(directoryPath)
    }
  }

  private def findMainModelJson(directoryPath: Path): Try[String] = {

    def isMainModel(path: Path): Boolean = path.getFileName.toString.endsWith(".model3.json")

    Files.list(directoryPath)
      .toScala(LazyList)
      .find(p => isMainModel(p) && p.isReadableFile)
      .toRight(new FileNotFoundException(s"Main model json file not found at $directory"))
      .toTry
      .flatMap(p => p.readToString())
  }


  private def parseMocFile(modelSetting: ModelSetting): Try[String] = {
    val filePath = s"$directory/${modelSetting.fileReferences.moc}"

    Option(Paths.get(filePath))
      .find(p => p.isReadableFile)
      .map(_.toAbsolutePath.toString)
      .toRight(new FileNotFoundException(s"Main model json file not found at $directory"))
      .toTry
  }

  private def parseTextureFiles(modelSetting: ModelSetting): Try[List[String]] = Try {
    modelSetting.fileReferences
      .textures
      .map(file => Paths.get(s"$directory/$file"))
      .filter(p => p.isReadableFile)
      .map(_.toAbsolutePath.toString)
  }

  private def parseEyeBlinkParameterIds(modelSetting: ModelSetting): Try[List[String]] = Try {

    def isEyeBlinkParameter(group: Group): Boolean = {
      group.name == "EyeBlink" && group.target == "Parameter"
    }

    for {
      group <- modelSetting.groups if isEyeBlinkParameter(group)
      parameterId <- group.ids
    } yield {
      parameterId
    }
  }

  private def parsePose(modelSetting: ModelSetting): Try[Option[PoseSetting]] = Try {
    for {
      pose <- modelSetting.fileReferences.pose
      jsonFile <- Option(Paths.get(s"$directory/$pose")) if jsonFile.isReadableFile
      jsonFileContent <- jsonFile.readToString().toOption
    } yield {
      parse(jsonFileContent).camelizeKeys.extract[PoseSetting]
    }
  }

  private def parseExpressions(modelSetting: ModelSetting): Try[Map[String, ExpressionSetting]] = Try {
    val nameToExpressionList = for {
      expressionFileInfo <- modelSetting.fileReferences.expressions
      expressionJsonFilePath = Paths.get(s"$directory/${expressionFileInfo.file}")
      jsonFile <- Option(expressionJsonFilePath) if jsonFile.isReadableFile
      jsonFileContent <- jsonFile.readToString().toOption
      parsedJson <- Try(parse(jsonFileContent)).toOption
    } yield {
      expressionFileInfo.name -> parsedJson.camelizeKeys.extract[ExpressionSetting]
    }
    nameToExpressionList.toMap
  }

  private def parseMotionGroups(modelSetting: ModelSetting): Try[Map[String, List[MotionSetting]]] = Try {
    for {
      (groupName, motionList) <- modelSetting.fileReferences.motions
    } yield {
      val motionJsonList = for {
        motionFile <- motionList
        paredJson <- motionFile.loadMotion(directory)
      } yield {
        MotionSetting(
          paredJson.version,
          motionFile.fadeInTime,
          motionFile.fadeOutTime,
          paredJson.meta,
          paredJson.userData,
          paredJson.curves
        )
      }

      groupName -> motionJsonList
    }
  }

}
