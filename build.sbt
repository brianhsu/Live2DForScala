name := "Live2D Cubism For Scala"

organization := "moe.brianhsu.live2d"

scalaVersion := "2.13.8"

scalacOptions := Seq("-deprecation", "-Ywarn-unused", "-feature")

Compile / doc / scalacOptions ++= Seq("-private")

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html")

//javaOptions ++= Seq("-XX:+PrintGC")

autoAPIMappings := true

fork := true

val swtVersion = "4.3"
val swtFramework = Seq(
  "org.eclipse.swt" % "org.eclipse.swt.gtk.linux.x86_64" % swtVersion,
  "org.eclipse.swt" % "org.eclipse.swt.cocoa.macosx.x86_64" % swtVersion,
  "org.eclipse.swt" % "org.eclipse.swt.win32.win32.x86_64" % swtVersion,
)

val slf4jVersion = "1.7.36"
val slfjFramework = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-simple" % slf4jVersion
)

val lwjglVersion = "3.3.1"
val lwjglFramework = Seq(
  "org.lwjgl" % "lwjgl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,                          
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion,                          
)

val lwjglNatives = Seq(
  "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-linux",
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-linux",
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion classifier "natives-linux",
)

val testFramework = Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
)

libraryDependencies ++= swtFramework
libraryDependencies ++= slfjFramework
libraryDependencies ++= lwjglFramework
libraryDependencies ++= lwjglNatives
libraryDependencies ++= testFramework

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "net.java.dev.jna" % "jna" % "5.10.0",
    "org.json4s" %% "json4s-native" % "4.0.4",
    "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test
//    "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2",
//    "org.jogamp.gluegen" % "gluegen-rt-main" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all" % "2.3.2",
//    "org.jogamp.jogl" % "jogl-all-main" % "2.3.2",
)

