package moe.brianhsu.live2d.exception

class ShaderLinkException(val programId: Int, val log: String) extends Exception(s"Link shader program error. programId=$programId, log=$log")
