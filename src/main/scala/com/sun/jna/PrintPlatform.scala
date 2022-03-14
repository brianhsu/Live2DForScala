package com.sun.jna

object PrintPlatform {
  def main(args: Array[String]): Unit = {
    println(Platform.getNativeLibraryResourcePrefix())
  }
}
