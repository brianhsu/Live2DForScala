name := "Live2D Cubism For Scala"

organization := "moe.brianhsu.live2d"

libraryDependencies ++= Seq(
    "net.java.dev.jna" % "jna" % "5.7.0",
    "org.scalatest" %% "scalatest" % "3.2.5" % "test"
)

fork := true
