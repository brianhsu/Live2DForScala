package moe.brianhsu.live2d.adapter.gateway.avatar.settings.json

import moe.brianhsu.live2d.adapter.RichPath.convertFromPath
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.{ExpressionSetting, MotionSetting, PhysicsSetting, PoseSetting}
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.model.{Group, ModelSetting}
import moe.brianhsu.live2d.boundary.gateway.avatar.SettingsReader
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

import java.io.FileNotFoundException
import java.nio.file.{Files, Path, Paths}
import scala.jdk.StreamConverters._
import scala.util.{Failure, Success, Try}

/**
 * Live 2D Cubism avatar JSON setting reader.
 *
 * Read settings from a directory contains the `model3.json`, and other related file.
 *
 * @param directory Directory contains the avatar settings.
 */
class JsonSettingsReader(directory: String) extends SettingsReader {
  private implicit val formats: Formats = DefaultFormats

  override def loadSettings(): Try[Settings] = {
    for {
      settings <- loadMainModelSettings()
      mocFile <- parseMocFile(settings)
      physics <- parsePhysics(settings)
      textureFiles <- parseTextureFiles(settings)
      pose <- parsePose(settings)
      eyeBlinkParameterIds <- parseEyeBlinkParameterIds(settings)
      lipSyncParameterIds <- parseLipSyncParameterIds(settings)
      expressions <- parseExpressions(settings)
      motionGroups <- parseMotionGroups(settings)
    } yield {

      Settings(
        mocFile, textureFiles, physics, pose,
        eyeBlinkParameterIds, lipSyncParameterIds,
        expressions, motionGroups,
        settings.hitAreas
      )
    }
  }

  /**
   * Load and parse the main .model3.json file.
   *
   * @return [[scala.util.Success]] if model loaded correctly, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def loadMainModelSettings(): Try[ModelSetting] = {

    for {
      directory <- findModelDirectory()
      jsonContent <- loadMainJsonFile(directory)
      parsedJson <- Try(parse(jsonContent))
    } yield {
      parsedJson.camelizeKeys.extract[ModelSetting]
    }
  }

  /**
   * Validate the path avatar directory exist.
   *
   * @return [[scala.util.Success]] if directory exist, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def findModelDirectory(): Try[Path] = {
    val directoryPath = Paths.get(directory)

    if (Files.notExists(directoryPath)) {
      Failure(new FileNotFoundException(s"The folder $directory does not exist."))
    } else {
      Success(directoryPath)
    }
  }

  /**
   * Load main .model3.json file to a String.
   *
   * @param directoryPath The directory path contains the .model3.json file.
   *
   * @return [[scala.util.Success]] containing the JSON file content, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def loadMainJsonFile(directoryPath: Path): Try[String] = {

    def isMainModel(path: Path): Boolean = path.getFileName.toString.endsWith(".model3.json")

    Files.list(directoryPath)
      .toScala(LazyList)
      .find(p => isMainModel(p) && p.isReadableFile)
      .toRight(new FileNotFoundException(s"Main model json file not found at $directory"))
      .toTry
      .flatMap(p => p.readToString())
  }

  /**
   * Parse moc file location
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing absolute path of .moc file, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parseMocFile(modelSetting: ModelSetting): Try[String] = {
    val filePath = s"$directory/${modelSetting.fileReferences.moc}"

    Option(Paths.get(filePath))
      .find(p => p.isReadableFile)
      .map(_.toAbsolutePath.toString)
      .toRight(new FileNotFoundException(s"Main model json file not found at $directory"))
      .toTry
  }

  /**
   * Parse texture files location.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing list of absolute path of texture files, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parseTextureFiles(modelSetting: ModelSetting): Try[List[String]] = Try {
    modelSetting.fileReferences
      .textures
      .map(file => Paths.get(s"$directory/$file"))
      .filter(p => p.isReadableFile)
      .map(_.toAbsolutePath.toString)
  }

  /**
   * Parse eye blink parameters.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing list of parameters related to eye blink, otherwise [[scala.util.Failure]] denoted the exception.
   */
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

  /**
   * Parse lip sync parameters.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing list of parameters related to lip sync, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parseLipSyncParameterIds(modelSetting: ModelSetting): Try[List[String]] = Try {

    def isLipSyncParameter(group: Group): Boolean = {
      group.name == "LipSync" && group.target == "Parameter"
    }

    for {
      group <- modelSetting.groups if isLipSyncParameter(group)
      parameterId <- group.ids
    } yield {
      parameterId
    }
  }

  /**
   * Parse Pose settings.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing optional pose settings, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parsePose(modelSetting: ModelSetting): Try[Option[PoseSetting]] = Try {
    for {
      pose <- modelSetting.fileReferences.pose
      jsonFile <- Option(Paths.get(s"$directory/$pose")) if jsonFile.isReadableFile
      jsonFileContent <- jsonFile.readToString().toOption
    } yield {
      parse(jsonFileContent).camelizeKeys.extract[PoseSetting]
    }
  }

  /**
   * Parse Physics settings.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing optional physics settings, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parsePhysics(modelSetting: ModelSetting): Try[Option[PhysicsSetting]] = Try {
    for {
      physics <- modelSetting.fileReferences.physics
      jsonFile <- Option(Paths.get(s"$directory/$physics")) if jsonFile.isReadableFile
      jsonFileContent <- jsonFile.readToString().toOption
    } yield {
      parse(jsonFileContent).camelizeKeys.extract[PhysicsSetting]
    }
  }

  /**
   * Parse Expression settings.
   *
   * The value inside returned [[scala.util.Success]] object, will be a map that key is expression name,
   * value is the setting of that expression.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing expression settings, otherwise [[scala.util.Failure]] denoted the exception.
   */
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

  /**
   * Parse Motion group settings.
   *
   * The value inside returned [[scala.util.Success]] object, will be a map that key is name of motion group,
   * value is the list of settings of motions in that group.
   *
   * @param modelSetting  The model setting object.
   *
   * @return [[scala.util.Success]] containing map of motion settings, otherwise [[scala.util.Failure]] denoted the exception.
   */
  private def parseMotionGroups(modelSetting: ModelSetting): Try[Map[String, List[MotionSetting]]] = Try {
    for {
      (groupName, motionList) <- modelSetting.fileReferences.motions
    } yield {
      val motionJsonList = for {
        motionFile <- motionList
        paredJson <- motionFile.loadMotion(directory).toOption
      } yield {
        MotionSetting(
          paredJson.version,
          motionFile.fadeInTime,
          motionFile.fadeOutTime,
          motionFile.sound.map(directory + "/" + _),
          paredJson.meta,
          paredJson.userData,
          paredJson.curves
        )
      }

      groupName -> motionJsonList
    }
  }

}
