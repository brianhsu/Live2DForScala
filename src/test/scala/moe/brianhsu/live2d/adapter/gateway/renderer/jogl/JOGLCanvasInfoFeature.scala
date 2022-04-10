package moe.brianhsu.live2d.adapter.gateway.renderer.jogl

import com.jogamp.opengl.awt.GLCanvas
import org.scalamock.scalatest.MixedMockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class JOGLCanvasInfoFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MixedMockFactory {
  Feature("Get canvas information") {
    Scenario("Get canvas information from Java OpenGL AWT Canvas") {
      Given("A stubbed Java OpenGL AWT Canvas")
      val canvas = new GLCanvas {
        override def getWidth: Int = 123
        override def getHeight: Int = 456
        override def getSurfaceWidth: Int = 789
        override def getSurfaceHeight: Int = 987
      }

      And("A JOGLCanvasInfo based on that canvas")
      val canvasInfo = new JOGLCanvasInfo(canvas)

      Then("it should have correct properties")
      canvasInfo.currentCanvasWidth shouldBe 123
      canvasInfo.currentCanvasHeight shouldBe 456
      canvasInfo.currentSurfaceWidth shouldBe 789
      canvasInfo.currentSurfaceHeight shouldBe 987
    }
  }

}
