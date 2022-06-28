ThisBuild / publishTo := Some("GitHub brianhsu Apache Maven Packages" at "https://maven.pkg.github.com/brianhsu/Live2DForScala")

ThisBuild / publishMavenStyle := true

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "brianhsu",
  System.getenv("GITHUB_TOKEN")
)

ThisBuild / assemblyMergeStrategy := {
  case x if x.endsWith("module-info.class") => {
    MergeStrategy.discard
  }
  case x => {
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
  }
}

