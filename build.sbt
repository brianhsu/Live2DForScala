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

lazy val core = project.settings(sharedSettings)

lazy val joglBinding = project.dependsOn(core).settings(sharedSettings)

lazy val lwjglBinding = project.dependsOn(core).settings(sharedSettings)

lazy val swtBinding = project.dependsOn(core).settings(sharedSettings)

lazy val examples = project.dependsOn(core, joglBinding, lwjglBinding, swtBinding)
