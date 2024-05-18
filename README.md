Live2D For Scala (JVM / Linux / Windows / MacOS Intel)
================================================
[![Maven Version](doc/images/version.svg)](https://github.com/brianhsu/mavenRepository/) 
![Relase Build Status](https://img.shields.io/github/actions/workflow/status/brianhsu/Live2DForScala/release.yaml?logo=github&style=for-the-badge&label=RELEASE%20BUILD)
![Development Build Status](https://img.shields.io/github/actions/workflow/status/brianhsu/Live2DForScala/unitTest.yaml?branch=develop&logo=github&style=for-the-badge&label=DEV%20BUILD) 
![Code Coverage](doc/images/coverage.svg)

zh_CN [简体中文](README.zh_CN.md)
--
Disclaimer
-----------

1. This project is **NOT** affiliate nor endorsed Live2D Inc, explicitly or implicitly.
2. This is a project that helps me learning how to interact with native libraries in Scala/JVM, and how to apply Clean Architecture in a real life scenario. Because such, although the core library of this project is relative fully featured, it should **NOT** be considered as a replacement for the official Live 2D Cubism SDK.
3. As above, this project may be abandoned if I don't interested in this topic any more. Use it at your own risk.

Background
-----------


I'm tired that there is no good webcam to Live2D program in Linux. Although the [facial-landmarks-for-cubism][0] provides a functional program, it requires user to have some decent knowledge about how to patch and build a C++ program in order to use.

The ultimate goal of this project is to provide something like [VTuber Studio][1] in the Linux world. Maybe with less features, but should be with a good out-of-box experience.

The whole idea of this project it that by leverage the [Clean Architecture][2] concept proposed by Robert C. Martin (Uncle Bob), combined with it's written in Scala/JVM, it should able to run on both Linux / Windows / MacOS Intel without the change in the source code.

### Acknowledgement

This project is heavily inspired by [facial-landmarks-for-cubism][0]. More specifically, the algorithm about how to convert OpenSeeFace data point into Live2D parameter in this project, are ported from [facial-landmarks-for-cubism][0].

Screenshots
--------------------

### Swing + JOGL Version Under Linux
![Screenshot of Swing Demo](doc/images/swing1.png)

![Screenshot of Swing Demo Face Tracking](doc/images/swing2.png)

### SWT+LWJGL Version under Linux
![Screenshot of SWT Demo](doc/images/swt1.png)

![Screenshot of SWT Demo Face Tracking](doc/images/swt2.png)


Feature
--------------------

The core library support much the same functionality you will find in the official Cubism SDK for Native and the sample application of it.

The following list shows features that this project currently supports or plan to be implemented.

### Feature Included in Official Cubism SDK for Native.
  - [x] Motion (Loop or single time)
  - [x] Model motion event listener
  - [x] Expression
  - [x] Physics
  - [x] Auto eye-blink
  - [x] Auto breath effect
  - [x] Face direction control by mouse
  - [x] Lip sync from .WAV file in the avatar motion

### Advance Feature
  - [x] Lip sync from microphone
  - [x] Use webcam to control Live2D avatar (VTuber like)

### Won't Implemented Feature

Some feature inside Cubism SDK for Native is dropped intended. Because I think these feature are not really useful for create a VTuber program. 

The following is the list of such features.

  - Priority motion queue

TODO List
--------------------

  - [ ] Control OpenSeeFace -> Live2D model motion parameter through UI.
  - [ ] Live2D model body movement by face tracking.

Supported Platforms
--------------------

Since this project is built on Clean Architecture concept and runs on Java Virtual Machine, it's able to use different Java OpenGL binding API. By default, it provids [JavaOpenGL][3] binding and [LWJGL][4] + [SWT][5] binding.

In theory, it should able to use differnt combination of OpenGL Java libary and GUI toolkit, but the examples come with this project use the following comination.

Due to some weired bug, I couldn't make the example program runs on SWT under MacOS. Also, currently it only support MacOS running on Intel processors, Apple M1 version of MacOS is not supported.

| OS / Binding  | Architecture | OpenGL Binding     | GUI Toolkit |Supported          | Note
| ------------- |------------- | ------------------ |------------ |------------------ |------
| Linux         | x86_64       | JavaOpenGL         | Swing       |:heavy_check_mark: |
| Linux         | x86_64       | LWJGL              | SWT         |:heavy_check_mark: |
| Windows 10    | x86_64       | JavaOpenGL         | Swing       |:heavy_check_mark: |
| Windows 10    | x86_64       | LWJGL              | SWT         |:heavy_check_mark: |
| MacOS         | x86_64       | JavaOpenGL         | Swing       |:heavy_check_mark: | 1
| MacOS         | x86_64       | LWJGL              | SWT         |:x:                | 

1. Must use jogamp-fat-v2.4.0-rc-20210111.jar, auto pull-in dependency from Maven Central will not work.

Install & Usage
--------------------

See [doc/INSTALL.md](doc/INSTALL.md) for detailed instruction on how to download the demo application and sample Live 2D model for use.

Both Swing and SWT version of demo application provide same functionality. The following is some basic control scheme.

1. Click `Load Avatar` button on the top-left corner to load Live2D model.

    1.1 You must select a folder contains a valid `.moc3` file.

2. User panel on the left to control effects / motions / expressions.
3. Right click on the avatar and drag to move the avatar around.
4. Use mouse wheel to zoom-in / zoom-out the avatar.


Project Structure and Design
-----------------------------

Since the project follows the Clean Architecture design philosophy, it's seperated into multiple modules. Those modules are SBT sub-projects under the `modules` directory.

The following is the overview of this project's structure and breif explanation on each of those modules.

See [modules/README.md](modules/README.md) for detailed documentation of each component and how to use this project's core library in your own project to control Live / render Live2D model.

```console
.
├── build.sbt   # SBT build definition
├── doc
├── modules     
│   ├── core         # The core library to control / render Live2D model.
│   ├── joglBinding  # The Java OpenGL binding
│   ├── lwjglBinding # The LWJGL binding
│   ├─── swtBinding  # The SWT GLCanvas binding
│   └──── examples
│      ├── base      # The base demo application without any GUI toolkit dependency. 
│      ├── swing     # The full Swing version demo application.
│      ├── swt       # The SWT version demo application without SWT runtime.
│      ├── swt-linux-bundle   # The SWT version demo application with Linux SWT runtime.
│      └── swt-windows-bundle # The SWT version demo application with Windows SWT runtime.
├── publish.sbt # Maven publish settings
├── release.sbt # Release settings
├── README.md  
└── version.sbt # Version settings
```

Build Instruction
--------------------

### 1. Install OpenJDK 11

- For Windows, download [Microsoft Build of OpenJDK](https://docs.microsoft.com/en-us/java/openjdk/download) and install it.
- For Linux, install it through your distro's package manager.
- For MacOS
    1. Install Homebrew
    2. Run `brew install openjdk@11` to install

### 2. Install [SBT][6] (Simple Build Tool)

- Follow the [Download](https://www.scala-sbt.org/download.html) page of SBT to install it.

### 3. Compile

1. After SBT is installed, run the following command the clone this project from GitHub.

```console
username@hostname:~$ git clone https://github.com/brianhsu/Live2DForScala.git
Cloning into 'Live2DForScala'...
remote: Enumerating objects: 10853, done.
remote: Counting objects: 100% (30/30), done.
remote: Compressing objects: 100% (23/23), done.
remote: Total 10853 (delta 6), reused 22 (delta 1), pack-reused 10823
Receiving objects: 100% (10853/10853), 67.14 MiB | 7.30 MiB/s, done.
Resolving deltas: 100% (4483/4483), done.

username@hostname:~$
```

2. Change directory into it and type `sbt` to run SBT console. It may take a while to download files for the first time of execution.

```console
username@hostname:~$ cd Live2DForScala
username@hostname:~/Live2DForScala$ sbt
copying runtime jar...
[info] [launcher] getting org.scala-sbt sbt 1.5.8  (this may take some time)...
:: loading settings :: url = jar:file:/usr/share/sbt-bin/lib/sbt-launch.jar!/org/apache/ivy/core/settings/ivysettings.xml
:: loading settings :: url = jar:file:/usr/share/sbt-bin/lib/sbt-launch.jar!/org/apache/ivy/core/settings/ivysettings.xml
....
[info] welcome to sbt 1.5.8 (Eclipse Adoptium Java 11.0.15)
[info] loading global plugins from /home/brianhsu/.sbt/1.0/plugins
[info] loading settings for project live2dforscala-build from plugins.sbt ...
[info] loading project definition from /home/brianhsu/Live2DForScala/project
[info] loading settings for project core from build.sbt ...
[info] loading settings for project lwjglBinding from build.sbt ...
[info] loading settings for project live2dforscala from build.sbt,publish.sbt,version.sbt ...
[info] set current project to live2dforscala (in build file:/home/brianhsu/Live2DForScala/)
[info] sbt server started at local:///home/brianhsu/.sbt/1.0/server/7f96e432f44ce5ee45c1/sock
[info] started sbt server
sbt:live2dforscala> 
```

3. Compile it by typing `compile` in the SBT console.

```console
sbt:live2dforscala> compile
[info] compiling 154 Scala sources and 1 Java source to /home/brianhsu/Live2DForScala/modules/core/target/scala-2.13/classes ...
[info] Non-compiled module 'compiler-bridge_2.13' for Scala 2.13.8. Compiling...
[info]   Compilation completed in 4.259s.
[info] compiling 2 Scala sources to /home/brianhsu/Live2DForScala/modules/lwjglBinding/target/scala-2.13/classes ...
[info] compiling 1 Scala source to /home/brianhsu/Live2DForScala/modules/swtBinding/target/scala-2.13/classes ...
[info] compiling 3 Scala sources to /home/brianhsu/Live2DForScala/modules/joglBinding/target/scala-2.13/classes ...
[info] compiling 7 Scala sources to /home/brianhsu/Live2DForScala/modules/examples/base/target/scala-2.13/classes ...
[info] compiling 8 Scala sources to /home/brianhsu/Live2DForScala/modules/examples/swt/target/scala-2.13/classes ...
[info] compiling 8 Scala sources to /home/brianhsu/Live2DForScala/modules/examples/swing/target/scala-2.13/classes ...
sbt:live2dforscala> 
```

### 3. Compile

- Type `test` in SBT console to run unit test.
- Since MacOS does not support SWT+JWJGL, SWTOpenGLCanvasInfoFeature would faild. It's expected.

```console
sbt:live2dforscala> test
[info] compiling 1 Scala source to /home/brianhsu/Live2DForScala/modules/swtBinding/target/scala-2.13/test-classes ...
[info] compiling 1 Scala source to /home/brianhsu/Live2DForScala/modules/lwjglBinding/target/scala-2.13/test-classes ...
[info] compiling 3 Scala sources to /home/brianhsu/Live2DForScala/modules/joglBinding/target/scala-2.13/test-classes ...
[info] Run completed in 326 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] SWTOpenGLCanvasInfoFeature:
[info] Feature: Get canvas information
[info]   Scenario: Get canvas information from SWT OpenGL Canvas
....
[info] Tests: succeeded 394, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
sbt:live2dforscala> 
```

### 3. Run Demo Application

Type the following command in SBT console to run the demo application.

- `exampleSwing/run` to run Swing version. Works for Linux / Windows / MacOS.
- `exampleSWTLinux/run` to run SWT version on Linux.
- `exampleSWTWin/run` to run SWT version on Windows.

```console
sbt:live2dforscala> exampleSwing/run
[info] running (fork) moe.brianhsu.live2d.demo.swing.SwingMain 
```

```console
sbt:live2dforscala> exampleSWTLinux/run                    
[info] running (fork) moe.brianhsu.live2d.demo.swing.SwingMain
```

License
==============================

The library and the example program of this project itself is licensed under the MIT license. Please note that this repository also contains files that are belong to other copyright holders, please see the following section for detail. Those files are not considered as "ths project itself" in previous sentence.

You must agree to those license agreement in order to use this program.

3rd Party Component Licenses
==============================

This project is based on some 3rd party resources, the following denoted those resources and thier licenses.

Live2D Cubism Core
--------------------

This project uses Live2D Cubism Core native library during runtime, it's located at `modules/core/src/main/resources/`.

```
modules/core/src/main/resources/linux-x86-64/libLive2DCubismCore.so
modules/core/src/main/resources/win32-x86-64/Live2DCubismCore.dll
modules/core/src/main/resources/win32-x86-64/Live2DCubismCore.lib
modules/core/src/main/resources/darwin/libLive2DCubismCore.dylib
modules/core/src/main/resources/LICENSE.md
```

Live2D Cubism Core is available under Live2D Proprietary Software License.

* [Live2D Proprietary Software License Agreement](https://www.live2d.com/eula/live2d-proprietary-software-license-agreement_en.html)
* [Live2D Proprietary Software 使用許諾契約書](https://www.live2d.com/eula/live2d-proprietary-software-license-agreement_jp.html)
* [Live2D Proprietary Software 使用授权协议](https://www.live2d.com/eula/live2d-proprietary-software-license-agreement_cn.html)

Live2D Model
--------------------

In order to do proper unit test to make sure this project's quality, the Live2D model located at `modules/core/src/test/resources/models` are used during unit test. And is included in this project's git repository.

We used it under the Free Material License Agreement from Live2D Inc.

* [Free Material License Agreement](https://www.live2d.com/eula/live2d-free-material-license-agreement_en.html)
* [無償提供マテリアルの使用許諾契約書](https://www.live2d.com/eula/live2d-free-material-license-agreement_jp.html)
* [无偿提供素材使用授权协议](https://www.live2d.com/eula/live2d-free-material-license-agreement_cn.html)

facial-landmarks-for-cubism
-----------------------------

The OpenSeeFace data point to Live2D parameters' algorithm in this project is mainly from [adrianiainlam/facial-landmarks-for-cubism][0]. 

Although this project does not include the original C++ version source code, it includes comments which could be found at above project regrading the explanation of various calculation.

[facial-landmarks-for-cubism][0] is released under MIT License with the following declaration:

```
Copyright (c) 2020 Adrian I. Lam

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

OpenSeeFace
--------------

### OpenSeeFace

The released package contains a bundled pre-built [OpenSeeFace][0] executable binary, which is released under `BSD-2-Clause` license.

### Dependencies

The bunlded OpenSeeFace package also contains it's dependencies, namely:

 - [OpenCV](https://opencv.org/) released under Apache License.
 - [ONNX Runtime](https://pypi.org/project/ort-nightly/) released under MIT License.
 - [Pillow](https://pypi.org/project/Pillow/) released under Historical Permission Notice and Disclaimer.
 - [Numpy](https://pypi.org/project/numpy/) released under BSD License.

Background
--------------

The default background in the example program is licensed under [Freepik License][11] by:

- [Japanese koi vector created by rawpixel.com - www.freepik.com][7]

Icons
--------------

The power / gear / speaker icons in the example are licensed under [Flaticon License][12]  by:

- [Power icons created by Gregor Cresnar - Flaticon][8]
- [Settings icons created by Gregor Cresnar Premium - Flaticon][9]
- [Speaker icons created by Freepik - Flaticon][10]

[0]: https://github.com/adrianiainlam/facial-landmarks-for-cubism
[1]: https://store.steampowered.com/app/1325860/VTube_Studio/
[2]: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[3]: https://jogamp.org/jogl/www/
[4]: https://www.lwjgl.org/
[5]: https://www.eclipse.org/swt/
[6]: https://www.scala-sbt.org/
[7]: https://www.freepik.com/vectors/japanese-koi
[8]: https://www.flaticon.com/free-icons/power
[9]: https://www.flaticon.com/free-icons/settings
[10]: https://www.flaticon.com/free-icons/speaker
[11]: https://www.freepikcompany.com/legal#nav-freepik-license
[12]: https://www.freepikcompany.com/legal#nav-flaticon
