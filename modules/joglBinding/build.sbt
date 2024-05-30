val joglVersion = "2.5.0"

resolvers += "jogamp" at "https://jogamp.org/deployment/maven"

libraryDependencies ++= Seq(
  "org.jogamp.gluegen" % "gluegen-rt-main" % joglVersion,
  "org.jogamp.jogl" % "jogl-all-main" % joglVersion
)

