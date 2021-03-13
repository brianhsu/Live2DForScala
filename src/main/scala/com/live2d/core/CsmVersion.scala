package com.live2d.core

case class CsmVersion(version: Int, major: Int, minor: Int, patch: Int)

object CsmVersion {
  def apply(version: Int) = {
    def major = (version & 0xFF000000) >> 24
    def minor = (version & 0x00FF0000) >> 16
    def patch = (version & 0x0000FFFF)
    new CsmVersion(version, major, minor, patch)
  }
}
