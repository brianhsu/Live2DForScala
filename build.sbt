name := "Live2D Cubism For Scala"

organization := "moe.brianhsu.live2d"

libraryDependencies ++= Seq(
    "net.java.dev.jna" % "jna" % "5.7.0",
    "org.scalatest" %% "scalatest" % "3.2.5" % Test,
    "org.scalamock" %% "scalamock" % "5.1.0" % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % Test
)

fork := true
