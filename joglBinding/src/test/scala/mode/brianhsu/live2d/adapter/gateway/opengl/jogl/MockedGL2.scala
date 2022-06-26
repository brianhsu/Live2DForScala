package mode.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.common.nio.PointerBuffer
import com.jogamp.opengl.{GL, GL2, GL2ES1, GL2ES2, GL2ES3, GL2GL3, GL3, GL3ES3, GL3bc, GL4, GL4ES3, GL4bc, GLArrayData, GLBufferStorage, GLContext, GLES1, GLES2, GLES3, GLProfile, GLUniformData}

import java.nio.{Buffer, ByteBuffer, DoubleBuffer, FloatBuffer, IntBuffer, LongBuffer, ShortBuffer}

class MockedGL2 extends GL2 {
  override def glClearIndex(v: Float): Unit = ???

  override def glIndexMask(i: Int): Unit = ???

  override def glLineStipple(i: Int, i1: Short): Unit = ???

  override def glPolygonStipple(byteBuffer: ByteBuffer): Unit = ???

  override def glPolygonStipple(bytes: Array[Byte], i: Int): Unit = ???

  override def glPolygonStipple(l: Long): Unit = ???

  override def glGetPolygonStipple(byteBuffer: ByteBuffer): Unit = ???

  override def glGetPolygonStipple(bytes: Array[Byte], i: Int): Unit = ???

  override def glGetPolygonStipple(l: Long): Unit = ???

  override def glEdgeFlag(b: Boolean): Unit = ???

  override def glEdgeFlagv(byteBuffer: ByteBuffer): Unit = ???

  override def glEdgeFlagv(bytes: Array[Byte], i: Int): Unit = ???

