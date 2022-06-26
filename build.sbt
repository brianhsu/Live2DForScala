ThisBuild / organization := "com.example"
ThisBuild / version      := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions := Seq("-deprecation", "-Ywarn-unused", "-feature")

lazy val core = project

lazy val examples = project.dependsOn(core)
