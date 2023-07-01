package moe.brianhsu.live2d.exception

class MocInconsistentException(filePath: String) extends Exception(s"The loaded .moc3 $filePath is inconsistent")
