name := "Live2D For Scala Core"

Compile / doc / scalacOptions ++= Seq("-private")

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html")

//javaOptions ++= Seq("-XX:+PrintGC")

fork := true

autoAPIMappings := true

val swtVersion = "3.120.0"

val swtFramework = {
  System.getProperty("os.name").toLowerCase match {
    case linux if linux.contains("linux") => "org.eclipse.platform" % "org.eclipse.swt.gtk.linux.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case win if win.contains("win") => "org.eclipse.platform" % "org.eclipse.swt.win32.win32.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case mac if mac.contains("mac")  => "org.eclipse.platform" % "org.eclipse.swt.cocoa.macosx.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case osName => throw new RuntimeException(s"Unknown operating system $osName")
  }
}

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
  "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-windows",
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-windows",
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion classifier "natives-windows",
  "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-macos",
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-macos",
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion classifier "natives-macos",
)

libraryDependencies += swtFramework
libraryDependencies ++= slfjFramework
libraryDependencies ++= lwjglFramework
libraryDependencies ++= lwjglNatives

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "net.java.dev.jna" % "jna" % "5.10.0",
    "org.json4s" %% "json4s-native" % "4.0.4",
    "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test
)

