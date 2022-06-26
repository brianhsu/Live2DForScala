ThisBuild / organization := "com.example"
ThisBuild / version      := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions := Seq("-deprecation", "-Ywarn-unused", "-feature")

val swtVersion = "3.120.0"
val swtPackageName = {
  System.getProperty("os.name").toLowerCase match {
    case linux if linux.contains("linux") => "org.eclipse.swt.gtk.linux.x86_64"
    case win if win.contains("win") => "org.eclipse.swt.win32.win32.x86_64"
    case mac if mac.contains("mac")  => "org.eclipse.swt.cocoa.macosx.x86_64"
    case osName => throw new RuntimeException(s"Unknown operating system $osName")
  }
}

val swtFramework = "org.eclipse.platform" % swtPackageName % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")

val testFramework = Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test
)

val sharedSettings = Seq(
  Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html"),
  Compile / doc / scalacOptions ++= Seq("-private"),
  autoAPIMappings := true,
  libraryDependencies ++= testFramework
)

lazy val core = (project in file("modules/core"))
  .settings(
    name := "Live2D For Scala Core",
    sharedSettings
  )

lazy val joglBinding = (project in file("modules/joglBinding"))
  .dependsOn(core)
  .settings(
    name := "Live2D For Scala Java OpenGL Binding",
    sharedSettings
  )

lazy val lwjglBinding = (project in file("modules/lwjglBinding"))
  .dependsOn(core)
  .settings(
    name := "Live2D For Scala LWJGL Binding",
    sharedSettings
  )

lazy val swtBinding = (project in file("modules/swtBinding"))
  .dependsOn(core)
  .settings(
    name := "Live2D For Scala SWT Binding",
    fork := true,
    sharedSettings,
    libraryDependencies += swtFramework % "test,provided" 
  )

lazy val examples = (project in file("modules/examples"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding)
  .settings(
    name := "Live2D For Scala Examples",
    fork := true,
    sharedSettings,
    libraryDependencies += swtFramework
  )
