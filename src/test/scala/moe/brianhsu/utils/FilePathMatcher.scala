package moe.brianhsu.utils

import org.scalatest.matchers.{MatchResult, Matcher}

import java.io.File

trait FilePathMatcher {

  class FileEndsWithPathMatcher(expectedPath: String) extends Matcher[String] {

    def apply(left: String) = {
      MatchResult(
        left.replace(File.separator, "/").endsWith(expectedPath),
        s"""File $left did not end with extension "$expectedPath"""",
        s"""File $left ended with extension "$expectedPath""""
      )
    }
  }

  def endWithPath(expectedExtension: String) = new FileEndsWithPathMatcher(expectedExtension)
}
