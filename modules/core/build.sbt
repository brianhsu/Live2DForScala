fork := true

val slf4jVersion = "2.0.7"
val slfjFramework = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-simple" % slf4jVersion
)

libraryDependencies ++= slfjFramework

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "net.java.dev.jna" % "jna" % "5.13.0",
  "org.json4s" %% "json4s-native" % "4.0.6"
)

