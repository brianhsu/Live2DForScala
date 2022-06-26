package moe.brianhsu.live2d.exception

class ShaderCompileException(val shaderId: Int, val log: String) extends Exception(s"Compile shader program error. shaderId=$shaderId, log=$log")
