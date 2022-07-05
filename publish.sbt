ThisBuild / publishTo := Some(Resolver.file("file", new File("mavenRepository")))

ThisBuild / publishMavenStyle := true

ThisBuild / assemblyMergeStrategy := {
  case x if x.endsWith("module-info.class") => {
    MergeStrategy.discard
  }
  case x => {
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
  }
}

