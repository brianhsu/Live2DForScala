package moe.brianhsu.live2d.exception

class NativeLibraryNotFoundError(cause: Throwable) extends Error("Cannot found native Live2DCubismCore library", cause)
