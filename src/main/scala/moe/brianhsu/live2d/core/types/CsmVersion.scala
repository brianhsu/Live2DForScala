package moe.brianhsu.live2d.core.types

/**
 * The version of Cubism Core C Library.
 *
 * @param major The major version number.
 * @param minor The minor version number.
 * @param patch The path number.
 */
case class CsmVersion(major: Int, minor: Int, patch: Int)

object CsmVersion {
  def apply(version: Int) = {
    def major = (version & 0xFF000000) >> 24
    def minor = (version & 0x00FF0000) >> 16
    def patch = (version & 0x0000FFFF)
    new CsmVersion(major, minor, patch)
  }
}
