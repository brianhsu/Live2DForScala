name := "Live2D For Scala SWT Binding"

val swtVersion = "3.120.0"

val swtFramework = {
  System.getProperty("os.name").toLowerCase match {
    case linux if linux.contains("linux") => "org.eclipse.platform" % "org.eclipse.swt.gtk.linux.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case win if win.contains("win") => "org.eclipse.platform" % "org.eclipse.swt.win32.win32.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case mac if mac.contains("mac")  => "org.eclipse.platform" % "org.eclipse.swt.cocoa.macosx.x86_64" % swtVersion exclude("org.eclipse.platform", "org.eclipse.swt")
    case osName => throw new RuntimeException(s"Unknown operating system $osName")
  }
}

libraryDependencies += swtFramework % "provided"
