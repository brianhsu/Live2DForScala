ThisBuild / organization := "moe.brianhsu.live2d"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions := Seq("-deprecation", "-Ywarn-unused", "-feature")
ThisBuild / publishArtifact := false
ThisBuild / Test / testOptions += Tests.Argument("-l", sys.env.get("EXCLUDE_TEST_TAG").getOrElse("noExclude"))

val swtVersion = "3.120.0"
val swtPackageName = {
  System.getProperty("os.name").toLowerCase match {
    case osName if osName.contains("linux") => "org.eclipse.swt.gtk.linux.x86_64"
    case osName if osName.contains("win") => "org.eclipse.swt.win32.win32.x86_64"
    case osName if osName.contains("mac")  => "org.eclipse.swt.cocoa.macosx.x86_64"
    case osName => throw new RuntimeException(s"Unknown operating system $osName")
  }
}

val swtFramework = "org.eclipse.platform" % swtPackageName % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
val swtWindows = "org.eclipse.platform" % "org.eclipse.swt.win32.win32.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
val swtLinux = "org.eclipse.platform" % "org.eclipse.swt.gtk.linux.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")

val testFramework = Seq(
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test
)

val sharedSettings = Seq(
  Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html"),
  Compile / doc / scalacOptions ++= Seq("-private"),
  autoAPIMappings := true,
  libraryDependencies ++= testFramework,
  libraryDependencies += "com.github.sarxos" % "webcam-capture" % "0.3.12",
  coverageExcludedPackages := """moe\.brianhsu\.live2d\.demo\..*"""
)

lazy val core = (project in file("modules/core"))
  .settings(
    name := "Core",
    publishArtifact := true,
    sharedSettings
  )

lazy val joglBinding = (project in file("modules/joglBinding"))
  .dependsOn(core)
  .settings(
    name := "JOGL Binding",
    publishArtifact := true,
    sharedSettings
  )

lazy val lwjglBinding = (project in file("modules/lwjglBinding"))
  .dependsOn(core)
  .settings(
    name := "LWJGL Binding",
    publishArtifact := true,
    sharedSettings
  )

lazy val swtBinding = (project in file("modules/swtBinding"))
  .dependsOn(core)
  .settings(
    name := "SWT Binding",
    fork := true,
    publishArtifact := true,
    sharedSettings,
    libraryDependencies += swtFramework % "test,provided" 
  )

lazy val exampleBase = (project in file("modules/examples/base"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding)
  .settings(
    name := "Examples Base",
    publishArtifact := false,
    sharedSettings
  )

lazy val exampleSwing = (project in file("modules/examples/swing"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding, exampleBase)
  .settings(
    name := "Example Swing+JOGL",
    fork := true,
    publishArtifact := false,
    assembly / assemblyJarName := s"Live2DForScala-Swing-${version.value}.jar",
    sharedSettings
  )

lazy val exampleSWT = (project in file("modules/examples/swt"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding, exampleBase)
  .settings(
    name := "Example SWT+JWJGL",
    fork := true,
    publishArtifact := false,
    sharedSettings,
    libraryDependencies += swtFramework % "provided"
  )

lazy val exampleSWTLinux = (project in file("modules/examples/swt-linux-bundle"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding, exampleSWT)
  .settings(
    name := "Example SWT+JWJGL Linux",
    fork := true,
    publishArtifact := false,
    Compile / mainClass := Some("moe.brianhsu.live2d.demo.swt.SWTWithLWJGLMain"),
    sharedSettings,
    assembly / assemblyJarName := s"Live2DForScala-SWT-Linux-${version.value}.jar",
    libraryDependencies += swtLinux
  )

lazy val exampleSWTWin = (project in file("modules/examples/swt-windows-bundle"))
  .dependsOn(core, joglBinding, lwjglBinding, swtBinding, exampleSWT)
  .settings(
    name := "Example SWT+JWJGL Windows",
    fork := true,
    publishArtifact := false,
    Compile / mainClass := Some("moe.brianhsu.live2d.demo.swt.SWTWithLWJGLMain"),
    sharedSettings,
    assembly / assemblyJarName := s"Live2DForScala-Windows-Linux-${version.value}.jar",
    libraryDependencies += swtWindows
  )
