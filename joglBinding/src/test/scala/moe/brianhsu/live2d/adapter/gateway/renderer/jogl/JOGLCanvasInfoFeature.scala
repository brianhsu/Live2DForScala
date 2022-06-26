package moe.brianhsu.live2d.adapter.gateway.renderer.jogl

import com.jogamp.opengl.awt.GLCanvas
import org.scalamock.scalatest.MixedMockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.awt.GraphicsConfiguration
import java.awt.geom.AffineTransform

class JOGLCanvasInfoFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MixedMockFactory {
  Feature("Get canvas information") {
    Scenario("Get canvas information from Java OpenGL AWT Canvas") {
      Given("a stubbed Java OpenGL AWT Canvas")
      val stubbedConfiguration = stub[GraphicsConfiguration]
      val transform = stub[AffineTransform]

      (() => stubbedConfiguration.getDefaultTransform).when().returns(transform)
      (() => transform.getScaleX).when().returns(1.5f)
      (() => transform.getScaleY).when().returns(2.5f)

      val canvas = new GLCanvas {
        override def getWidth: Int = 123
        override def getHeight: Int = 456
        override def getGraphicsConfiguration: GraphicsConfiguration = stubbedConfiguration
      }

      And("a JOGLCanvasInfo based on that canvas")
      val canvasInfo = new JOGLCanvasInfoReader(canvas)

      Then("it should have correct properties")
      canvasInfo.currentCanvasWidth shouldBe 123
      canvasInfo.currentCanvasHeight shouldBe 456
      canvasInfo.currentSurfaceWidth shouldBe 184
      canvasInfo.currentSurfaceHeight shouldBe 1140
    }
  }

}
