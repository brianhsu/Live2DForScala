package moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl

import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Rectangle
import org.eclipse.swt.opengl.{GLCanvas, GLData}
import org.eclipse.swt.widgets.Shell
import org.scalamock.scalatest.MixedMockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class SWTOpenGLCanvasInfoFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MixedMockFactory {
  Feature("Get canvas information") {
    Scenario("Get canvas information from SWT OpenGL Canvas") {
      Given("a stubbed SWT OpenGL Canvas")
      val canvas = new GLCanvas(new Shell(), SWT.NONE, stub[GLData]) {
        override def getBounds: Rectangle = new Rectangle(123, 456, 789, 987)
      }

      And("a SWTOpenGLCanvasInfo based on that canvas")
      val canvasInfo = new SWTOpenGLCanvasInfoReader(canvas)

      Then("it should have correct properties")
      canvasInfo.currentCanvasWidth shouldBe 789
      canvasInfo.currentCanvasHeight shouldBe 987
      canvasInfo.currentSurfaceWidth shouldBe 789
      canvasInfo.currentSurfaceHeight shouldBe 987
    }
  }

}