  override def glClipPlane(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glClipPlane(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glGetClipPlane(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetClipPlane(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glPushAttrib(i: Int): Unit = ???

  override def glPopAttrib(): Unit = ???

  override def glRenderMode(i: Int): Int = ???

  override def glClearAccum(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glAccum(i: Int, v: Float): Unit = ???

  override def glLoadMatrixd(doubleBuffer: DoubleBuffer): Unit = ???

  override def glLoadMatrixd(doubles: Array[Double], i: Int): Unit = ???

  override def glMultMatrixd(doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultMatrixd(doubles: Array[Double], i: Int): Unit = ???

  override def glRotated(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glScaled(v: Double, v1: Double, v2: Double): Unit = ???

  override def glTranslated(v: Double, v1: Double, v2: Double): Unit = ???

  override def glIsList(i: Int): Boolean = ???

  override def glDeleteLists(i: Int, i1: Int): Unit = ???

  override def glGenLists(i: Int): Int = ???

  override def glNewList(i: Int, i1: Int): Unit = ???

  override def glEndList(): Unit = ???

  override def glCallList(i: Int): Unit = ???

  override def glCallLists(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glListBase(i: Int): Unit = ???

  override def glBegin(i: Int): Unit = ???

  override def glEnd(): Unit = ???

  override def glVertex2d(v: Double, v1: Double): Unit = ???

  override def glVertex2f(v: Float, v1: Float): Unit = ???

  override def glVertex2i(i: Int, i1: Int): Unit = ???

  override def glVertex2s(i: Short, i1: Short): Unit = ???

  override def glVertex3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glVertex3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glVertex3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertex3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glVertex4d(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glVertex4f(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glVertex4i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertex4s(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glVertex2dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertex2dv(doubles: Array[Double], i: Int): Unit = ???

  override def glVertex2fv(floatBuffer: FloatBuffer): Unit = ???

  override def glVertex2fv(floats: Array[Float], i: Int): Unit = ???

  override def glVertex2iv(intBuffer: IntBuffer): Unit = ???

  override def glVertex2iv(ints: Array[Int], i: Int): Unit = ???

  override def glVertex2sv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex2sv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertex3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertex3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glVertex3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glVertex3fv(floats: Array[Float], i: Int): Unit = ???

  override def glVertex3iv(intBuffer: IntBuffer): Unit = ???

  override def glVertex3iv(ints: Array[Int], i: Int): Unit = ???

  override def glVertex3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertex4dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertex4dv(doubles: Array[Double], i: Int): Unit = ???

  override def glVertex4fv(floatBuffer: FloatBuffer): Unit = ???

  override def glVertex4fv(floats: Array[Float], i: Int): Unit = ???

  override def glVertex4iv(intBuffer: IntBuffer): Unit = ???

  override def glVertex4iv(ints: Array[Int], i: Int): Unit = ???

  override def glVertex4sv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex4sv(shorts: Array[Short], i: Int): Unit = ???

  override def glNormal3b(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glNormal3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glNormal3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glNormal3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glNormal3bv(byteBuffer: ByteBuffer): Unit = ???

  override def glNormal3bv(bytes: Array[Byte], i: Int): Unit = ???

  override def glNormal3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glNormal3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glNormal3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glNormal3fv(floats: Array[Float], i: Int): Unit = ???

  override def glNormal3iv(intBuffer: IntBuffer): Unit = ???

  override def glNormal3iv(ints: Array[Int], i: Int): Unit = ???

  override def glNormal3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glNormal3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glIndexd(v: Double): Unit = ???

  override def glIndexf(v: Float): Unit = ???

  override def glIndexi(i: Int): Unit = ???

  override def glIndexs(i: Short): Unit = ???

  override def glIndexdv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glIndexdv(doubles: Array[Double], i: Int): Unit = ???

  override def glIndexfv(floatBuffer: FloatBuffer): Unit = ???

  override def glIndexfv(floats: Array[Float], i: Int): Unit = ???

  override def glIndexiv(intBuffer: IntBuffer): Unit = ???

  override def glIndexiv(ints: Array[Int], i: Int): Unit = ???

  override def glIndexsv(shortBuffer: ShortBuffer): Unit = ???

  override def glIndexsv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor3b(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glColor3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glColor3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glColor3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glColor3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glColor3ub(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glColor3ui(i: Int, i1: Int, i2: Int): Unit = ???

  override def glColor3us(i: Short, i1: Short, i2: Short): Unit = ???

  override def glColor4b(b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glColor4d(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glColor4i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glColor4s(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glColor4ui(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glColor4us(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glColor3bv(byteBuffer: ByteBuffer): Unit = ???

  override def glColor3bv(bytes: Array[Byte], i: Int): Unit = ???

  override def glColor3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glColor3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glColor3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glColor3fv(floats: Array[Float], i: Int): Unit = ???

  override def glColor3iv(intBuffer: IntBuffer): Unit = ???

  override def glColor3iv(ints: Array[Int], i: Int): Unit = ???

  override def glColor3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor3ubv(byteBuffer: ByteBuffer): Unit = ???

  override def glColor3ubv(bytes: Array[Byte], i: Int): Unit = ???

  override def glColor3uiv(intBuffer: IntBuffer): Unit = ???

  override def glColor3uiv(ints: Array[Int], i: Int): Unit = ???

  override def glColor3usv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor3usv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor4bv(byteBuffer: ByteBuffer): Unit = ???

  override def glColor4bv(bytes: Array[Byte], i: Int): Unit = ???

  override def glColor4dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glColor4dv(doubles: Array[Double], i: Int): Unit = ???

  override def glColor4fv(floatBuffer: FloatBuffer): Unit = ???

  override def glColor4fv(floats: Array[Float], i: Int): Unit = ???

  override def glColor4iv(intBuffer: IntBuffer): Unit = ???

  override def glColor4iv(ints: Array[Int], i: Int): Unit = ???

  override def glColor4sv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor4sv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor4ubv(byteBuffer: ByteBuffer): Unit = ???

  override def glColor4ubv(bytes: Array[Byte], i: Int): Unit = ???

  override def glColor4uiv(intBuffer: IntBuffer): Unit = ???

  override def glColor4uiv(ints: Array[Int], i: Int): Unit = ???

  override def glColor4usv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor4usv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord1d(v: Double): Unit = ???

  override def glTexCoord1f(v: Float): Unit = ???

  override def glTexCoord1i(i: Int): Unit = ???

  override def glTexCoord1s(i: Short): Unit = ???

  override def glTexCoord2d(v: Double, v1: Double): Unit = ???

  override def glTexCoord2f(v: Float, v1: Float): Unit = ???

  override def glTexCoord2i(i: Int, i1: Int): Unit = ???

  override def glTexCoord2s(i: Short, i1: Short): Unit = ???

  override def glTexCoord3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glTexCoord3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glTexCoord3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTexCoord3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glTexCoord4d(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glTexCoord4f(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glTexCoord4i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTexCoord4s(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glTexCoord1dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glTexCoord1dv(doubles: Array[Double], i: Int): Unit = ???

  override def glTexCoord1fv(floatBuffer: FloatBuffer): Unit = ???

  override def glTexCoord1fv(floats: Array[Float], i: Int): Unit = ???

  override def glTexCoord1iv(intBuffer: IntBuffer): Unit = ???

  override def glTexCoord1iv(ints: Array[Int], i: Int): Unit = ???

  override def glTexCoord1sv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord1sv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord2dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glTexCoord2dv(doubles: Array[Double], i: Int): Unit = ???

  override def glTexCoord2fv(floatBuffer: FloatBuffer): Unit = ???

  override def glTexCoord2fv(floats: Array[Float], i: Int): Unit = ???

  override def glTexCoord2iv(intBuffer: IntBuffer): Unit = ???

  override def glTexCoord2iv(ints: Array[Int], i: Int): Unit = ???

  override def glTexCoord2sv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord2sv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glTexCoord3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glTexCoord3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glTexCoord3fv(floats: Array[Float], i: Int): Unit = ???

  override def glTexCoord3iv(intBuffer: IntBuffer): Unit = ???

  override def glTexCoord3iv(ints: Array[Int], i: Int): Unit = ???

  override def glTexCoord3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord4dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glTexCoord4dv(doubles: Array[Double], i: Int): Unit = ???

  override def glTexCoord4fv(floatBuffer: FloatBuffer): Unit = ???

  override def glTexCoord4fv(floats: Array[Float], i: Int): Unit = ???

  override def glTexCoord4iv(intBuffer: IntBuffer): Unit = ???

  override def glTexCoord4iv(ints: Array[Int], i: Int): Unit = ???

  override def glTexCoord4sv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord4sv(shorts: Array[Short], i: Int): Unit = ???

  override def glRasterPos2d(v: Double, v1: Double): Unit = ???

  override def glRasterPos2f(v: Float, v1: Float): Unit = ???

  override def glRasterPos2i(i: Int, i1: Int): Unit = ???

  override def glRasterPos2s(i: Short, i1: Short): Unit = ???

  override def glRasterPos3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glRasterPos3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glRasterPos3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glRasterPos3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glRasterPos4d(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glRasterPos4f(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glRasterPos4i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glRasterPos4s(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glRasterPos2dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glRasterPos2dv(doubles: Array[Double], i: Int): Unit = ???

  override def glRasterPos2fv(floatBuffer: FloatBuffer): Unit = ???

  override def glRasterPos2fv(floats: Array[Float], i: Int): Unit = ???

  override def glRasterPos2iv(intBuffer: IntBuffer): Unit = ???

  override def glRasterPos2iv(ints: Array[Int], i: Int): Unit = ???

  override def glRasterPos2sv(shortBuffer: ShortBuffer): Unit = ???

  override def glRasterPos2sv(shorts: Array[Short], i: Int): Unit = ???

  override def glRasterPos3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glRasterPos3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glRasterPos3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glRasterPos3fv(floats: Array[Float], i: Int): Unit = ???

  override def glRasterPos3iv(intBuffer: IntBuffer): Unit = ???

  override def glRasterPos3iv(ints: Array[Int], i: Int): Unit = ???

  override def glRasterPos3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glRasterPos3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glRasterPos4dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glRasterPos4dv(doubles: Array[Double], i: Int): Unit = ???

  override def glRasterPos4fv(floatBuffer: FloatBuffer): Unit = ???

  override def glRasterPos4fv(floats: Array[Float], i: Int): Unit = ???

  override def glRasterPos4iv(intBuffer: IntBuffer): Unit = ???

  override def glRasterPos4iv(ints: Array[Int], i: Int): Unit = ???

  override def glRasterPos4sv(shortBuffer: ShortBuffer): Unit = ???

  override def glRasterPos4sv(shorts: Array[Short], i: Int): Unit = ???

  override def glRectd(v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glRectf(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glRecti(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glRects(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glRectdv(doubleBuffer: DoubleBuffer, doubleBuffer1: DoubleBuffer): Unit = ???

  override def glRectdv(doubles: Array[Double], i: Int, doubles1: Array[Double], i1: Int): Unit = ???

  override def glRectfv(floatBuffer: FloatBuffer, floatBuffer1: FloatBuffer): Unit = ???

  override def glRectfv(floats: Array[Float], i: Int, floats1: Array[Float], i1: Int): Unit = ???

  override def glRectiv(intBuffer: IntBuffer, intBuffer1: IntBuffer): Unit = ???

  override def glRectiv(ints: Array[Int], i: Int, ints1: Array[Int], i1: Int): Unit = ???

  override def glRectsv(shortBuffer: ShortBuffer, shortBuffer1: ShortBuffer): Unit = ???

  override def glRectsv(shorts: Array[Short], i: Int, shorts1: Array[Short], i1: Int): Unit = ???

  override def glLighti(i: Int, i1: Int, i2: Int): Unit = ???

  override def glLightiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glLightiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetLightiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetLightiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glLightModeli(i: Int, i1: Int): Unit = ???

  override def glLightModeliv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glLightModeliv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMateriali(i: Int, i1: Int, i2: Int): Unit = ???

  override def glMaterialiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glMaterialiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetMaterialiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMaterialiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glColorMaterial(i: Int, i1: Int): Unit = ???

  override def glPixelZoom(v: Float, v1: Float): Unit = ???

  override def glPixelTransferf(i: Int, v: Float): Unit = ???

  override def glPixelTransferi(i: Int, i1: Int): Unit = ???

  override def glPixelMapfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glPixelMapfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glPixelMapfv(i: Int, i1: Int, l: Long): Unit = ???

  override def glPixelMapuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glPixelMapuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glPixelMapuiv(i: Int, i1: Int, l: Long): Unit = ???

  override def glPixelMapusv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glPixelMapusv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glPixelMapusv(i: Int, i1: Int, l: Long): Unit = ???

  override def glGetPixelMapfv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetPixelMapfv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetPixelMapfv(i: Int, l: Long): Unit = ???

  override def glGetPixelMapuiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetPixelMapuiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetPixelMapuiv(i: Int, l: Long): Unit = ???

  override def glGetPixelMapusv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glGetPixelMapusv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glGetPixelMapusv(i: Int, l: Long): Unit = ???

  override def glBitmap(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float, byteBuffer: ByteBuffer): Unit = ???

  override def glBitmap(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float, bytes: Array[Byte], i2: Int): Unit = ???

  override def glBitmap(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float, l: Long): Unit = ???

  override def glDrawPixels(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glDrawPixels(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glCopyPixels(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glTexGend(i: Int, i1: Int, v: Double): Unit = ???

  override def glTexGenf(i: Int, i1: Int, v: Float): Unit = ???

  override def glTexGeni(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTexGendv(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glTexGendv(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glTexGenfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glTexGenfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glTexGeniv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glTexGeniv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetTexGendv(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetTexGendv(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetTexGenfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTexGenfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetTexGeniv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexGeniv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glMap1d(i: Int, v: Double, v1: Double, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMap1d(i: Int, v: Double, v1: Double, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glMap1f(i: Int, v: Float, v1: Float, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMap1f(i: Int, v: Float, v1: Float, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glMap2d(i: Int, v: Double, v1: Double, i1: Int, i2: Int, v2: Double, v3: Double, i3: Int, i4: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMap2d(i: Int, v: Double, v1: Double, i1: Int, i2: Int, v2: Double, v3: Double, i3: Int, i4: Int, doubles: Array[Double], i5: Int): Unit = ???

  override def glMap2f(i: Int, v: Float, v1: Float, i1: Int, i2: Int, v2: Float, v3: Float, i3: Int, i4: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMap2f(i: Int, v: Float, v1: Float, i1: Int, i2: Int, v2: Float, v3: Float, i3: Int, i4: Int, floats: Array[Float], i5: Int): Unit = ???

  override def glGetMapdv(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetMapdv(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetMapfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMapfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetMapiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMapiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glEvalCoord1d(v: Double): Unit = ???

  override def glEvalCoord1f(v: Float): Unit = ???

  override def glEvalCoord1dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glEvalCoord1dv(doubles: Array[Double], i: Int): Unit = ???

  override def glEvalCoord1fv(floatBuffer: FloatBuffer): Unit = ???

  override def glEvalCoord1fv(floats: Array[Float], i: Int): Unit = ???

  override def glEvalCoord2d(v: Double, v1: Double): Unit = ???

  override def glEvalCoord2f(v: Float, v1: Float): Unit = ???

  override def glEvalCoord2dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glEvalCoord2dv(doubles: Array[Double], i: Int): Unit = ???

  override def glEvalCoord2fv(floatBuffer: FloatBuffer): Unit = ???

  override def glEvalCoord2fv(floats: Array[Float], i: Int): Unit = ???

  override def glMapGrid1d(i: Int, v: Double, v1: Double): Unit = ???

  override def glMapGrid1f(i: Int, v: Float, v1: Float): Unit = ???

  override def glMapGrid2d(i: Int, v: Double, v1: Double, i1: Int, v2: Double, v3: Double): Unit = ???

  override def glMapGrid2f(i: Int, v: Float, v1: Float, i1: Int, v2: Float, v3: Float): Unit = ???

  override def glEvalPoint1(i: Int): Unit = ???

  override def glEvalPoint2(i: Int, i1: Int): Unit = ???

  override def glEvalMesh1(i: Int, i1: Int, i2: Int): Unit = ???

  override def glEvalMesh2(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glFogi(i: Int, i1: Int): Unit = ???

  override def glFogiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glFogiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glFeedbackBuffer(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glPassThrough(v: Float): Unit = ???

  override def glSelectBuffer(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glInitNames(): Unit = ???

  override def glLoadName(i: Int): Unit = ???

  override def glPushName(i: Int): Unit = ???

  override def glPopName(): Unit = ???

  override def glIndexub(b: Byte): Unit = ???

  override def glIndexubv(byteBuffer: ByteBuffer): Unit = ???

  override def glIndexubv(bytes: Array[Byte], i: Int): Unit = ???

  override def glPushClientAttrib(i: Int): Unit = ???

  override def glPopClientAttrib(): Unit = ???

  override def glIndexPointer(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glEdgeFlagPointer(i: Int, buffer: Buffer): Unit = ???

  override def glEdgeFlagPointer(i: Int, l: Long): Unit = ???

  override def glArrayElement(i: Int): Unit = ???

  override def glInterleavedArrays(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glInterleavedArrays(i: Int, i1: Int, l: Long): Unit = ???

  override def glPrioritizeTextures(i: Int, intBuffer: IntBuffer, floatBuffer: FloatBuffer): Unit = ???

  override def glPrioritizeTextures(i: Int, ints: Array[Int], i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glAreTexturesResident(i: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Boolean = ???

  override def glAreTexturesResident(i: Int, ints: Array[Int], i1: Int, bytes: Array[Byte], i2: Int): Boolean = ???

  override def glMultiTexCoord1d(i: Int, v: Double): Unit = ???

  override def glMultiTexCoord1dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultiTexCoord1dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMultiTexCoord1f(i: Int, v: Float): Unit = ???

  override def glMultiTexCoord1fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexCoord1fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMultiTexCoord1i(i: Int, i1: Int): Unit = ???

  override def glMultiTexCoord1iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexCoord1iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMultiTexCoord1s(i: Int, i1: Short): Unit = ???

  override def glMultiTexCoord1sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord1sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord2d(i: Int, v: Double, v1: Double): Unit = ???

  override def glMultiTexCoord2dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultiTexCoord2dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMultiTexCoord2f(i: Int, v: Float, v1: Float): Unit = ???

  override def glMultiTexCoord2fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexCoord2fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMultiTexCoord2i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glMultiTexCoord2iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexCoord2iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMultiTexCoord2s(i: Int, i1: Short, i2: Short): Unit = ???

  override def glMultiTexCoord2sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord2sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord3d(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glMultiTexCoord3dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultiTexCoord3dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMultiTexCoord3f(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glMultiTexCoord3fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexCoord3fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMultiTexCoord3i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiTexCoord3iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexCoord3iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMultiTexCoord3s(i: Int, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glMultiTexCoord3sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord3sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord4d(i: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glMultiTexCoord4dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultiTexCoord4dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMultiTexCoord4fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexCoord4fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMultiTexCoord4i(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glMultiTexCoord4iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexCoord4iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMultiTexCoord4s(i: Int, i1: Short, i2: Short, i3: Short, i4: Short): Unit = ???

  override def glMultiTexCoord4sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord4sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glLoadTransposeMatrixf(floatBuffer: FloatBuffer): Unit = ???

  override def glLoadTransposeMatrixf(floats: Array[Float], i: Int): Unit = ???

  override def glLoadTransposeMatrixd(doubleBuffer: DoubleBuffer): Unit = ???

  override def glLoadTransposeMatrixd(doubles: Array[Double], i: Int): Unit = ???

  override def glMultTransposeMatrixf(floatBuffer: FloatBuffer): Unit = ???

  override def glMultTransposeMatrixf(floats: Array[Float], i: Int): Unit = ???

  override def glMultTransposeMatrixd(doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultTransposeMatrixd(doubles: Array[Double], i: Int): Unit = ???

  override def glFogCoordf(v: Float): Unit = ???

  override def glFogCoordfv(floatBuffer: FloatBuffer): Unit = ???

  override def glFogCoordfv(floats: Array[Float], i: Int): Unit = ???

  override def glFogCoordd(v: Double): Unit = ???

  override def glFogCoorddv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glFogCoorddv(doubles: Array[Double], i: Int): Unit = ???

  override def glFogCoordPointer(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glFogCoordPointer(i: Int, i1: Int, l: Long): Unit = ???

  override def glSecondaryColor3b(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glSecondaryColor3bv(byteBuffer: ByteBuffer): Unit = ???

  override def glSecondaryColor3bv(bytes: Array[Byte], i: Int): Unit = ???

  override def glSecondaryColor3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glSecondaryColor3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glSecondaryColor3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glSecondaryColor3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glSecondaryColor3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glSecondaryColor3fv(floats: Array[Float], i: Int): Unit = ???

  override def glSecondaryColor3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glSecondaryColor3iv(intBuffer: IntBuffer): Unit = ???

  override def glSecondaryColor3iv(ints: Array[Int], i: Int): Unit = ???

  override def glSecondaryColor3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glSecondaryColor3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glSecondaryColor3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glSecondaryColor3ub(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glSecondaryColor3ubv(byteBuffer: ByteBuffer): Unit = ???

  override def glSecondaryColor3ubv(bytes: Array[Byte], i: Int): Unit = ???

  override def glSecondaryColor3ui(i: Int, i1: Int, i2: Int): Unit = ???

  override def glSecondaryColor3uiv(intBuffer: IntBuffer): Unit = ???

  override def glSecondaryColor3uiv(ints: Array[Int], i: Int): Unit = ???

  override def glSecondaryColor3us(i: Short, i1: Short, i2: Short): Unit = ???

  override def glSecondaryColor3usv(shortBuffer: ShortBuffer): Unit = ???

  override def glSecondaryColor3usv(shorts: Array[Short], i: Int): Unit = ???

  override def glSecondaryColorPointer(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glSecondaryColorPointer(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glWindowPos2d(v: Double, v1: Double): Unit = ???

  override def glWindowPos2dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glWindowPos2dv(doubles: Array[Double], i: Int): Unit = ???

  override def glWindowPos2f(v: Float, v1: Float): Unit = ???

  override def glWindowPos2fv(floatBuffer: FloatBuffer): Unit = ???

  override def glWindowPos2fv(floats: Array[Float], i: Int): Unit = ???

  override def glWindowPos2i(i: Int, i1: Int): Unit = ???

  override def glWindowPos2iv(intBuffer: IntBuffer): Unit = ???

  override def glWindowPos2iv(ints: Array[Int], i: Int): Unit = ???

  override def glWindowPos2s(i: Short, i1: Short): Unit = ???

  override def glWindowPos2sv(shortBuffer: ShortBuffer): Unit = ???

  override def glWindowPos2sv(shorts: Array[Short], i: Int): Unit = ???

  override def glWindowPos3d(v: Double, v1: Double, v2: Double): Unit = ???

  override def glWindowPos3dv(doubleBuffer: DoubleBuffer): Unit = ???

  override def glWindowPos3dv(doubles: Array[Double], i: Int): Unit = ???

  override def glWindowPos3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glWindowPos3fv(floatBuffer: FloatBuffer): Unit = ???

  override def glWindowPos3fv(floats: Array[Float], i: Int): Unit = ???

  override def glWindowPos3i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glWindowPos3iv(intBuffer: IntBuffer): Unit = ???

  override def glWindowPos3iv(ints: Array[Int], i: Int): Unit = ???

  override def glWindowPos3s(i: Short, i1: Short, i2: Short): Unit = ???

  override def glWindowPos3sv(shortBuffer: ShortBuffer): Unit = ???

  override def glWindowPos3sv(shorts: Array[Short], i: Int): Unit = ???

  override def glClearNamedBufferData(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glClearNamedBufferSubData(i: Int, i1: Int, l: Long, l1: Long, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glNamedFramebufferParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glGetNamedFramebufferParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedFramebufferParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetnMapdv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetnMapdv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glGetnMapfv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetnMapfv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetnMapiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetnMapiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetnPixelMapfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetnPixelMapfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetnPixelMapuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetnPixelMapuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetnPixelMapusv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glGetnPixelMapusv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glGetnPolygonStipple(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetnPolygonStipple(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glGetnColorTable(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetnConvolutionFilter(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetnSeparableFilter(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer, i4: Int, buffer1: Buffer, buffer2: Buffer): Unit = ???

  override def glGetnHistogram(i: Int, b: Boolean, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetnMinmax(i: Int, b: Boolean, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glProgramStringARB(i: Int, i1: Int, i2: Int, s: String): Unit = ???

  override def glBindProgramARB(i: Int, i1: Int): Unit = ???

  override def glDeleteProgramsARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteProgramsARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenProgramsARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenProgramsARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glProgramEnvParameter4dARB(i: Int, i1: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glProgramEnvParameter4dvARB(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramEnvParameter4dvARB(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glProgramEnvParameter4fARB(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glProgramEnvParameter4fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramEnvParameter4fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glProgramLocalParameter4dARB(i: Int, i1: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glProgramLocalParameter4dvARB(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramLocalParameter4dvARB(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glProgramLocalParameter4fARB(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glProgramLocalParameter4fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramLocalParameter4fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetProgramEnvParameterdvARB(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetProgramEnvParameterdvARB(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetProgramEnvParameterfvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetProgramEnvParameterfvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetProgramLocalParameterdvARB(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetProgramLocalParameterdvARB(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetProgramLocalParameterfvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetProgramLocalParameterfvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetProgramivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramStringARB(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glIsProgramARB(i: Int): Boolean = ???

  override def glColorTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glColorTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glColorTableParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glColorTableParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glColorTableParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glColorTableParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glCopyColorTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glGetColorTable(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGetColorTable(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glGetColorTableParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetColorTableParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetColorTableParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetColorTableParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glColorSubTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glColorSubTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glCopyColorSubTable(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glConvolutionFilter1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glConvolutionFilter1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glConvolutionFilter2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer): Unit = ???

  override def glConvolutionFilter2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glConvolutionParameterf(i: Int, i1: Int, v: Float): Unit = ???

  override def glConvolutionParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glConvolutionParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glConvolutionParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glConvolutionParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glConvolutionParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glCopyConvolutionFilter1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glCopyConvolutionFilter2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glGetConvolutionFilter(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGetConvolutionFilter(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glGetConvolutionParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetConvolutionParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetConvolutionParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetConvolutionParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetSeparableFilter(i: Int, i1: Int, i2: Int, buffer: Buffer, buffer1: Buffer, buffer2: Buffer): Unit = ???

  override def glGetSeparableFilter(i: Int, i1: Int, i2: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glSeparableFilter2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer, buffer1: Buffer): Unit = ???

  override def glSeparableFilter2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long, l1: Long): Unit = ???

  override def glGetHistogram(i: Int, b: Boolean, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGetHistogram(i: Int, b: Boolean, i1: Int, i2: Int, l: Long): Unit = ???

  override def glGetHistogramParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetHistogramParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetHistogramParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetHistogramParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetMinmax(i: Int, b: Boolean, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGetMinmax(i: Int, b: Boolean, i1: Int, i2: Int, l: Long): Unit = ???

  override def glGetMinmaxParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMinmaxParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetMinmaxParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMinmaxParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glHistogram(i: Int, i1: Int, i2: Int, b: Boolean): Unit = ???

  override def glMinmax(i: Int, i1: Int, b: Boolean): Unit = ???

  override def glResetHistogram(i: Int): Unit = ???

  override def glResetMinmax(i: Int): Unit = ???

  override def glCurrentPaletteMatrixARB(i: Int): Unit = ???

  override def glMatrixIndexubvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glMatrixIndexubvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glMatrixIndexusvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMatrixIndexusvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMatrixIndexuivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glMatrixIndexuivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMatrixIndexPointerARB(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glMatrixIndexPointerARB(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glDeleteObjectARB(l: Long): Unit = ???

  override def glGetHandleARB(i: Int): Long = ???

  override def glDetachObjectARB(l: Long, l1: Long): Unit = ???

  override def glCreateShaderObjectARB(i: Int): Long = ???

  override def glShaderSourceARB(l: Long, i: Int, strings: Array[String], intBuffer: IntBuffer): Unit = ???

  override def glShaderSourceARB(l: Long, i: Int, strings: Array[String], ints: Array[Int], i1: Int): Unit = ???

  override def glCompileShaderARB(l: Long): Unit = ???

  override def glCreateProgramObjectARB(): Long = ???

  override def glAttachObjectARB(l: Long, l1: Long): Unit = ???

  override def glLinkProgramARB(l: Long): Unit = ???

  override def glUseProgramObjectARB(l: Long): Unit = ???

  override def glValidateProgramARB(l: Long): Unit = ???

  override def glUniform1fARB(i: Int, v: Float): Unit = ???

  override def glUniform2fARB(i: Int, v: Float, v1: Float): Unit = ???

  override def glUniform3fARB(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glUniform4fARB(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glUniform1iARB(i: Int, i1: Int): Unit = ???

  override def glUniform2iARB(i: Int, i1: Int, i2: Int): Unit = ???

  override def glUniform3iARB(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glUniform4iARB(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glUniform1fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform1fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform2fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform2fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform3fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform3fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform4fvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform4fvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform1ivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform1ivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform2ivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform2ivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform3ivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform3ivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform4ivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform4ivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniformMatrix2fvARB(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix2fvARB(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix3fvARB(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix3fvARB(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix4fvARB(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix4fvARB(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glGetObjectParameterfvARB(l: Long, i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetObjectParameterfvARB(l: Long, i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetObjectParameterivARB(l: Long, i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetObjectParameterivARB(l: Long, i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetInfoLogARB(l: Long, i: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetInfoLogARB(l: Long, i: Int, ints: Array[Int], i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glGetAttachedObjectsARB(l: Long, i: Int, intBuffer: IntBuffer, longBuffer: LongBuffer): Unit = ???

  override def glGetAttachedObjectsARB(l: Long, i: Int, ints: Array[Int], i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetUniformLocationARB(l: Long, s: String): Int = ???

  override def glGetActiveUniformARB(l: Long, i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetActiveUniformARB(l: Long, i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int, ints2: Array[Int], i4: Int, bytes: Array[Byte], i5: Int): Unit = ???

  override def glGetUniformfvARB(l: Long, i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetUniformfvARB(l: Long, i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetUniformivARB(l: Long, i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetUniformivARB(l: Long, i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetShaderSourceARB(l: Long, i: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetShaderSourceARB(l: Long, i: Int, ints: Array[Int], i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glWeightbvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glWeightbvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glWeightsvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glWeightsvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glWeightivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glWeightivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glWeightfvARB(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glWeightfvARB(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glWeightdvARB(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glWeightdvARB(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glWeightubvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glWeightubvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glWeightusvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glWeightusvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glWeightuivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glWeightuivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glWeightPointerARB(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glWeightPointerARB(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glVertexBlendARB(i: Int): Unit = ???

  override def glVertexAttrib1dARB(i: Int, v: Double): Unit = ???

  override def glVertexAttrib1dvARB(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib1dvARB(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib1fARB(i: Int, v: Float): Unit = ???

  override def glVertexAttrib1fvARB(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib1fvARB(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib1sARB(i: Int, i1: Short): Unit = ???

  override def glVertexAttrib1svARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib1svARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib2dARB(i: Int, v: Double, v1: Double): Unit = ???

  override def glVertexAttrib2dvARB(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib2dvARB(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib2fARB(i: Int, v: Float, v1: Float): Unit = ???

  override def glVertexAttrib2fvARB(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib2fvARB(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib2sARB(i: Int, i1: Short, i2: Short): Unit = ???

  override def glVertexAttrib2svARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib2svARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib3dARB(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glVertexAttrib3dvARB(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib3dvARB(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib3fARB(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glVertexAttrib3fvARB(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib3fvARB(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib3sARB(i: Int, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glVertexAttrib3svARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib3svARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4NbvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4NbvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4NivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4NivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4NsvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4NsvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4NubARB(i: Int, b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glVertexAttrib4NubvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4NubvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4NuivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4NuivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4NusvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4NusvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4bvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4bvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4dARB(i: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glVertexAttrib4dvARB(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib4dvARB(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib4fARB(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glVertexAttrib4fvARB(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib4fvARB(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib4ivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4ivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4sARB(i: Int, i1: Short, i2: Short, i3: Short, i4: Short): Unit = ???

  override def glVertexAttrib4svARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4svARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4ubvARB(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4ubvARB(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4uivARB(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4uivARB(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4usvARB(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4usvARB(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttribPointerARB(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int, buffer: Buffer): Unit = ???

  override def glVertexAttribPointerARB(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int, l: Long): Unit = ???

  override def glEnableVertexAttribArrayARB(i: Int): Unit = ???

  override def glDisableVertexAttribArrayARB(i: Int): Unit = ???

  override def glGetVertexAttribdvARB(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetVertexAttribdvARB(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetVertexAttribfvARB(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetVertexAttribfvARB(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetVertexAttribivARB(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribivARB(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glBlendBarrier(): Unit = ???

  override def glMultiTexCoord1bOES(i: Int, b: Byte): Unit = ???

  override def glMultiTexCoord1bvOES(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glMultiTexCoord1bvOES(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glMultiTexCoord2bOES(i: Int, b: Byte, b1: Byte): Unit = ???

  override def glMultiTexCoord2bvOES(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glMultiTexCoord2bvOES(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glMultiTexCoord3bOES(i: Int, b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glMultiTexCoord3bvOES(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glMultiTexCoord3bvOES(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glMultiTexCoord4bOES(i: Int, b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glMultiTexCoord4bvOES(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glMultiTexCoord4bvOES(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glTexCoord1bOES(b: Byte): Unit = ???

  override def glTexCoord1bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glTexCoord1bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glTexCoord2bOES(b: Byte, b1: Byte): Unit = ???

  override def glTexCoord2bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glTexCoord2bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glTexCoord3bOES(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glTexCoord3bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glTexCoord3bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glTexCoord4bOES(b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glTexCoord4bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glTexCoord4bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glVertex2bOES(b: Byte, b1: Byte): Unit = ???

  override def glVertex2bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glVertex2bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glVertex3bOES(b: Byte, b1: Byte, b2: Byte): Unit = ???

  override def glVertex3bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glVertex3bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glVertex4bOES(b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glVertex4bvOES(byteBuffer: ByteBuffer): Unit = ???

  override def glVertex4bvOES(bytes: Array[Byte], i: Int): Unit = ???

  override def glQueryMatrixxOES(intBuffer: IntBuffer, intBuffer1: IntBuffer): Int = ???

  override def glQueryMatrixxOES(ints: Array[Int], i: Int, ints1: Array[Int], i1: Int): Int = ???

  override def glClipPlanef(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glClipPlanef(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetClipPlanef(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetClipPlanef(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glBlendFuncIndexedAMD(i: Int, i1: Int, i2: Int): Unit = ???

  override def glBlendFuncSeparateIndexedAMD(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glBlendEquationIndexedAMD(i: Int, i1: Int): Unit = ???

  override def glBlendEquationSeparateIndexedAMD(i: Int, i1: Int, i2: Int): Unit = ???

  override def glUniform1i64NV(i: Int, l: Long): Unit = ???

  override def glUniform2i64NV(i: Int, l: Long, l1: Long): Unit = ???

  override def glUniform3i64NV(i: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glUniform4i64NV(i: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glUniform1i64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform1i64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform2i64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform2i64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform3i64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform3i64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform4i64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform4i64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform1ui64NV(i: Int, l: Long): Unit = ???

  override def glUniform2ui64NV(i: Int, l: Long, l1: Long): Unit = ???

  override def glUniform3ui64NV(i: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glUniform4ui64NV(i: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glUniform1ui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform1ui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform2ui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform2ui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform3ui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform3ui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glUniform4ui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniform4ui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetUniformi64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetUniformi64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glProgramUniform1i64NV(i: Int, i1: Int, l: Long): Unit = ???

  override def glProgramUniform2i64NV(i: Int, i1: Int, l: Long, l1: Long): Unit = ???

  override def glProgramUniform3i64NV(i: Int, i1: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glProgramUniform4i64NV(i: Int, i1: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glProgramUniform1i64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform1i64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform2i64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform2i64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform3i64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform3i64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform4i64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform4i64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform1ui64NV(i: Int, i1: Int, l: Long): Unit = ???

  override def glProgramUniform2ui64NV(i: Int, i1: Int, l: Long, l1: Long): Unit = ???

  override def glProgramUniform3ui64NV(i: Int, i1: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glProgramUniform4ui64NV(i: Int, i1: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glProgramUniform1ui64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform1ui64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform2ui64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform2ui64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform3ui64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform3ui64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glProgramUniform4ui64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniform4ui64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glVertexAttribParameteriAMD(i: Int, i1: Int, i2: Int): Unit = ???

  override def glGenNamesAMD(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenNamesAMD(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glDeleteNamesAMD(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteNamesAMD(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glIsNameAMD(i: Int, i1: Int): Boolean = ???

  override def glQueryObjectParameteruiAMD(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glGetPerfMonitorGroupsAMD(intBuffer: IntBuffer, i: Int, intBuffer1: IntBuffer): Unit = ???

  override def glGetPerfMonitorGroupsAMD(ints: Array[Int], i: Int, i1: Int, ints1: Array[Int], i2: Int): Unit = ???

  override def glGetPerfMonitorCountersAMD(i: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, i1: Int, intBuffer2: IntBuffer): Unit = ???

  override def glGetPerfMonitorCountersAMD(i: Int, ints: Array[Int], i1: Int, ints1: Array[Int], i2: Int, i3: Int, ints2: Array[Int], i4: Int): Unit = ???

  override def glGetPerfMonitorGroupStringAMD(i: Int, i1: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetPerfMonitorGroupStringAMD(i: Int, i1: Int, ints: Array[Int], i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetPerfMonitorCounterStringAMD(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetPerfMonitorCounterStringAMD(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, bytes: Array[Byte], i4: Int): Unit = ???

  override def glGetPerfMonitorCounterInfoAMD(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGenPerfMonitorsAMD(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenPerfMonitorsAMD(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeletePerfMonitorsAMD(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeletePerfMonitorsAMD(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glSelectPerfMonitorCountersAMD(i: Int, b: Boolean, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glSelectPerfMonitorCountersAMD(i: Int, b: Boolean, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glBeginPerfMonitorAMD(i: Int): Unit = ???

  override def glEndPerfMonitorAMD(i: Int): Unit = ???

  override def glGetPerfMonitorCounterDataAMD(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer): Unit = ???

  override def glGetPerfMonitorCounterDataAMD(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int): Unit = ???

  override def glTexStorageSparseAMD(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glTextureStorageSparseAMD(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glBufferParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glObjectPurgeableAPPLE(i: Int, i1: Int, i2: Int): Int = ???

  override def glObjectUnpurgeableAPPLE(i: Int, i1: Int, i2: Int): Int = ???

  override def glGetObjectParameterivAPPLE(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetObjectParameterivAPPLE(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glTextureRangeAPPLE(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glVertexArrayRangeAPPLE(i: Int, buffer: Buffer): Unit = ???

  override def glFlushVertexArrayRangeAPPLE(i: Int, buffer: Buffer): Unit = ???

  override def glVertexArrayParameteriAPPLE(i: Int, i1: Int): Unit = ???

  override def glEnableVertexAttribAPPLE(i: Int, i1: Int): Unit = ???

  override def glDisableVertexAttribAPPLE(i: Int, i1: Int): Unit = ???

  override def glIsVertexAttribEnabledAPPLE(i: Int, i1: Int): Boolean = ???

  override def glMapVertexAttrib1dAPPLE(i: Int, i1: Int, v: Double, v1: Double, i2: Int, i3: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMapVertexAttrib1dAPPLE(i: Int, i1: Int, v: Double, v1: Double, i2: Int, i3: Int, doubles: Array[Double], i4: Int): Unit = ???

  override def glMapVertexAttrib1fAPPLE(i: Int, i1: Int, v: Float, v1: Float, i2: Int, i3: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMapVertexAttrib1fAPPLE(i: Int, i1: Int, v: Float, v1: Float, i2: Int, i3: Int, floats: Array[Float], i4: Int): Unit = ???

  override def glMapVertexAttrib2dAPPLE(i: Int, i1: Int, v: Double, v1: Double, i2: Int, i3: Int, v2: Double, v3: Double, i4: Int, i5: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMapVertexAttrib2dAPPLE(i: Int, i1: Int, v: Double, v1: Double, i2: Int, i3: Int, v2: Double, v3: Double, i4: Int, i5: Int, doubles: Array[Double], i6: Int): Unit = ???

  override def glMapVertexAttrib2fAPPLE(i: Int, i1: Int, v: Float, v1: Float, i2: Int, i3: Int, v2: Float, v3: Float, i4: Int, i5: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMapVertexAttrib2fAPPLE(i: Int, i1: Int, v: Float, v1: Float, i2: Int, i3: Int, v2: Float, v3: Float, i4: Int, i5: Int, floats: Array[Float], i6: Int): Unit = ???

  override def glDrawBuffersATI(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDrawBuffersATI(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glPNTrianglesiATI(i: Int, i1: Int): Unit = ???

  override def glPNTrianglesfATI(i: Int, v: Float): Unit = ???

  override def glUniformBufferEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glGetUniformBufferSizeEXT(i: Int, i1: Int): Int = ???

  override def glGetUniformOffsetEXT(i: Int, i1: Int): Long = ???

  override def glLockArraysEXT(i: Int, i1: Int): Unit = ???

  override def glUnlockArraysEXT(): Unit = ???

  override def glCullParameterdvEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glCullParameterdvEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glCullParameterfvEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glCullParameterfvEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glDepthBoundsEXT(v: Double, v1: Double): Unit = ???

  override def glMatrixLoadfEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMatrixLoadfEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMatrixLoaddEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMatrixLoaddEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMatrixMultfEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMatrixMultfEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMatrixMultdEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMatrixMultdEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMatrixLoadIdentityEXT(i: Int): Unit = ???

  override def glMatrixRotatefEXT(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glMatrixRotatedEXT(i: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glMatrixScalefEXT(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glMatrixScaledEXT(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glMatrixTranslatefEXT(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glMatrixTranslatedEXT(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glMatrixFrustumEXT(i: Int, v: Double, v1: Double, v2: Double, v3: Double, v4: Double, v5: Double): Unit = ???

  override def glMatrixOrthoEXT(i: Int, v: Double, v1: Double, v2: Double, v3: Double, v4: Double, v5: Double): Unit = ???

  override def glMatrixPopEXT(i: Int): Unit = ???

  override def glMatrixPushEXT(i: Int): Unit = ???

  override def glClientAttribDefaultEXT(i: Int): Unit = ???

  override def glPushClientAttribDefaultEXT(i: Int): Unit = ???

  override def glTextureParameterfEXT(i: Int, i1: Int, i2: Int, v: Float): Unit = ???

  override def glTextureParameterfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glTextureParameterfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glTextureParameteriEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTextureParameterivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glTextureParameterivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glTextureImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glTextureImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, l: Long): Unit = ???

  override def glTextureImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glTextureImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, l: Long): Unit = ???

  override def glTextureSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glTextureSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, l: Long): Unit = ???

  override def glTextureSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glTextureSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, l: Long): Unit = ???

  override def glCopyTextureImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glCopyTextureImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int): Unit = ???

  override def glCopyTextureSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glCopyTextureSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int): Unit = ???

  override def glGetTextureImageEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glGetTextureParameterfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTextureParameterfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetTextureParameterivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTextureParameterivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetTextureLevelParameterfvEXT(i: Int, i1: Int, i2: Int, i3: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTextureLevelParameterfvEXT(i: Int, i1: Int, i2: Int, i3: Int, floats: Array[Float], i4: Int): Unit = ???

  override def glGetTextureLevelParameterivEXT(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTextureLevelParameterivEXT(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glTextureImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, buffer: Buffer): Unit = ???

  override def glTextureImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, l: Long): Unit = ???

  override def glTextureSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, buffer: Buffer): Unit = ???

  override def glTextureSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, l: Long): Unit = ???

  override def glCopyTextureSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int): Unit = ???

  override def glBindMultiTextureEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glMultiTexCoordPointerEXT(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glMultiTexEnvfEXT(i: Int, i1: Int, i2: Int, v: Float): Unit = ???

  override def glMultiTexEnvfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexEnvfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glMultiTexEnviEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiTexEnvivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexEnvivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glMultiTexGendEXT(i: Int, i1: Int, i2: Int, v: Double): Unit = ???

  override def glMultiTexGendvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMultiTexGendvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glMultiTexGenfEXT(i: Int, i1: Int, i2: Int, v: Float): Unit = ???

  override def glMultiTexGenfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexGenfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glMultiTexGeniEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiTexGenivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexGenivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMultiTexEnvfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultiTexEnvfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetMultiTexEnvivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexEnvivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMultiTexGendvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetMultiTexGendvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glGetMultiTexGenfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultiTexGenfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetMultiTexGenivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexGenivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glMultiTexParameteriEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiTexParameterivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexParameterivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glMultiTexParameterfEXT(i: Int, i1: Int, i2: Int, v: Float): Unit = ???

  override def glMultiTexParameterfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMultiTexParameterfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glMultiTexImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glMultiTexImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glMultiTexSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glMultiTexSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glCopyMultiTexImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glCopyMultiTexImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int): Unit = ???

  override def glCopyMultiTexSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glCopyMultiTexSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int): Unit = ???

  override def glGetMultiTexImageEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glGetMultiTexParameterfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultiTexParameterfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetMultiTexParameterivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexParameterivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMultiTexLevelParameterfvEXT(i: Int, i1: Int, i2: Int, i3: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultiTexLevelParameterfvEXT(i: Int, i1: Int, i2: Int, i3: Int, floats: Array[Float], i4: Int): Unit = ???

  override def glGetMultiTexLevelParameterivEXT(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexLevelParameterivEXT(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glMultiTexImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, buffer: Buffer): Unit = ???

  override def glMultiTexSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, buffer: Buffer): Unit = ???

  override def glCopyMultiTexSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int): Unit = ???

  override def glEnableClientStateIndexedEXT(i: Int, i1: Int): Unit = ???

  override def glDisableClientStateIndexedEXT(i: Int, i1: Int): Unit = ???

  override def glGetFloatIndexedvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetFloatIndexedvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetDoubleIndexedvEXT(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetDoubleIndexedvEXT(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glEnableIndexed(i: Int, i1: Int): Unit = ???

  override def glDisableIndexed(i: Int, i1: Int): Unit = ???

  override def glIsEnabledIndexed(i: Int, i1: Int): Boolean = ???

  override def glGetIntegerIndexedv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetIntegerIndexedv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetBooleanIndexedv(i: Int, i1: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetBooleanIndexedv(i: Int, i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glCompressedTextureImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glCompressedTextureImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glCompressedTextureImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glCompressedTextureSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, buffer: Buffer): Unit = ???

  override def glCompressedTextureSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glCompressedTextureSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glGetCompressedTextureImageEXT(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexSubImage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexSubImage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glCompressedMultiTexSubImage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glGetCompressedMultiTexImageEXT(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glMatrixLoadTransposefEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMatrixLoadTransposefEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMatrixLoadTransposedEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMatrixLoadTransposedEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glMatrixMultTransposefEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMatrixMultTransposefEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glMatrixMultTransposedEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glMatrixMultTransposedEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glNamedBufferDataEXT(i: Int, l: Long, buffer: Buffer, i1: Int): Unit = ???

  override def glNamedBufferSubDataEXT(i: Int, l: Long, l1: Long, buffer: Buffer): Unit = ???

  override def glMapNamedBufferEXT(i: Int, i1: Int): ByteBuffer = ???

  override def glUnmapNamedBufferEXT(i: Int): Boolean = ???

  override def glGetNamedBufferParameterivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedBufferParameterivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetNamedBufferSubDataEXT(i: Int, l: Long, l1: Long, buffer: Buffer): Unit = ???

  override def glTextureBufferEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiTexBufferEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTextureParameterIivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glTextureParameterIivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glTextureParameterIuivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glTextureParameterIuivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetTextureParameterIivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTextureParameterIivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetTextureParameterIuivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTextureParameterIuivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glMultiTexParameterIivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexParameterIivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glMultiTexParameterIuivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glMultiTexParameterIuivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMultiTexParameterIivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexParameterIivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMultiTexParameterIuivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMultiTexParameterIuivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glNamedProgramLocalParameters4fvEXT(i: Int, i1: Int, i2: Int, i3: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glNamedProgramLocalParameters4fvEXT(i: Int, i1: Int, i2: Int, i3: Int, floats: Array[Float], i4: Int): Unit = ???

  override def glNamedProgramLocalParameterI4iEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glNamedProgramLocalParameterI4ivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glNamedProgramLocalParameterI4ivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glNamedProgramLocalParametersI4ivEXT(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glNamedProgramLocalParametersI4ivEXT(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glNamedProgramLocalParameterI4uiEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glNamedProgramLocalParameterI4uivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glNamedProgramLocalParameterI4uivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glNamedProgramLocalParametersI4uivEXT(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glNamedProgramLocalParametersI4uivEXT(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glGetNamedProgramLocalParameterIivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedProgramLocalParameterIivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetNamedProgramLocalParameterIuivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedProgramLocalParameterIuivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glEnableClientStateiEXT(i: Int, i1: Int): Unit = ???

  override def glDisableClientStateiEXT(i: Int, i1: Int): Unit = ???

  override def glGetFloati_vEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetFloati_vEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetDoublei_vEXT(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetDoublei_vEXT(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetPointeri_vEXT(i: Int, i1: Int, pointerBuffer: PointerBuffer): Unit = ???

  override def glNamedProgramStringEXT(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glNamedProgramLocalParameter4dEXT(i: Int, i1: Int, i2: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glNamedProgramLocalParameter4dvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glNamedProgramLocalParameter4dvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glNamedProgramLocalParameter4fEXT(i: Int, i1: Int, i2: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glNamedProgramLocalParameter4fvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glNamedProgramLocalParameter4fvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetNamedProgramLocalParameterdvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetNamedProgramLocalParameterdvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glGetNamedProgramLocalParameterfvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetNamedProgramLocalParameterfvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetNamedProgramivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedProgramivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetNamedProgramStringEXT(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glNamedRenderbufferStorageEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glGetNamedRenderbufferParameterivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedRenderbufferParameterivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glNamedRenderbufferStorageMultisampleEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glNamedRenderbufferStorageMultisampleCoverageEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glCheckNamedFramebufferStatusEXT(i: Int, i1: Int): Int = ???

  override def glNamedFramebufferTexture1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glNamedFramebufferTexture2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glNamedFramebufferTexture3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glNamedFramebufferRenderbufferEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glGetNamedFramebufferAttachmentParameterivEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNamedFramebufferAttachmentParameterivEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGenerateTextureMipmapEXT(i: Int, i1: Int): Unit = ???

  override def glGenerateMultiTexMipmapEXT(i: Int, i1: Int): Unit = ???

  override def glFramebufferDrawBufferEXT(i: Int, i1: Int): Unit = ???

  override def glFramebufferDrawBuffersEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glFramebufferDrawBuffersEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glFramebufferReadBufferEXT(i: Int, i1: Int): Unit = ???

  override def glGetFramebufferParameterivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetFramebufferParameterivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glNamedCopyBufferSubDataEXT(i: Int, i1: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glNamedFramebufferTextureEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glNamedFramebufferTextureLayerEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glNamedFramebufferTextureFaceEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glTextureRenderbufferEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glMultiTexRenderbufferEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexArrayVertexOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glVertexArrayColorOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glVertexArrayEdgeFlagOffsetEXT(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glVertexArrayIndexOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glVertexArrayNormalOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glVertexArrayTexCoordOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glVertexArrayMultiTexCoordOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glVertexArrayFogCoordOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glVertexArraySecondaryColorOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glVertexArrayVertexAttribOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, b: Boolean, i5: Int, l: Long): Unit = ???

  override def glVertexArrayVertexAttribIOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glEnableVertexArrayEXT(i: Int, i1: Int): Unit = ???

  override def glDisableVertexArrayEXT(i: Int, i1: Int): Unit = ???

  override def glEnableVertexArrayAttribEXT(i: Int, i1: Int): Unit = ???

  override def glDisableVertexArrayAttribEXT(i: Int, i1: Int): Unit = ???

  override def glGetVertexArrayIntegervEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexArrayIntegervEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetVertexArrayPointervEXT(i: Int, i1: Int, pointerBuffer: PointerBuffer): Unit = ???

  override def glGetVertexArrayIntegeri_vEXT(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexArrayIntegeri_vEXT(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetVertexArrayPointeri_vEXT(i: Int, i1: Int, i2: Int, pointerBuffer: PointerBuffer): Unit = ???

  override def glMapNamedBufferRangeEXT(i: Int, l: Long, l1: Long, i1: Int): ByteBuffer = ???

  override def glFlushMappedNamedBufferRangeEXT(i: Int, l: Long, l1: Long): Unit = ???

  override def glNamedBufferStorageEXT(i: Int, l: Long, buffer: Buffer, i1: Int): Unit = ???

  override def glProgramUniform1dEXT(i: Int, i1: Int, v: Double): Unit = ???

  override def glProgramUniform2dEXT(i: Int, i1: Int, v: Double, v1: Double): Unit = ???

  override def glProgramUniform3dEXT(i: Int, i1: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glProgramUniform4dEXT(i: Int, i1: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glProgramUniform1dvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform1dvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform2dvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform2dvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform3dvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform3dvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform4dvEXT(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform4dvEXT(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2x3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2x4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3x2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3x4dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4x2dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4x3dvEXT(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glTextureBufferRangeEXT(i: Int, i1: Int, i2: Int, i3: Int, l: Long, l1: Long): Unit = ???

  override def glTextureStorage2DMultisampleEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, b: Boolean): Unit = ???

  override def glTextureStorage3DMultisampleEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, b: Boolean): Unit = ???

  override def glVertexArrayBindVertexBufferEXT(i: Int, i1: Int, i2: Int, l: Long, i3: Int): Unit = ???

  override def glVertexArrayVertexAttribFormatEXT(i: Int, i1: Int, i2: Int, i3: Int, b: Boolean, i4: Int): Unit = ???

  override def glVertexArrayVertexAttribIFormatEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexArrayVertexAttribLFormatEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexArrayVertexAttribBindingEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexArrayVertexBindingDivisorEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexArrayVertexAttribLOffsetEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glTexturePageCommitmentEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, b: Boolean): Unit = ???

  override def glVertexArrayVertexAttribDivisorEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glColorMaskIndexed(i: Int, b: Boolean, b1: Boolean, b2: Boolean, b3: Boolean): Unit = ???

  override def glProgramEnvParameters4fvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramEnvParameters4fvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramLocalParameters4fvEXT(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramLocalParameters4fvEXT(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glIndexFuncEXT(i: Int, v: Float): Unit = ???

  override def glIndexMaterialEXT(i: Int, i1: Int): Unit = ???

  override def glApplyTextureEXT(i: Int): Unit = ???

  override def glTextureLightEXT(i: Int): Unit = ???

  override def glTextureMaterialEXT(i: Int, i1: Int): Unit = ???

  override def glPixelTransformParameteriEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glPixelTransformParameterfEXT(i: Int, i1: Int, v: Float): Unit = ???

  override def glPixelTransformParameterivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glPixelTransformParameterivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glPixelTransformParameterfvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glPixelTransformParameterfvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetPixelTransformParameterivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetPixelTransformParameterivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetPixelTransformParameterfvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetPixelTransformParameterfvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glPolygonOffsetClampEXT(v: Float, v1: Float, v2: Float): Unit = ???

  override def glProvokingVertexEXT(i: Int): Unit = ???

  override def glRasterSamplesEXT(i: Int, b: Boolean): Unit = ???

  override def glStencilClearTagEXT(i: Int, i1: Int): Unit = ???

  override def glActiveStencilFaceEXT(i: Int): Unit = ???

  override def glClearColorIi(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glClearColorIui(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTextureNormalEXT(i: Int): Unit = ???

  override def glGetQueryObjecti64vEXT(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetQueryObjecti64vEXT(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetQueryObjectui64vEXT(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetQueryObjectui64vEXT(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glBeginVertexShaderEXT(): Unit = ???

  override def glEndVertexShaderEXT(): Unit = ???

  override def glBindVertexShaderEXT(i: Int): Unit = ???

  override def glGenVertexShadersEXT(i: Int): Int = ???

  override def glDeleteVertexShaderEXT(i: Int): Unit = ???

  override def glShaderOp1EXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glShaderOp2EXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glShaderOp3EXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glSwizzleEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glWriteMaskEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glInsertComponentEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glExtractComponentEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glGenSymbolsEXT(i: Int, i1: Int, i2: Int, i3: Int): Int = ???

  override def glSetInvariantEXT(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glSetLocalConstantEXT(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glVariantbvEXT(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVariantbvEXT(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVariantsvEXT(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVariantsvEXT(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVariantivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVariantivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVariantfvEXT(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVariantfvEXT(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVariantdvEXT(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVariantdvEXT(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVariantubvEXT(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVariantubvEXT(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVariantusvEXT(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVariantusvEXT(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVariantuivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVariantuivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVariantPointerEXT(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glVariantPointerEXT(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glEnableVariantClientStateEXT(i: Int): Unit = ???

  override def glDisableVariantClientStateEXT(i: Int): Unit = ???

  override def glBindLightParameterEXT(i: Int, i1: Int): Int = ???

  override def glBindMaterialParameterEXT(i: Int, i1: Int): Int = ???

  override def glBindTexGenParameterEXT(i: Int, i1: Int, i2: Int): Int = ???

  override def glBindTextureUnitParameterEXT(i: Int, i1: Int): Int = ???

  override def glBindParameterEXT(i: Int): Int = ???

  override def glIsVariantEnabledEXT(i: Int, i1: Int): Boolean = ???

  override def glGetVariantBooleanvEXT(i: Int, i1: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetVariantBooleanvEXT(i: Int, i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glGetVariantIntegervEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVariantIntegervEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetVariantFloatvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetVariantFloatvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetInvariantBooleanvEXT(i: Int, i1: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetInvariantBooleanvEXT(i: Int, i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glGetInvariantIntegervEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetInvariantIntegervEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetInvariantFloatvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetInvariantFloatvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetLocalConstantBooleanvEXT(i: Int, i1: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetLocalConstantBooleanvEXT(i: Int, i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glGetLocalConstantIntegervEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetLocalConstantIntegervEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetLocalConstantFloatvEXT(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetLocalConstantFloatvEXT(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glVertexWeightfEXT(v: Float): Unit = ???

  override def glVertexWeightfvEXT(floatBuffer: FloatBuffer): Unit = ???

  override def glVertexWeightfvEXT(floats: Array[Float], i: Int): Unit = ???

  override def glVertexWeightPointerEXT(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glVertexWeightPointerEXT(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glFrameTerminatorGREMEDY(): Unit = ???

  override def glStringMarkerGREMEDY(i: Int, buffer: Buffer): Unit = ???

  override def glSyncTextureINTEL(i: Int): Unit = ???

  override def glUnmapTexture2DINTEL(i: Int, i1: Int): Unit = ???

  override def glMapTexture2DINTEL(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer): ByteBuffer = ???

  override def glMapTexture2DINTEL(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int): ByteBuffer = ???

  override def glBeginPerfQueryINTEL(i: Int): Unit = ???

  override def glCreatePerfQueryINTEL(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glCreatePerfQueryINTEL(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeletePerfQueryINTEL(i: Int): Unit = ???

  override def glEndPerfQueryINTEL(i: Int): Unit = ???

  override def glGetFirstPerfQueryIdINTEL(intBuffer: IntBuffer): Unit = ???

  override def glGetFirstPerfQueryIdINTEL(ints: Array[Int], i: Int): Unit = ???

  override def glGetNextPerfQueryIdINTEL(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetNextPerfQueryIdINTEL(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetPerfCounterInfoINTEL(i: Int, i1: Int, i2: Int, byteBuffer: ByteBuffer, i3: Int, byteBuffer1: ByteBuffer, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, intBuffer3: IntBuffer, longBuffer: LongBuffer): Unit = ???

  override def glGetPerfCounterInfoINTEL(i: Int, i1: Int, i2: Int, bytes: Array[Byte], i3: Int, i4: Int, bytes1: Array[Byte], i5: Int, ints: Array[Int], i6: Int, ints1: Array[Int], i7: Int, ints2: Array[Int], i8: Int, ints3: Array[Int], i9: Int, longs: Array[Long], i10: Int): Unit = ???

  override def glGetPerfQueryDataINTEL(i: Int, i1: Int, i2: Int, buffer: Buffer, intBuffer: IntBuffer): Unit = ???

  override def glGetPerfQueryDataINTEL(i: Int, i1: Int, i2: Int, buffer: Buffer, ints: Array[Int], i3: Int): Unit = ???

  override def glGetPerfQueryIdByNameINTEL(byteBuffer: ByteBuffer, intBuffer: IntBuffer): Unit = ???

  override def glGetPerfQueryIdByNameINTEL(bytes: Array[Byte], i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetPerfQueryInfoINTEL(i: Int, i1: Int, byteBuffer: ByteBuffer, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, intBuffer3: IntBuffer): Unit = ???

  override def glGetPerfQueryInfoINTEL(i: Int, i1: Int, bytes: Array[Byte], i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int, ints2: Array[Int], i5: Int, ints3: Array[Int], i6: Int): Unit = ???

  override def glBeginConditionalRenderNVX(i: Int): Unit = ???

  override def glEndConditionalRenderNVX(): Unit = ???

  override def glMultiDrawArraysIndirectBindlessNV(i: Int, buffer: Buffer, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glMultiDrawElementsIndirectBindlessNV(i: Int, i1: Int, buffer: Buffer, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glMultiDrawArraysIndirectBindlessCountNV(i: Int, buffer: Buffer, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glMultiDrawElementsIndirectBindlessCountNV(i: Int, i1: Int, buffer: Buffer, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glCreateStatesNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glCreateStatesNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteStatesNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteStatesNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsStateNV(i: Int): Boolean = ???

  override def glStateCaptureNV(i: Int, i1: Int): Unit = ???

  override def glGetCommandHeaderNV(i: Int, i1: Int): Int = ???

  override def glGetStageIndexNV(i: Int): Short = ???

  override def glDrawCommandsNV(i: Int, i1: Int, pointerBuffer: PointerBuffer, intBuffer: IntBuffer, i2: Int): Unit = ???

  override def glDrawCommandsNV(i: Int, i1: Int, pointerBuffer: PointerBuffer, ints: Array[Int], i2: Int, i3: Int): Unit = ???

  override def glDrawCommandsAddressNV(i: Int, longBuffer: LongBuffer, intBuffer: IntBuffer, i1: Int): Unit = ???

  override def glDrawCommandsAddressNV(i: Int, longs: Array[Long], i1: Int, ints: Array[Int], i2: Int, i3: Int): Unit = ???

  override def glDrawCommandsStatesNV(i: Int, pointerBuffer: PointerBuffer, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, i1: Int): Unit = ???

  override def glDrawCommandsStatesNV(i: Int, pointerBuffer: PointerBuffer, ints: Array[Int], i1: Int, ints1: Array[Int], i2: Int, ints2: Array[Int], i3: Int, i4: Int): Unit = ???

  override def glDrawCommandsStatesAddressNV(longBuffer: LongBuffer, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, i: Int): Unit = ???

  override def glDrawCommandsStatesAddressNV(longs: Array[Long], i: Int, ints: Array[Int], i1: Int, ints1: Array[Int], i2: Int, ints2: Array[Int], i3: Int, i4: Int): Unit = ???

  override def glCreateCommandListsNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glCreateCommandListsNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteCommandListsNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteCommandListsNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsCommandListNV(i: Int): Boolean = ???

  override def glListDrawCommandsStatesClientNV(i: Int, i1: Int, pointerBuffer: PointerBuffer, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, i2: Int): Unit = ???

  override def glListDrawCommandsStatesClientNV(i: Int, i1: Int, pointerBuffer: PointerBuffer, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int, ints2: Array[Int], i4: Int, i5: Int): Unit = ???

  override def glCommandListSegmentsNV(i: Int, i1: Int): Unit = ???

  override def glCompileCommandListNV(i: Int): Unit = ???

  override def glCallCommandListNV(i: Int): Unit = ???

  override def glSubpixelPrecisionBiasNV(i: Int, i1: Int): Unit = ???

  override def glConservativeRasterParameterfNV(i: Int, v: Float): Unit = ???

  override def glCopyImageSubDataNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, i11: Int, i12: Int, i13: Int, i14: Int): Unit = ???

  override def glDrawTextureNV(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float, v4: Float, v5: Float, v6: Float, v7: Float, v8: Float): Unit = ???

  override def glMapControlPointsNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, b: Boolean, buffer: Buffer): Unit = ???

  override def glMapParameterivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glMapParameterivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glMapParameterfvNV(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMapParameterfvNV(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetMapControlPointsNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, b: Boolean, buffer: Buffer): Unit = ???

  override def glGetMapParameterivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMapParameterivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetMapParameterfvNV(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMapParameterfvNV(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetMapAttribParameterivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetMapAttribParameterivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetMapAttribParameterfvNV(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMapAttribParameterfvNV(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glEvalMapsNV(i: Int, i1: Int): Unit = ???

  override def glGetMultisamplefvNV(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultisamplefvNV(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glSampleMaskIndexedNV(i: Int, i1: Int): Unit = ???

  override def glTexRenderbufferNV(i: Int, i1: Int): Unit = ???

  override def glFragmentCoverageColorNV(i: Int): Unit = ???

  override def glCoverageModulationTableNV(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glCoverageModulationTableNV(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetCoverageModulationTableNV(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetCoverageModulationTableNV(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glCoverageModulationNV(i: Int): Unit = ???

  override def glRenderbufferStorageMultisampleCoverageNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramVertexLimitNV(i: Int, i1: Int): Unit = ???

  override def glFramebufferTextureFaceEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glProgramLocalParameterI4iNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramLocalParameterI4ivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramLocalParameterI4ivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramLocalParametersI4ivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramLocalParametersI4ivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramLocalParameterI4uiNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramLocalParameterI4uivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramLocalParameterI4uivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramLocalParametersI4uivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramLocalParametersI4uivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramEnvParameterI4iNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramEnvParameterI4ivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramEnvParameterI4ivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramEnvParametersI4ivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramEnvParametersI4ivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramEnvParameterI4uiNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramEnvParameterI4uivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramEnvParameterI4uivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramEnvParametersI4uivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramEnvParametersI4uivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetProgramLocalParameterIivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramLocalParameterIivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramLocalParameterIuivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramLocalParameterIuivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramEnvParameterIivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramEnvParameterIivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramEnvParameterIuivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramEnvParameterIuivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramSubroutineParametersuivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramSubroutineParametersuivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramSubroutineParameteruivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramSubroutineParameteruivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glVertex2h(i: Short, i1: Short): Unit = ???

  override def glVertex2hv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex2hv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertex3h(i: Short, i1: Short, i2: Short): Unit = ???

  override def glVertex3hv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex3hv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertex4h(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glVertex4hv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertex4hv(shorts: Array[Short], i: Int): Unit = ???

  override def glNormal3h(i: Short, i1: Short, i2: Short): Unit = ???

  override def glNormal3hv(shortBuffer: ShortBuffer): Unit = ???

  override def glNormal3hv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor3h(i: Short, i1: Short, i2: Short): Unit = ???

  override def glColor3hv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor3hv(shorts: Array[Short], i: Int): Unit = ???

  override def glColor4h(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glColor4hv(shortBuffer: ShortBuffer): Unit = ???

  override def glColor4hv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord1h(i: Short): Unit = ???

  override def glTexCoord1hv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord1hv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord2h(i: Short, i1: Short): Unit = ???

  override def glTexCoord2hv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord2hv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord3h(i: Short, i1: Short, i2: Short): Unit = ???

  override def glTexCoord3hv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord3hv(shorts: Array[Short], i: Int): Unit = ???

  override def glTexCoord4h(i: Short, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glTexCoord4hv(shortBuffer: ShortBuffer): Unit = ???

  override def glTexCoord4hv(shorts: Array[Short], i: Int): Unit = ???

  override def glMultiTexCoord1h(i: Int, i1: Short): Unit = ???

  override def glMultiTexCoord1hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord1hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord2h(i: Int, i1: Short, i2: Short): Unit = ???

  override def glMultiTexCoord2hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord2hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord3h(i: Int, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glMultiTexCoord3hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord3hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glMultiTexCoord4h(i: Int, i1: Short, i2: Short, i3: Short, i4: Short): Unit = ???

  override def glMultiTexCoord4hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glMultiTexCoord4hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glFogCoordh(i: Short): Unit = ???

  override def glFogCoordhv(shortBuffer: ShortBuffer): Unit = ???

  override def glFogCoordhv(shorts: Array[Short], i: Int): Unit = ???

  override def glSecondaryColor3h(i: Short, i1: Short, i2: Short): Unit = ???

  override def glSecondaryColor3hv(shortBuffer: ShortBuffer): Unit = ???

  override def glSecondaryColor3hv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertexWeighth(i: Short): Unit = ???

  override def glVertexWeighthv(shortBuffer: ShortBuffer): Unit = ???

  override def glVertexWeighthv(shorts: Array[Short], i: Int): Unit = ???

  override def glVertexAttrib1h(i: Int, i1: Short): Unit = ???

  override def glVertexAttrib1hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib1hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib2h(i: Int, i1: Short, i2: Short): Unit = ???

  override def glVertexAttrib2hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib2hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib3h(i: Int, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glVertexAttrib3hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib3hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4h(i: Int, i1: Short, i2: Short, i3: Short, i4: Short): Unit = ???

  override def glVertexAttrib4hv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4hv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttribs1hv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribs1hv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glVertexAttribs2hv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribs2hv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glVertexAttribs3hv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribs3hv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glVertexAttribs4hv(i: Int, i1: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribs4hv(i: Int, i1: Int, shorts: Array[Short], i2: Int): Unit = ???

  override def glGenOcclusionQueriesNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenOcclusionQueriesNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteOcclusionQueriesNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteOcclusionQueriesNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsOcclusionQueryNV(i: Int): Boolean = ???

  override def glBeginOcclusionQueryNV(i: Int): Unit = ???

  override def glEndOcclusionQueryNV(): Unit = ???

  override def glGetOcclusionQueryivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetOcclusionQueryivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetOcclusionQueryuivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetOcclusionQueryuivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glProgramBufferParametersfvNV(i: Int, i1: Int, i2: Int, i3: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramBufferParametersfvNV(i: Int, i1: Int, i2: Int, i3: Int, floats: Array[Float], i4: Int): Unit = ???

  override def glProgramBufferParametersIivNV(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramBufferParametersIivNV(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glProgramBufferParametersIuivNV(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramBufferParametersIuivNV(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glPixelDataRangeNV(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glFlushPixelDataRangeNV(i: Int): Unit = ???

  override def glPrimitiveRestartNV(): Unit = ???

  override def glPrimitiveRestartIndexNV(i: Int): Unit = ???

  override def glFramebufferSampleLocationsfvNV(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glFramebufferSampleLocationsfvNV(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glNamedFramebufferSampleLocationsfvNV(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glNamedFramebufferSampleLocationsfvNV(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glResolveDepthValuesNV(): Unit = ???

  override def glTextureBarrierNV(): Unit = ???

  override def glBindTransformFeedbackNV(i: Int, i1: Int): Unit = ???

  override def glDeleteTransformFeedbacksNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteTransformFeedbacksNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenTransformFeedbacksNV(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenTransformFeedbacksNV(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsTransformFeedbackNV(i: Int): Boolean = ???

  override def glPauseTransformFeedbackNV(): Unit = ???

  override def glResumeTransformFeedbackNV(): Unit = ???

  override def glDrawTransformFeedbackNV(i: Int, i1: Int): Unit = ???

  override def glVDPAUInitNV(buffer: Buffer, buffer1: Buffer): Unit = ???

  override def glVDPAUFiniNV(): Unit = ???

  override def glVDPAURegisterVideoSurfaceNV(buffer: Buffer, i: Int, i1: Int, intBuffer: IntBuffer): Long = ???

  override def glVDPAURegisterVideoSurfaceNV(buffer: Buffer, i: Int, i1: Int, ints: Array[Int], i2: Int): Long = ???

  override def glVDPAURegisterOutputSurfaceNV(buffer: Buffer, i: Int, i1: Int, intBuffer: IntBuffer): Long = ???

  override def glVDPAURegisterOutputSurfaceNV(buffer: Buffer, i: Int, i1: Int, ints: Array[Int], i2: Int): Long = ???

  override def glVDPAUIsSurfaceNV(l: Long): Boolean = ???

  override def glVDPAUUnregisterSurfaceNV(l: Long): Unit = ???

  override def glVDPAUGetSurfaceivNV(l: Long, i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer): Unit = ???

  override def glVDPAUGetSurfaceivNV(l: Long, i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int): Unit = ???

  override def glVDPAUSurfaceAccessNV(l: Long, i: Int): Unit = ???

  override def glVDPAUMapSurfacesNV(i: Int, pointerBuffer: PointerBuffer): Unit = ???

  override def glVDPAUUnmapSurfacesNV(i: Int, pointerBuffer: PointerBuffer): Unit = ???

  override def glVertexAttribL1i64NV(i: Int, l: Long): Unit = ???

  override def glVertexAttribL2i64NV(i: Int, l: Long, l1: Long): Unit = ???

  override def glVertexAttribL3i64NV(i: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glVertexAttribL4i64NV(i: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glVertexAttribL1i64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL1i64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL2i64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL2i64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL3i64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL3i64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL4i64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL4i64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL1ui64NV(i: Int, l: Long): Unit = ???

  override def glVertexAttribL2ui64NV(i: Int, l: Long, l1: Long): Unit = ???

  override def glVertexAttribL3ui64NV(i: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glVertexAttribL4ui64NV(i: Int, l: Long, l1: Long, l2: Long, l3: Long): Unit = ???

  override def glVertexAttribL1ui64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL1ui64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL2ui64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL2ui64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL3ui64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL3ui64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glVertexAttribL4ui64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glVertexAttribL4ui64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glGetVertexAttribLi64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetVertexAttribLi64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetVertexAttribLui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetVertexAttribLui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glVertexAttribLFormatNV(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertexAttribI1iEXT(i: Int, i1: Int): Unit = ???

  override def glVertexAttribI2iEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexAttribI3iEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertexAttribI4iEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexAttribI1uiEXT(i: Int, i1: Int): Unit = ???

  override def glVertexAttribI2uiEXT(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexAttribI3uiEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertexAttribI4uiEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexAttribI1ivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI1ivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI2ivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI2ivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI3ivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI3ivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI4ivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI4ivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI1uivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI1uivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI2uivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI2uivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI3uivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI3uivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI4uivEXT(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI4uivEXT(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI4bvEXT(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttribI4bvEXT(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttribI4svEXT(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribI4svEXT(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttribI4ubvEXT(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttribI4ubvEXT(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttribI4usvEXT(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribI4usvEXT(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttribIPointerEXT(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetVertexAttribIivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribIivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetVertexAttribIuivEXT(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribIuivEXT(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glBeginVideoCaptureNV(i: Int): Unit = ???

  override def glBindVideoCaptureStreamBufferNV(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glBindVideoCaptureStreamTextureNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glEndVideoCaptureNV(i: Int): Unit = ???

  override def glGetVideoCaptureivNV(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVideoCaptureivNV(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetVideoCaptureStreamivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVideoCaptureStreamivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetVideoCaptureStreamfvNV(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetVideoCaptureStreamfvNV(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetVideoCaptureStreamdvNV(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetVideoCaptureStreamdvNV(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glVideoCaptureNV(i: Int, intBuffer: IntBuffer, longBuffer: LongBuffer): Int = ???

  override def glVideoCaptureNV(i: Int, ints: Array[Int], i1: Int, longs: Array[Long], i2: Int): Int = ???

  override def glVideoCaptureStreamParameterivNV(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glVideoCaptureStreamParameterivNV(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glVideoCaptureStreamParameterfvNV(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVideoCaptureStreamParameterfvNV(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glVideoCaptureStreamParameterdvNV(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVideoCaptureStreamParameterdvNV(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glFramebufferTextureMultiviewOVR(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glFinishTextureSUNX(): Unit = ???

  override def mapNamedBufferEXT(i: Int, i1: Int): GLBufferStorage = ???

  override def mapNamedBufferRangeEXT(i: Int, l: Long, l1: Long, i1: Int): GLBufferStorage = ???

  override def glVertexAttribPointer(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int, buffer: Buffer): Unit = ???

  override def glDrawElementsInstanced(i: Int, i1: Int, i2: Int, buffer: Buffer, i3: Int): Unit = ???

  override def glDrawRangeElements(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glVertexAttribIPointer(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glLightfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glLightfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glMaterialf(i: Int, i1: Int, v: Float): Unit = ???

  override def glMaterialfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glMaterialfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glColor4f(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glShadeModel(i: Int): Unit = ???

  override def glLogicOp(i: Int): Unit = ???

  override def glPointSize(v: Float): Unit = ???

  override def glPolygonMode(i: Int, i1: Int): Unit = ???

  override def glDrawBuffer(i: Int): Unit = ???

  override def glGetDoublev(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetDoublev(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glPixelStoref(i: Int, v: Float): Unit = ???

  override def glTexImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glTexImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, l: Long): Unit = ???

  override def glGetTexImage(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetTexImage(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glEnableClientState(i: Int): Unit = ???

  override def glDisableClientState(i: Int): Unit = ???

  override def glTexSubImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer): Unit = ???

  override def glTexSubImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glCopyTexImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glCopyTexSubImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glCompressedTexImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glCompressedTexSubImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexSubImage1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glGetCompressedTexImage(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glGetCompressedTexImage(i: Int, i1: Int, l: Long): Unit = ???

  override def glMultiDrawArrays(i: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, i1: Int): Unit = ???

  override def glMultiDrawArrays(i: Int, ints: Array[Int], i1: Int, ints1: Array[Int], i2: Int, i3: Int): Unit = ???

  override def glMultiDrawElements(i: Int, intBuffer: IntBuffer, i1: Int, pointerBuffer: PointerBuffer, i2: Int): Unit = ???

  override def glPointParameterf(i: Int, v: Float): Unit = ???

  override def glPointParameterfv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glPointParameterfv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glPointParameteri(i: Int, i1: Int): Unit = ???

  override def glPointParameteriv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glPointParameteriv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetBufferSubData(i: Int, l: Long, l1: Long, buffer: Buffer): Unit = ???

  override def glGetVertexAttribdv(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetVertexAttribdv(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glVertexAttrib1d(i: Int, v: Double): Unit = ???

  override def glVertexAttrib1dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib1dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib1s(i: Int, i1: Short): Unit = ???

  override def glVertexAttrib1sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib1sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib2d(i: Int, v: Double, v1: Double): Unit = ???

  override def glVertexAttrib2dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib2dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib2s(i: Int, i1: Short, i2: Short): Unit = ???

  override def glVertexAttrib2sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib2sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib3d(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glVertexAttrib3dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib3dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib3s(i: Int, i1: Short, i2: Short, i3: Short): Unit = ???

  override def glVertexAttrib3sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib3sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4Nbv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4Nbv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4Niv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4Niv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4Nsv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4Nsv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4Nub(i: Int, b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glVertexAttrib4Nubv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4Nubv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4Nuiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4Nuiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4Nusv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4Nusv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4bv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4bv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4d(i: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glVertexAttrib4dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttrib4dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttrib4iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4s(i: Int, i1: Short, i2: Short, i3: Short, i4: Short): Unit = ???

  override def glVertexAttrib4sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttrib4ubv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttrib4ubv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttrib4uiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttrib4uiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttrib4usv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttrib4usv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glClampColor(i: Int, i1: Int): Unit = ???

  override def glVertexAttribI1i(i: Int, i1: Int): Unit = ???

  override def glVertexAttribI2i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexAttribI3i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertexAttribI1ui(i: Int, i1: Int): Unit = ???

  override def glVertexAttribI2ui(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexAttribI3ui(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glVertexAttribI1iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI1iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI2iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI2iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI3iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI3iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI1uiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI1uiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI2uiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI2uiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI3uiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI3uiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI4bv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttribI4bv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttribI4sv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribI4sv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glVertexAttribI4ubv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glVertexAttribI4ubv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glVertexAttribI4usv(i: Int, shortBuffer: ShortBuffer): Unit = ???

  override def glVertexAttribI4usv(i: Int, shorts: Array[Short], i1: Int): Unit = ???

  override def glBindFragDataLocation(i: Int, i1: Int, s: String): Unit = ???

  override def glFramebufferTexture1D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glPrimitiveRestartIndex(i: Int): Unit = ???

  override def glGetActiveUniformName(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetActiveUniformName(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, bytes: Array[Byte], i4: Int): Unit = ???

  override def glProvokingVertex(i: Int): Unit = ???

  override def glDrawTransformFeedback(i: Int, i1: Int): Unit = ???

  override def glDrawTransformFeedbackStream(i: Int, i1: Int, i2: Int): Unit = ???

  override def glBeginQueryIndexed(i: Int, i1: Int, i2: Int): Unit = ???

  override def glEndQueryIndexed(i: Int, i1: Int): Unit = ???

  override def glGetQueryIndexediv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetQueryIndexediv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform1d(i: Int, i1: Int, v: Double): Unit = ???

  override def glProgramUniform1dv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform1dv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform2d(i: Int, i1: Int, v: Double, v1: Double): Unit = ???

  override def glProgramUniform2dv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform2dv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform3d(i: Int, i1: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glProgramUniform3dv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform3dv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniform4d(i: Int, i1: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glProgramUniform4dv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniform4dv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2x3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3x2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix2x4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4x2dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix3x4dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubleBuffer: DoubleBuffer): Unit = ???

  override def glProgramUniformMatrix4x3dv(i: Int, i1: Int, i2: Int, b: Boolean, doubles: Array[Double], i3: Int): Unit = ???

  override def glVertexAttribL1d(i: Int, v: Double): Unit = ???

  override def glVertexAttribL2d(i: Int, v: Double, v1: Double): Unit = ???

  override def glVertexAttribL3d(i: Int, v: Double, v1: Double, v2: Double): Unit = ???

  override def glVertexAttribL4d(i: Int, v: Double, v1: Double, v2: Double, v3: Double): Unit = ???

  override def glVertexAttribL1dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttribL1dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttribL2dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttribL2dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttribL3dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttribL3dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttribL4dv(i: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glVertexAttribL4dv(i: Int, doubles: Array[Double], i1: Int): Unit = ???

  override def glVertexAttribLPointer(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glGetVertexAttribLdv(i: Int, i1: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetVertexAttribLdv(i: Int, i1: Int, doubles: Array[Double], i2: Int): Unit = ???

  override def glGetActiveAtomicCounterBufferiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetActiveAtomicCounterBufferiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glClearBufferData(i: Int, i1: Int, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glClearBufferSubData(i: Int, i1: Int, l: Long, l1: Long, i2: Int, i3: Int, buffer: Buffer): Unit = ???

  override def glGetInternalformati64v(i: Int, i1: Int, i2: Int, i3: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetInternalformati64v(i: Int, i1: Int, i2: Int, i3: Int, longs: Array[Long], i4: Int): Unit = ???

  override def glInvalidateTexSubImage(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glInvalidateTexImage(i: Int, i1: Int): Unit = ???

  override def glInvalidateBufferSubData(i: Int, l: Long, l1: Long): Unit = ???

  override def glInvalidateBufferData(i: Int): Unit = ???

  override def glGetnCompressedTexImage(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glGetnTexImage(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, buffer: Buffer): Unit = ???

  override def glGetnUniformdv(i: Int, i1: Int, i2: Int, doubleBuffer: DoubleBuffer): Unit = ???

  override def glGetnUniformdv(i: Int, i1: Int, i2: Int, doubles: Array[Double], i3: Int): Unit = ???

  override def glBufferPageCommitmentARB(i: Int, l: Long, l1: Long, b: Boolean): Unit = ???

  override def glNamedBufferPageCommitmentEXT(i: Int, l: Long, l1: Long, b: Boolean): Unit = ???

  override def glNamedBufferPageCommitmentARB(i: Int, l: Long, l1: Long, b: Boolean): Unit = ???

  override def glTexPageCommitmentARB(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, b: Boolean): Unit = ???

  override def glDebugMessageEnableAMD(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, b: Boolean): Unit = ???

  override def glDebugMessageEnableAMD(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, b: Boolean): Unit = ???

  override def glDebugMessageInsertAMD(i: Int, i1: Int, i2: Int, i3: Int, s: String): Unit = ???

  override def glGetDebugMessageLogAMD(i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, intBuffer3: IntBuffer, byteBuffer: ByteBuffer): Int = ???

  override def glGetDebugMessageLogAMD(i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int, ints2: Array[Int], i4: Int, ints3: Array[Int], i5: Int, bytes: Array[Byte], i6: Int): Int = ???

  override def glGetUniformui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetUniformui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glMultiDrawArraysIndirectAMD(i: Int, buffer: Buffer, i1: Int, i2: Int): Unit = ???

  override def glMultiDrawElementsIndirectAMD(i: Int, i1: Int, buffer: Buffer, i2: Int, i3: Int): Unit = ???

  override def glSetMultisamplefvAMD(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glSetMultisamplefvAMD(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glStencilOpValueAMD(i: Int, i1: Int): Unit = ???

  override def glTessellationFactorAMD(v: Float): Unit = ???

  override def glTessellationModeAMD(i: Int): Unit = ???

  override def glImportSyncEXT(i: Int, l: Long, i1: Int): Long = ???

  override def glMakeBufferResidentNV(i: Int, i1: Int): Unit = ???

  override def glMakeBufferNonResidentNV(i: Int): Unit = ???

  override def glIsBufferResidentNV(i: Int): Boolean = ???

  override def glMakeNamedBufferResidentNV(i: Int, i1: Int): Unit = ???

  override def glMakeNamedBufferNonResidentNV(i: Int): Unit = ???

  override def glIsNamedBufferResidentNV(i: Int): Boolean = ???

  override def glGetBufferParameterui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetBufferParameterui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetNamedBufferParameterui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetNamedBufferParameterui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetIntegerui64vNV(i: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetIntegerui64vNV(i: Int, longs: Array[Long], i1: Int): Unit = ???

  override def glUniformui64NV(i: Int, l: Long): Unit = ???

  override def glUniformui64vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glUniformui64vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glProgramUniformui64NV(i: Int, i1: Int, l: Long): Unit = ???

  override def glProgramUniformui64vNV(i: Int, i1: Int, i2: Int, longBuffer: LongBuffer): Unit = ???

  override def glProgramUniformui64vNV(i: Int, i1: Int, i2: Int, longs: Array[Long], i3: Int): Unit = ???

  override def glTexImage2DMultisampleCoverageNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, b: Boolean): Unit = ???

  override def glTexImage3DMultisampleCoverageNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, b: Boolean): Unit = ???

  override def glTextureImage2DMultisampleNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, b: Boolean): Unit = ???

  override def glTextureImage3DMultisampleNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, b: Boolean): Unit = ???

  override def glTextureImage2DMultisampleCoverageNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, b: Boolean): Unit = ???

  override def glTextureImage3DMultisampleCoverageNV(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, b: Boolean): Unit = ???

  override def glBufferAddressRangeNV(i: Int, i1: Int, l: Long, l1: Long): Unit = ???

  override def glVertexFormatNV(i: Int, i1: Int, i2: Int): Unit = ???

  override def glNormalFormatNV(i: Int, i1: Int): Unit = ???

  override def glColorFormatNV(i: Int, i1: Int, i2: Int): Unit = ???

  override def glIndexFormatNV(i: Int, i1: Int): Unit = ???

  override def glTexCoordFormatNV(i: Int, i1: Int, i2: Int): Unit = ???

  override def glEdgeFlagFormatNV(i: Int): Unit = ???

  override def glSecondaryColorFormatNV(i: Int, i1: Int, i2: Int): Unit = ???

  override def glFogCoordFormatNV(i: Int, i1: Int): Unit = ???

  override def glVertexAttribFormatNV(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int): Unit = ???

  override def glVertexAttribIFormatNV(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glGetIntegerui64i_vNV(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetIntegerui64i_vNV(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glAlphaFunc(i: Int, v: Float): Unit = ???

  override def glFogf(i: Int, v: Float): Unit = ???

  override def glFogfv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glFogfv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetLightfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetLightfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetMaterialfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMaterialfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetTexEnvfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTexEnvfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glLightModelf(i: Int, v: Float): Unit = ???

  override def glLightModelfv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glLightModelfv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glLightf(i: Int, i1: Int, v: Float): Unit = ???

  override def glMultiTexCoord4f(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glNormal3f(v: Float, v1: Float, v2: Float): Unit = ???

  override def glTexEnvf(i: Int, i1: Int, v: Float): Unit = ???

  override def glTexEnvfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glTexEnvfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glClientActiveTexture(i: Int): Unit = ???

  override def glColor4ub(b: Byte, b1: Byte, b2: Byte, b3: Byte): Unit = ???

  override def glGetTexEnviv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexEnviv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glTexEnvi(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTexEnviv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glTexEnviv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glOrtho(v: Double, v1: Double, v2: Double, v3: Double, v4: Double, v5: Double): Unit = ???

  override def glFrustum(v: Double, v1: Double, v2: Double, v3: Double, v4: Double, v5: Double): Unit = ???

  override def glDrawElements(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glReadBuffer(i: Int): Unit = ???

  override def glGetTexLevelParameterfv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTexLevelParameterfv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetTexLevelParameteriv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexLevelParameteriv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glDrawRangeElements(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, l: Long): Unit = ???

  override def glUniformMatrix2x3fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix2x3fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix3x2fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix3x2fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix2x4fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix2x4fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix4x2fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix4x2fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix3x4fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix3x4fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix4x3fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix4x3fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glColorMaski(i: Int, b: Boolean, b1: Boolean, b2: Boolean, b3: Boolean): Unit = ???

  override def glGetBooleani_v(i: Int, i1: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetBooleani_v(i: Int, i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glGetIntegeri_v(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetIntegeri_v(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glEnablei(i: Int, i1: Int): Unit = ???

  override def glDisablei(i: Int, i1: Int): Unit = ???

  override def glIsEnabledi(i: Int, i1: Int): Boolean = ???

  override def glBeginTransformFeedback(i: Int): Unit = ???

  override def glEndTransformFeedback(): Unit = ???

  override def glBindBufferRange(i: Int, i1: Int, i2: Int, l: Long, l1: Long): Unit = ???

  override def glBindBufferBase(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTransformFeedbackVaryings(i: Int, i1: Int, strings: Array[String], i2: Int): Unit = ???

  override def glGetTransformFeedbackVarying(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetTransformFeedbackVarying(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int, ints2: Array[Int], i5: Int, bytes: Array[Byte], i6: Int): Unit = ???

  override def glBeginConditionalRender(i: Int, i1: Int): Unit = ???

  override def glEndConditionalRender(): Unit = ???

  override def glVertexAttribIPointer(i: Int, i1: Int, i2: Int, i3: Int, l: Long): Unit = ???

  override def glGetVertexAttribIiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribIiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetVertexAttribIuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribIuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glVertexAttribI4i(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexAttribI4ui(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glVertexAttribI4iv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI4iv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glVertexAttribI4uiv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glVertexAttribI4uiv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetUniformuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetUniformuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetFragDataLocation(i: Int, s: String): Int = ???

  override def glUniform1ui(i: Int, i1: Int): Unit = ???

  override def glUniform2ui(i: Int, i1: Int, i2: Int): Unit = ???

  override def glUniform3ui(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glUniform4ui(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glUniform1uiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform1uiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform2uiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform2uiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform3uiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform3uiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform4uiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform4uiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glClearBufferiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glClearBufferiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glClearBufferuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glClearBufferuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glClearBufferfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glClearBufferfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glClearBufferfi(i: Int, i1: Int, v: Float, i2: Int): Unit = ???

  override def glGetStringi(i: Int, i1: Int): String = ???

  override def glBlitFramebuffer(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int): Unit = ???

  override def glFramebufferTextureLayer(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glBindVertexArray(i: Int): Unit = ???

  override def glDeleteVertexArrays(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteVertexArrays(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenVertexArrays(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenVertexArrays(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsVertexArray(i: Int): Boolean = ???

  override def glDrawArraysInstanced(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glDrawElementsInstanced(i: Int, i1: Int, i2: Int, l: Long, i3: Int): Unit = ???

  override def glTexBuffer(i: Int, i1: Int, i2: Int): Unit = ???

  override def glCopyBufferSubData(i: Int, i1: Int, l: Long, l1: Long, l2: Long): Unit = ???

  override def glGetUniformIndices(i: Int, i1: Int, strings: Array[String], intBuffer: IntBuffer): Unit = ???

  override def glGetUniformIndices(i: Int, i1: Int, strings: Array[String], ints: Array[Int], i2: Int): Unit = ???

  override def glGetActiveUniformsiv(i: Int, i1: Int, intBuffer: IntBuffer, i2: Int, intBuffer1: IntBuffer): Unit = ???

  override def glGetActiveUniformsiv(i: Int, i1: Int, ints: Array[Int], i2: Int, i3: Int, ints1: Array[Int], i4: Int): Unit = ???

  override def glGetUniformBlockIndex(i: Int, s: String): Int = ???

  override def glGetActiveUniformBlockiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetActiveUniformBlockiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetActiveUniformBlockName(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetActiveUniformBlockName(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, bytes: Array[Byte], i4: Int): Unit = ???

  override def glUniformBlockBinding(i: Int, i1: Int, i2: Int): Unit = ???

  override def glVertexAttribDivisor(i: Int, i1: Int): Unit = ???

  override def glMinSampleShading(v: Float): Unit = ???

  override def glBlendEquationi(i: Int, i1: Int): Unit = ???

  override def glBlendEquationSeparatei(i: Int, i1: Int, i2: Int): Unit = ???

  override def glBlendFunci(i: Int, i1: Int, i2: Int): Unit = ???

  override def glBlendFuncSeparatei(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glBindTransformFeedback(i: Int, i1: Int): Unit = ???

  override def glDeleteTransformFeedbacks(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteTransformFeedbacks(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenTransformFeedbacks(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenTransformFeedbacks(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsTransformFeedback(i: Int): Boolean = ???

  override def glPauseTransformFeedback(): Unit = ???

  override def glResumeTransformFeedback(): Unit = ???

  override def glGetInternalformativ(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetInternalformativ(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int): Unit = ???

  override def glBindImageTexture(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glMemoryBarrier(i: Int): Unit = ???

  override def glFramebufferParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glGetFramebufferParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetFramebufferParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glInvalidateFramebuffer(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glInvalidateFramebuffer(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glInvalidateSubFramebuffer(i: Int, i1: Int, intBuffer: IntBuffer, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glInvalidateSubFramebuffer(i: Int, i1: Int, ints: Array[Int], i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glTexStorage2DMultisample(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, b: Boolean): Unit = ???

  override def glTexStorage3DMultisample(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, b: Boolean): Unit = ???

  override def glGetnUniformuiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetnUniformuiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glFramebufferTextureEXT(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def isPBOPackBound: Boolean = ???

  override def isPBOUnpackBound: Boolean = ???

  override def glActiveTexture(i: Int): Unit = ???

  override def glBindBuffer(i: Int, i1: Int): Unit = ???

  override def glBindFramebuffer(i: Int, i1: Int): Unit = ???

  override def glBindRenderbuffer(i: Int, i1: Int): Unit = ???

  override def glBindTexture(i: Int, i1: Int): Unit = ???

  override def glBlendEquation(i: Int): Unit = ???

  override def glBlendEquationSeparate(i: Int, i1: Int): Unit = ???

  override def glBlendFunc(i: Int, i1: Int): Unit = ???

  override def glBlendFuncSeparate(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glBufferData(i: Int, l: Long, buffer: Buffer, i1: Int): Unit = ???

  override def glBufferSubData(i: Int, l: Long, l1: Long, buffer: Buffer): Unit = ???

  override def glCheckFramebufferStatus(i: Int): Int = ???

  override def glClear(i: Int): Unit = ???

  override def glClearColor(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glClearStencil(i: Int): Unit = ???

  override def glColorMask(b: Boolean, b1: Boolean, b2: Boolean, b3: Boolean): Unit = ???

  override def glCompressedTexImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, l: Long): Unit = ???

  override def glCompressedTexSubImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexSubImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, l: Long): Unit = ???

  override def glCopyTexImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glCopyTexSubImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int): Unit = ???

  override def glCullFace(i: Int): Unit = ???

  override def glDeleteBuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteBuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteFramebuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteFramebuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteRenderbuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteRenderbuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteTextures(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteTextures(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDepthFunc(i: Int): Unit = ???

  override def glDepthMask(b: Boolean): Unit = ???

  override def glDisable(i: Int): Unit = ???

  override def glDrawArrays(i: Int, i1: Int, i2: Int): Unit = ???

  override def glDrawElements(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glEnable(i: Int): Unit = ???

  override def glFinish(): Unit = ???

  override def glFlush(): Unit = ???

  override def glFramebufferRenderbuffer(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glFramebufferTexture2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glFrontFace(i: Int): Unit = ???

  override def glGenBuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenBuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenerateMipmap(i: Int): Unit = ???

  override def glGenFramebuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenFramebuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenRenderbuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenRenderbuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenTextures(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenTextures(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetBooleanv(i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glGetBooleanv(i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glGetBufferParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetBufferParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetError(): Int = ???

  override def glGetFramebufferAttachmentParameteriv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetFramebufferAttachmentParameteriv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glGetRenderbufferParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetRenderbufferParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetString(i: Int): String = ???

  override def glGetTexParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetTexParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetTexParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glHint(i: Int, i1: Int): Unit = ???

  override def glIsBuffer(i: Int): Boolean = ???

  override def glIsEnabled(i: Int): Boolean = ???

  override def glIsFramebuffer(i: Int): Boolean = ???

  override def glIsRenderbuffer(i: Int): Boolean = ???

  override def glIsTexture(i: Int): Boolean = ???

  override def glLineWidth(v: Float): Unit = ???

  override def glPixelStorei(i: Int, i1: Int): Unit = ???

  override def glPolygonOffset(v: Float, v1: Float): Unit = ???

  override def glReadPixels(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, buffer: Buffer): Unit = ???

  override def glReadPixels(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, l: Long): Unit = ???

  override def glRenderbufferStorage(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glSampleCoverage(v: Float, b: Boolean): Unit = ???

  override def glScissor(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glStencilFunc(i: Int, i1: Int, i2: Int): Unit = ???

  override def glStencilMask(i: Int): Unit = ???

  override def glStencilOp(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTexImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glTexImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, l: Long): Unit = ???

  override def glTexParameterf(i: Int, i1: Int, v: Float): Unit = ???

  override def glTexParameterfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glTexParameterfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glTexParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glTexParameteriv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glTexParameteriv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glTexSubImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glTexSubImage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, l: Long): Unit = ???

  override def glViewport(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTexStorage1D(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glTexStorage2D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glTexStorage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glTextureStorage1DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glTextureStorage2DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glTextureStorage3DEXT(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int): Unit = ???

  override def glMapBuffer(i: Int, i1: Int): ByteBuffer = ???

  override def glUnmapBuffer(i: Int): Boolean = ???

  override def glRenderbufferStorageMultisample(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glMapBufferRange(i: Int, l: Long, l1: Long, i1: Int): ByteBuffer = ???

  override def glFlushMappedBufferRange(i: Int, l: Long, l1: Long): Unit = ???

  override def glGetGraphicsResetStatus(): Int = ???

  override def glReadnPixels(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, buffer: Buffer): Unit = ???

  override def glGetnUniformfv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetnUniformfv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glGetnUniformiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetnUniformiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glVertexPointer(glArrayData: GLArrayData): Unit = ???

  override def glVertexPointer(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glVertexPointer(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glColorPointer(glArrayData: GLArrayData): Unit = ???

  override def glColorPointer(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glColorPointer(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glNormalPointer(glArrayData: GLArrayData): Unit = ???

  override def glNormalPointer(i: Int, i1: Int, buffer: Buffer): Unit = ???

  override def glNormalPointer(i: Int, i1: Int, l: Long): Unit = ???

  override def glTexCoordPointer(glArrayData: GLArrayData): Unit = ???

  override def glTexCoordPointer(i: Int, i1: Int, i2: Int, buffer: Buffer): Unit = ???

  override def glTexCoordPointer(i: Int, i1: Int, i2: Int, l: Long): Unit = ???

  override def glAttachShader(i: Int, i1: Int): Unit = ???

  override def glBindAttribLocation(i: Int, i1: Int, s: String): Unit = ???

  override def glBlendColor(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glCompileShader(i: Int): Unit = ???

  override def glCreateProgram(): Int = ???

  override def glCreateShader(i: Int): Int = ???

  override def glDeleteProgram(i: Int): Unit = ???

  override def glDeleteShader(i: Int): Unit = ???

  override def glDetachShader(i: Int, i1: Int): Unit = ???

  override def glDisableVertexAttribArray(i: Int): Unit = ???

  override def glEnableVertexAttribArray(i: Int): Unit = ???

  override def glGetActiveAttrib(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetActiveAttrib(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int, ints2: Array[Int], i5: Int, bytes: Array[Byte], i6: Int): Unit = ???

  override def glGetActiveUniform(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetActiveUniform(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, ints1: Array[Int], i4: Int, ints2: Array[Int], i5: Int, bytes: Array[Byte], i6: Int): Unit = ???

  override def glGetAttachedShaders(i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer): Unit = ???

  override def glGetAttachedShaders(i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int): Unit = ???

  override def glGetAttribLocation(i: Int, s: String): Int = ???

  override def glGetProgramiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetProgramInfoLog(i: Int, i1: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetProgramInfoLog(i: Int, i1: Int, ints: Array[Int], i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetShaderiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetShaderiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetShaderInfoLog(i: Int, i1: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetShaderInfoLog(i: Int, i1: Int, ints: Array[Int], i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetShaderSource(i: Int, i1: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetShaderSource(i: Int, i1: Int, ints: Array[Int], i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetUniformfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetUniformfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetUniformiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetUniformiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetUniformLocation(i: Int, s: String): Int = ???

  override def glGetVertexAttribfv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetVertexAttribfv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glGetVertexAttribiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetVertexAttribiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glIsProgram(i: Int): Boolean = ???

  override def glIsShader(i: Int): Boolean = ???

  override def glLinkProgram(i: Int): Unit = ???

  override def glShaderSource(i: Int, i1: Int, strings: Array[String], intBuffer: IntBuffer): Unit = ???

  override def glShaderSource(i: Int, i1: Int, strings: Array[String], ints: Array[Int], i2: Int): Unit = ???

  override def glStencilFuncSeparate(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glStencilMaskSeparate(i: Int, i1: Int): Unit = ???

  override def glStencilOpSeparate(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glUniform1f(i: Int, v: Float): Unit = ???

  override def glUniform1fv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform1fv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform1i(i: Int, i1: Int): Unit = ???

  override def glUniform1iv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform1iv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform2f(i: Int, v: Float, v1: Float): Unit = ???

  override def glUniform2fv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform2fv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform2i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glUniform2iv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform2iv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform3f(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glUniform3fv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform3fv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform3i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glUniform3iv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform3iv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniform4f(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glUniform4fv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glUniform4fv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glUniform4i(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glUniform4iv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glUniform4iv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glUniformMatrix2fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix2fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix3fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix3fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUniformMatrix4fv(i: Int, i1: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glUniformMatrix4fv(i: Int, i1: Int, b: Boolean, floats: Array[Float], i2: Int): Unit = ???

  override def glUseProgram(i: Int): Unit = ???

  override def glValidateProgram(i: Int): Unit = ???

  override def glVertexAttrib1f(i: Int, v: Float): Unit = ???

  override def glVertexAttrib1fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib1fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib2f(i: Int, v: Float, v1: Float): Unit = ???

  override def glVertexAttrib2fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib2fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib3f(i: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glVertexAttrib3fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib3fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttrib4f(i: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glVertexAttrib4fv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glVertexAttrib4fv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glVertexAttribPointer(i: Int, i1: Int, i2: Int, b: Boolean, i3: Int, l: Long): Unit = ???

  override def glTexImage2DMultisample(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, b: Boolean): Unit = ???

  override def glTexImage3DMultisample(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, b: Boolean): Unit = ???

  override def glGetMultisamplefv(i: Int, i1: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetMultisamplefv(i: Int, i1: Int, floats: Array[Float], i2: Int): Unit = ???

  override def glSampleMaski(i: Int, i1: Int): Unit = ???

  override def glDebugMessageControl(i: Int, i1: Int, i2: Int, i3: Int, intBuffer: IntBuffer, b: Boolean): Unit = ???

  override def glDebugMessageControl(i: Int, i1: Int, i2: Int, i3: Int, ints: Array[Int], i4: Int, b: Boolean): Unit = ???

  override def glDebugMessageInsert(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, s: String): Unit = ???

  override def glGetDebugMessageLog(i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, intBuffer2: IntBuffer, intBuffer3: IntBuffer, intBuffer4: IntBuffer, byteBuffer: ByteBuffer): Int = ???

  override def glGetDebugMessageLog(i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int, ints2: Array[Int], i4: Int, ints3: Array[Int], i5: Int, ints4: Array[Int], i6: Int, bytes: Array[Byte], i7: Int): Int = ???

  override def glPushDebugGroup(i: Int, i1: Int, i2: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glPushDebugGroup(i: Int, i1: Int, i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glPopDebugGroup(): Unit = ???

  override def glObjectLabel(i: Int, i1: Int, i2: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glObjectLabel(i: Int, i1: Int, i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetObjectLabel(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetObjectLabel(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int, bytes: Array[Byte], i4: Int): Unit = ???

  override def glObjectPtrLabel(buffer: Buffer, i: Int, byteBuffer: ByteBuffer): Unit = ???

  override def glObjectPtrLabel(buffer: Buffer, i: Int, bytes: Array[Byte], i1: Int): Unit = ???

  override def glGetObjectPtrLabel(buffer: Buffer, i: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetObjectPtrLabel(buffer: Buffer, i: Int, ints: Array[Int], i1: Int, bytes: Array[Byte], i2: Int): Unit = ???

  override def glCopyImageSubData(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, i11: Int, i12: Int, i13: Int, i14: Int): Unit = ???

  override def glGetProgramBinary(i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer, buffer: Buffer): Unit = ???

  override def glGetProgramBinary(i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int, buffer: Buffer): Unit = ???

  override def glProgramBinary(i: Int, i1: Int, buffer: Buffer, i2: Int): Unit = ???

  override def glTexImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, buffer: Buffer): Unit = ???

  override def glTexImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, l: Long): Unit = ???

  override def glTexSubImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, buffer: Buffer): Unit = ???

  override def glTexSubImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, l: Long): Unit = ???

  override def glCopyTexSubImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int): Unit = ???

  override def glCompressedTexImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, l: Long): Unit = ???

  override def glCompressedTexSubImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, buffer: Buffer): Unit = ???

  override def glCompressedTexSubImage3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, l: Long): Unit = ???

  override def glFramebufferTexture3D(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glTexParameterIiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glTexParameterIiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glTexParameterIuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glTexParameterIuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetTexParameterIiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexParameterIiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetTexParameterIuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetTexParameterIuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glSamplerParameterIiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glSamplerParameterIiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glSamplerParameterIuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glSamplerParameterIuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetSamplerParameterIiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetSamplerParameterIiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetSamplerParameterIuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetSamplerParameterIuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glDrawArraysInstancedBaseInstance(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glDrawElementsInstancedBaseInstance(i: Int, i1: Int, i2: Int, l: Long, i3: Int, i4: Int): Unit = ???

  override def glDrawElementsInstancedBaseVertexBaseInstance(i: Int, i1: Int, i2: Int, l: Long, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glGenQueries(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenQueries(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glDeleteQueries(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteQueries(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glIsQuery(i: Int): Boolean = ???

  override def glBeginQuery(i: Int, i1: Int): Unit = ???

  override def glEndQuery(i: Int): Unit = ???

  override def glQueryCounter(i: Int, i1: Int): Unit = ???

  override def glGetQueryiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetQueryiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetQueryObjectiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetQueryObjectiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetQueryObjectuiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetQueryObjectuiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glGetQueryObjecti64v(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetQueryObjecti64v(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glGetQueryObjectui64v(i: Int, i1: Int, longBuffer: LongBuffer): Unit = ???

  override def glGetQueryObjectui64v(i: Int, i1: Int, longs: Array[Long], i2: Int): Unit = ???

  override def glActiveShaderProgram(i: Int, i1: Int): Unit = ???

  override def glBindProgramPipeline(i: Int): Unit = ???

  override def glCreateShaderProgramv(i: Int, i1: Int, strings: Array[String]): Int = ???

  override def glDeleteProgramPipelines(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDeleteProgramPipelines(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGenProgramPipelines(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGenProgramPipelines(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glGetProgramPipelineInfoLog(i: Int, i1: Int, intBuffer: IntBuffer, byteBuffer: ByteBuffer): Unit = ???

  override def glGetProgramPipelineInfoLog(i: Int, i1: Int, ints: Array[Int], i2: Int, bytes: Array[Byte], i3: Int): Unit = ???

  override def glGetProgramPipelineiv(i: Int, i1: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetProgramPipelineiv(i: Int, i1: Int, ints: Array[Int], i2: Int): Unit = ???

  override def glIsProgramPipeline(i: Int): Boolean = ???

  override def glProgramParameteri(i: Int, i1: Int, i2: Int): Unit = ???

  override def glProgramUniform1f(i: Int, i1: Int, v: Float): Unit = ???

  override def glProgramUniform1fv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniform1fv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniform1i(i: Int, i1: Int, i2: Int): Unit = ???

  override def glProgramUniform1iv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform1iv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform2f(i: Int, i1: Int, v: Float, v1: Float): Unit = ???

  override def glProgramUniform2fv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniform2fv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniform2i(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glProgramUniform2iv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform2iv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform3f(i: Int, i1: Int, v: Float, v1: Float, v2: Float): Unit = ???

  override def glProgramUniform3fv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniform3fv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniform3i(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glProgramUniform3iv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform3iv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform4f(i: Int, i1: Int, v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glProgramUniform4fv(i: Int, i1: Int, i2: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniform4fv(i: Int, i1: Int, i2: Int, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniform4i(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramUniform4iv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform4iv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniformMatrix2fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix2fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix3fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix3fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix4fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix4fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glUseProgramStages(i: Int, i1: Int, i2: Int): Unit = ???

  override def glValidateProgramPipeline(i: Int): Unit = ???

  override def glProgramUniform1ui(i: Int, i1: Int, i2: Int): Unit = ???

  override def glProgramUniform2ui(i: Int, i1: Int, i2: Int, i3: Int): Unit = ???

  override def glProgramUniform3ui(i: Int, i1: Int, i2: Int, i3: Int, i4: Int): Unit = ???

  override def glProgramUniform4ui(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Unit = ???

  override def glProgramUniform1uiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform1uiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform2uiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform2uiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform3uiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform3uiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniform4uiv(i: Int, i1: Int, i2: Int, intBuffer: IntBuffer): Unit = ???

  override def glProgramUniform4uiv(i: Int, i1: Int, i2: Int, ints: Array[Int], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x3fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix2x3fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x2fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix3x2fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix2x4fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix2x4fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x2fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix4x2fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix3x4fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix3x4fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glProgramUniformMatrix4x3fv(i: Int, i1: Int, i2: Int, b: Boolean, floatBuffer: FloatBuffer): Unit = ???

  override def glProgramUniformMatrix4x3fv(i: Int, i1: Int, i2: Int, b: Boolean, floats: Array[Float], i3: Int): Unit = ???

  override def glApplyFramebufferAttachmentCMAAINTEL(): Unit = ???

  override def glDrawBuffers(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glDrawBuffers(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glReleaseShaderCompiler(): Unit = ???

  override def glShaderBinary(i: Int, intBuffer: IntBuffer, i1: Int, buffer: Buffer, i2: Int): Unit = ???

  override def glShaderBinary(i: Int, ints: Array[Int], i1: Int, i2: Int, buffer: Buffer, i3: Int): Unit = ???

  override def glGetShaderPrecisionFormat(i: Int, i1: Int, intBuffer: IntBuffer, intBuffer1: IntBuffer): Unit = ???

  override def glGetShaderPrecisionFormat(i: Int, i1: Int, ints: Array[Int], i2: Int, ints1: Array[Int], i3: Int): Unit = ???

  override def glDepthRangef(v: Float, v1: Float): Unit = ???

  override def glDepthRange(v: Double, v1: Double): Unit = ???

  override def glClearDepthf(v: Float): Unit = ???

  override def glClearDepth(v: Double): Unit = ???

  override def glVertexAttribPointer(glArrayData: GLArrayData): Unit = ???

  override def glUniform(glUniformData: GLUniformData): Unit = ???

  override def isGL: Boolean = ???

  override def isGL4bc: Boolean = ???

  override def isGL4: Boolean = ???

  override def isGL3bc: Boolean = ???

  override def isGL3: Boolean = ???

  override def isGL2: Boolean = ???

  override def isGLES1: Boolean = ???

  override def isGLES2: Boolean = ???

  override def isGLES3: Boolean = ???

  override def isGLES: Boolean = ???

  override def isGL2ES1: Boolean = ???

  override def isGL2ES2: Boolean = ???

  override def isGL2ES3: Boolean = ???

  override def isGL3ES3: Boolean = ???

  override def isGL4ES3: Boolean = ???

  override def isGL2GL3: Boolean = ???

  override def isGL4core: Boolean = ???

  override def isGL3core: Boolean = ???

  override def isGLcore: Boolean = ???

  override def isGLES2Compatible: Boolean = ???

  override def isGLES3Compatible: Boolean = ???

  override def isGLES31Compatible: Boolean = ???

  override def isGLES32Compatible: Boolean = ???

  override def hasGLSL: Boolean = ???

  override def getDownstreamGL: GL = ???

  override def getRootGL: GL = ???

  override def getGL: GL = ???

  override def getGL4bc: GL4bc = ???

  override def getGL4: GL4 = ???

  override def getGL3bc: GL3bc = ???

  override def getGL3: GL3 = ???

  override def getGL2: GL2 = ???

  override def getGLES1: GLES1 = ???

  override def getGLES2: GLES2 = ???

  override def getGLES3: GLES3 = ???

  override def getGL2ES1: GL2ES1 = ???

  override def getGL2ES2: GL2ES2 = ???

  override def getGL2ES3: GL2ES3 = ???

  override def getGL3ES3: GL3ES3 = ???

  override def getGL4ES3: GL4ES3 = ???

  override def getGL2GL3: GL2GL3 = ???

  override def getGLProfile: GLProfile = ???

  override def getContext: GLContext = ???

  override def isFunctionAvailable(s: String): Boolean = ???

  override def isExtensionAvailable(s: String): Boolean = ???

  override def hasBasicFBOSupport: Boolean = ???

  override def hasFullFBOSupport: Boolean = ???

  override def getMaxRenderbufferSamples: Int = ???

  override def isNPOTTextureAvailable: Boolean = ???

  override def isTextureFormatBGRA8888Available: Boolean = ???

  override def setSwapInterval(i: Int): Unit = ???

  override def getSwapInterval: Int = ???

  override def getPlatformGLExtensions: AnyRef = ???

  override def getExtension(s: String): AnyRef = ???

  override def getBoundBuffer(i: Int): Int = ???

  override def getBufferStorage(i: Int): GLBufferStorage = ???

  override def mapBuffer(i: Int, i1: Int): GLBufferStorage = ???

  override def mapBufferRange(i: Int, l: Long, l1: Long, i1: Int): GLBufferStorage = ???

  override def isVBOArrayBound: Boolean = ???

  override def isVBOElementArrayBound: Boolean = ???

  override def getBoundFramebuffer(i: Int): Int = ???

  override def getDefaultDrawFramebuffer: Int = ???

  override def getDefaultReadFramebuffer: Int = ???

  override def getDefaultReadBuffer: Int = ???

  override def glGetFloatv(i: Int, floatBuffer: FloatBuffer): Unit = ???

  override def glGetFloatv(i: Int, floats: Array[Float], i1: Int): Unit = ???

  override def glGetIntegerv(i: Int, intBuffer: IntBuffer): Unit = ???

  override def glGetIntegerv(i: Int, ints: Array[Int], i1: Int): Unit = ???

  override def glMatrixMode(i: Int): Unit = ???

  override def glPushMatrix(): Unit = ???

  override def glPopMatrix(): Unit = ???

  override def glLoadIdentity(): Unit = ???

  override def glLoadMatrixf(floatBuffer: FloatBuffer): Unit = ???

  override def glLoadMatrixf(floats: Array[Float], i: Int): Unit = ???

  override def glMultMatrixf(floatBuffer: FloatBuffer): Unit = ???

  override def glMultMatrixf(floats: Array[Float], i: Int): Unit = ???

  override def glTranslatef(v: Float, v1: Float, v2: Float): Unit = ???

  override def glRotatef(v: Float, v1: Float, v2: Float, v3: Float): Unit = ???

  override def glScalef(v: Float, v1: Float, v2: Float): Unit = ???

  override def glOrthof(v: Float, v1: Float, v2: Float, v3: Float, v4: Float, v5: Float): Unit = ???

  override def glFrustumf(v: Float, v1: Float, v2: Float, v3: Float, v4: Float, v5: Float): Unit = ???
}
