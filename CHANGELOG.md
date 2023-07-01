CHANGELOG
================

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

[Unreleased]
-------------

### Added

- Added `csmGetParameterTypes` function to `NativeCubismAPI` trait.
- Added `csmHasMocConsistency' function to 'NativeCubismAPI` trait.

- Added `parameterType` field to Parameter trait, to inidicate is this parameter a normal one or a blend shape one.
- Added `revivedMoc` and `mocVersion` field to `MocInfo` class.
- Added 'shouldCheckConsistent' parameter to constructor of `MocInfoFileReader` / `AvatarFileReader`, which controls should we check consistency of the loaded `.moc3` file or not.

### Changed

- Update Cubism Core Native Library to version 4.2.4.

### Fixed

- Fix the signature of `NativeCubismAPI#csmGetMocVersion`

### Breaking API Changes

- Move Parameter / JVMParameter / CPointerParameter to separate package `moe.brianhsu.live2d.enitiy.model.parameter`.


1.0.0
-------------

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

