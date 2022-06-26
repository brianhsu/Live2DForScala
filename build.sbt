ThisBuild / organization := "com.example"
ThisBuild / version      := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions := Seq("-deprecation", "-Ywarn-unused", "-feature")

val testFramework = Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
)

val sharedSettings = Seq(
  libraryDependencies ++= testFramework
)

lazy val core = (project in file("modules/core")).settings(sharedSettings)

lazy val joglBinding = (project in file("modules/joglBinding")).dependsOn(core).settings(sharedSettings)

lazy val lwjglBinding = (project in file("modules/lwjglBinding")).dependsOn(core).settings(sharedSettings)

lazy val swtBinding = (project in file("modules/swtBinding")).dependsOn(core).settings(sharedSettings)

lazy val examples = (project in file("modules/examples")).dependsOn(core, joglBinding, lwjglBinding, swtBinding)
