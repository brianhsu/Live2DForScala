CHANGELOG
================

1.0.0
---------

### Core API

- Added Features
  - `UDPOpenSeeFaceDataReader' API.
  - `FaceTracking` / `OpenSeeFaceTracking` effect.
  - `Live2DModel` has new `isOldParameterId` attribute indicates if it use old parameter id form.
  - `ModelUpdater` now will convert new parameter id format to old format when using old model.
  - `EasyUpdateStrategy` that has a easy to use API.
- Breaking API Changes
  - Remove the adapter dependency in AvatarMotion factory method.
  - Rename `BasicUpdateStrategy` to `GenericUpdateStrategyFeature`
  - Major change on `GenericUpdateStrategyFeature` API.
