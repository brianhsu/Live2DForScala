Live2D For Scala (JVM / Linux / Windows / MacOS)
=================================================
![Maven metadata URL](https://img.shields.io/maven-metadata/v?label=maven&logo=github&metadataUrl=https%3A%2F%2Fmaven.pkg.github.com%2Fbrianhsu%2FLive2DForScala%2Fmoe%2Fbrianhsu%2Flive2d%2Fcore_2.13%2Fmaven-metadata.xml&style=for-the-badge) ![Code Coverage](../doc/images/coverage.svg) ![Version](https://img.shields.io/github/v/tag/brianhsu/Live2DForScala?style=for-the-badge)

Disclaimer
-----------

1. This project is **NOT** affiliate nor endorsed Live2D Inc, explicitly or implicitly.
2. This is a project that helps me learning how to interact with native libraries in Scala/JVM, and how to apply Clean Architecture in a real life scenario. Because such, although the core library of this project is relative fully featured, it should **NOT** be considered as a replacement for the official Live 2D Cubism SDK.
3. As above, this project may be abandoned if I don't interested in this topic any more. Use it at your own risk.

Scala/Java version requirement
----------------------------

1. It required JDK 11 to build.
2. It required Scala 2.13.x to build.

Introduction
-------------

Since the project follows the [Clean Architecture][0] design philosophy, it's seperated into multiple modules. Those modules are SBT sub-projects under the modules directory.

The main concept of Clean Architecture is that the core business logic module should not depends on concrete dependencies of outside world. For example, the renderer logic of Live 2D avatar model in the core module should not depends on Java OpenGL or LWJGL implementation. Library users should able to inject Java OpenGL binding or LWJGL binding in runtime.

The following table shows the description of each module.

| Module Source Code | Maven GroupID        | Maven ArtifactID   | Description                                                                  | 
| ------------------ | -------------------- | ----------------   | ---------------------------------------------------------------------------- |
| core/              | moe.brianhsu.live2d  | core_2.13   | The core library to control Live2D model, you could consider it like an SDK. |
| joglBinding/       | moe.brianhsu.live2d  | jogl_2.13  | The [JavaOpenGL][1] binding for render your avatar.                  |
| lwjglBinding/      | moe.brianhsu.live2d  | lwjgl_2.13 | The [LWJGL][2] (Light Weight Java Game Library) binding for render your avatar.                  |
| swtBinding/        | moe.brianhsu.live2d  | swt_2.13 | The [SWT][3] binding for render your avatar in [SWT][3] with LWJGL.         |

Core Library
-------------

This is the core library to control and render the Live2D model. It's provides you all the core functionality to handle a Live2D avatar model resource (a directory contains the `.moc3` file).

You could add the following code into your project's `build.sbt` to get the dependency from GitHub Packages maven repository.

### Add Dependency

```scala
resolvers +=
  "Live2DForScala" at "https://maven.pkg.github.com/brianhsu/Live2DForScala/"

libraryDependencies += "moe.brianhsu.live2d" %% "core" % "X.Y.Z"
```

### Load / Control Live2D Model

The following code shows you how to read a Live2D avatar model and enable some built-in effects.

```scala
package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.adapter.gateway.avatar.AvatarFileReader
import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPhysicsReader, AvatarPoseReader}
import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink}
import moe.brianhsu.live2d.enitiy.updater.SystemNanoTimeBasedFrameInfo
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy

import scala.util.Try

object Main {
  // Load the Cubism Native library
  private implicit val cubismCore: JnaNativeCubismAPILoader = new JnaNativeCubismAPILoader()

  def main(args: Array[String]): Unit = {

    // Load a Live2D model from `modelResource` library which contains a .moc3 file
    val avatarHolder: Try[Avatar] = new AvatarFileReader("modelResource").loadAvatar()

    // Create update strategy to tell Avatar how to update the Live2D model from time to time,
    // and set it to the Avatar object if the it is loaded successfully.
    for {
      avatar <- avatarHolder
    } {
      // Load pose settings from avatar
      val poseHolder = new AvatarPoseReader(avatar.avatarSettings).loadPose

      // Load physics settings from avatar
      val physicsHolder = new AvatarPhysicsReader(avatar.avatarSettings).loadPhysics

      // Create a update strategy that enables the following effect:
      // 1. Auto breathing
      // 2. Auto eye blinks
      // 3. Physics effects
      // 4. Pose settings
      val strategy = new BasicUpdateStrategy(avatar.avatarSettings, avatar.model)
      strategy.effects = List(
        Some(new Breath()),
        Some(new EyeBlink(avatar.avatarSettings)),
        physicsHolder,
        poseHolder
      ).flatten

      // Set the strategy to the Avatar object
      avatar.updateStrategyHolder = Some(strategy)
    }

    // Create a FrameTime info object that calculate how much time has past between each draw
    val frameTimeInfo = new SystemNanoTimeBasedFrameInfo

    // Update the avatar at 60 fps
    while (true) {
      frameTimeInfo.updateFrameTime(System.currentTimeMillis())
      avatarHolder.foreach(_.update(frameTimeInfo))
      Thread.sleep(16)
    }

  }
}
```

### Render the Live2D Model

Without rendering, the Live2D model itself it pretty meaningless. The core library also provides a `AvatarRenderer` class to help you render the model by using OpenGL.

Please be aware that since this project follows the Clean Architecture design, so the AvatarRenderer does not depends on actual OpenGL binding. User should include the OpenGL binding of thier choice into their project's dependency, and initialize it in the code. Also, the client should be responsible to create and setup OpenGL context.

```scala
implicit val openGLBinding: OpenGLBinding = ...
val avatar: Avatar = ....

val renderer = AvatarRenderer.apply(avatar.model)
val projectionMatrix = new ProjectionMatrix

renderer.draw(projectionMatrix)
```

For real-life example, see [modules/examples/](modules/examples/).

[0]: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[1]: https://jogamp.org/jogl/www/
[2]: https://www.lwjgl.org/
[3]: https://www.eclipse.org/swt/
