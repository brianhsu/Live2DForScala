package moe.brianhsu.live2d.enitiy.core.types

/**
 * The supported version of .moc file
 *
 * @param min The minimum version of the .moc file.
 * @param max The maximum dversion of the .moc file.
 */
sealed abstract class MocVersion(val min: String, val max: String)

/**
 * Unknown version
 */
case object Unknown extends MocVersion("unknown", "unknown")

/**
 * Version between 3.0.00 ~ 3.2.07
 */
case object MocVersion30 extends MocVersion("3.0.00", "3.2.07")

/**
 * Version between 3.3.00 ~ 3.3.03
 */
case object MocVersion33 extends MocVersion("3.3.00", "3.3.03")

/**
 * Version above 4.0.00
 */
case object MocVersion40 extends MocVersion("4.0.00", "4.1.05")

/**
 * Version above 4.2.00
 */
case object MocVersion42 extends MocVersion("4.2.00", "4.2.04")

/**
 * Version above 5.0.00
 */
case object MocVersion50 extends MocVersion("5.0.0", "*")

object MocVersion {
  def apply(version: Int): MocVersion = {
    version match {
      case 0 => Unknown
      case 1 => MocVersion30
      case 2 => MocVersion33
      case 3 => MocVersion40
      case 4 => MocVersion42
      case 5 => MocVersion50
    }
  }
}