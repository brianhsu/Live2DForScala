CHANGELOG
================

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Add 120 / 150 camera fps to face tracking setting panel.

### Fixed

- A potential out of bound error in FaceDirection tab in Swing Demo App.

### Changed

- Refine CI pipeline.
  - Use GitHub shared actions for building test environment / run unit test...etc.
  - Use Debian as a building environment in GitHub Actions CI pipeline.
  - Enable AudioOutputTest in GitHub Actions CI pipeline.

## v2.0.2

### Fixed

- Fixed some Live2D model will yield `IndexOutOfBoundException: Required 1 remaining elements in buffer, only had 0` when using JavaOpenGLBinding to render the Live2D model. (Issue [#3](https://github.com/brianhsu/Live2DForScala/issues/3))
- Fixed Java Swing Example yield `java.lang.IllegalArgumentException: setSelectedIndex: 0 out of bounds` and crash during startup if there is no webcam.

## v2.0.1

### Fixed

- Fixed `FileNotFound` exception when use pre-built Maven JAR files and loading avatar through AvatarFileReader. (Issue [#2](https://github.com/brianhsu/Live2DForScala/issues/2)).

## v2.0.0

This version basically aligned to Cubism Native SDK 4-r.5-beta.4.

### Added

- Added `csmGetParameterTypes` function to `NativeCubismAPI` trait.
- Added `csmHasMocConsistency' function to 'NativeCubismAPI` trait.
- Added `csmGetParameterKeyValues` function to `NativeCubismAPI` trait.
- Added `csmGetParameterKeyCounts` function to `NativeCubismAPI` trait.
- Added `csmGetDrawableParentPartIndices` function to `NativeCubismAPI` trait.

- Added `parameterType` field to Parameter trait, to inidicate is this parameter a normal one or a blend shape one.
- Added 'keyValues' field to Parameter trait, to indicate the key values of a certian parameter.
- Added `revivedMoc` and `mocVersion` field to `MocInfo` class.
- Added 'shouldCheckConsistent' parameter to constructor of `MocInfoFileReader` / `AvatarFileReader`, which controls should we check consistency of the loaded `.moc3` file or not.
- Added `parentPartIndexHolder` field to `Drawable` class, to inidicate the parent part index of that drawable.

### Changed

- Update Cubism Core Native Library to version 4.2.4.
- Make face tracking motion more smoothly.

### Fixed

- Fix the signature of `NativeCubismAPI#csmGetMocVersion`

### Breaking API Changes

- Move Parameter / JVMParameter / CPointerParameter to separate package `moe.brianhsu.live2d.enitiy.model.parameter`.


## v1.0.0

### Added

- Added `UDPOpenSeeFaceDataReader' API.
- Added `FaceTracking` / `OpenSeeFaceTracking` effect.
- Added `isOldParameterId` field to 'Live2DModel` to indicates if it use old parameter id form.
- Added `EasyUpdateStrategy` that has a easy to use API.

### Changed

- `ModelUpdater` now will convert new parameter id format to old format when using old model.

### Removed

- Remove the adapter dependency in AvatarMotion factory method.

### Breaking API Changes

- Rename `BasicUpdateStrategy` to `GenericUpdateStrategyFeature`
- Major change on `GenericUpdateStrategyFeature` API.

