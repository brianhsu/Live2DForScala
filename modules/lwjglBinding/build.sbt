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

libraryDependencies ++= lwjglFramework
libraryDependencies ++= lwjglNatives
