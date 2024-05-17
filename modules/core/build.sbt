fork := true

val slf4jVersion = "1.7.36"
val slfjFramework = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-simple" % slf4jVersion
)

libraryDependencies ++= slfjFramework

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.13.11",
  "net.java.dev.jna" % "jna" % "5.10.0",
  "org.json4s" %% "json4s-native" % "4.0.4",
  "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test
)

