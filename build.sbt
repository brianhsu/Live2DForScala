name := "Live2D Cubism For Scala"

organization := "moe.brianhsu.live2d"

scalaVersion := "2.13.5"

scalacOptions := Seq("-deprecation")

val lwjglVersion = "3.2.3"

libraryDependencies ++= Seq(
    "net.java.dev.jna" % "jna" % "5.7.0",
    "org.json4s" %% "json4s-native" % "3.6.11",
//    "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2",
//    "org.jogamp.gluegen" % "gluegen-rt-main" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all-main" % "2.3.2",
    "org.lwjgl" % "lwjgl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
    "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
    "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-macos",
    "org.lwjgl" % "lwjgl-assimp" % lwjglVersion classifier "natives-macos",
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier "natives-macos",
    "org.lwjgl" % "lwjgl-openal" % lwjglVersion classifier "natives-macos",
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-macos",
    "org.scalatest" %% "scalatest" % "3.2.5" % Test,
    "org.scalamock" %% "scalamock" % "5.1.0" % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % Test
)

fork := true

//run / javaOptions += "-XstartOnFirstThread"
