name := "Live2D Cubism For Scala"

organization := "moe.brianhsu.live2d"

scalaVersion := "2.13.5"

//scalacOptions := Seq("-deprecation", "-Ywarn-unused")

scalacOptions := Seq("-deprecation")

Compile / doc / scalacOptions ++= Seq("-private")

autoAPIMappings := true

libraryDependencies ++= Seq(
    "net.java.dev.jna" % "jna" % "5.10.0",
    "org.json4s" %% "json4s-native" % "4.0.4",
//    "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2",
//    "org.jogamp.gluegen" % "gluegen-rt-main" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all-main" % "2.3.2",
    "org.scalatest" %% "scalatest" % "3.2.11" % Test,
    "org.scalamock" %% "scalamock" % "5.2.0" % Test,
    "org.slf4j" % "slf4j-api" % "1.7.36",
    "org.slf4j" % "slf4j-simple" % "1.7.36",
    "org.lwjgl" % "lwjgl" % "3.3.1",
    "org.lwjgl" % "lwjgl" % "3.3.1" classifier "natives-linux",
    "org.lwjgl" % "lwjgl-opengl" % "3.3.1",                          
    "org.lwjgl" % "lwjgl-opengles" % "3.3.1",                          
    "org.lwjgl" % "lwjgl-opengl" % "3.3.1" classifier "natives-linux",
    "org.lwjgl" % "lwjgl-opengles" % "3.3.1" classifier "natives-linux"

)

fork := true

