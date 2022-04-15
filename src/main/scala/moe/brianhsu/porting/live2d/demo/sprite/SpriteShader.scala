package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.shader.BaseShader

class SpriteShader(implicit gl: OpenGLBinding) extends BaseShader {
  override def vertexShaderSource: String =
    """
      | #version 120
      | attribute vec3 position;
      | attribute vec2 uv;
      | varying vec2 vuv;
      | void main(void){
      |     gl_Position = vec4(position, 1.0);
      |     vuv = uv;
      | }
      | """.stripMargin.trim

  override def fragmentShaderSource: String =
    """
      | #version 120
      | varying vec2 vuv;
      | uniform sampler2D texture;
      | uniform vec4 baseColor;
      | void main(void){
      |     gl_FragColor = texture2D(texture, vuv) * baseColor;
      | }
      | """.stripMargin.trim
}
