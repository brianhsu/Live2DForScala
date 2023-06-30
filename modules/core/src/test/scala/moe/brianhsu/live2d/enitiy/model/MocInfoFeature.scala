package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.live2d.exception.MocNotRevivedException
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class MocInfoFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Error handling of uninitialized memory") {
    Scenario("Try to access revivedMoc from an uninitialized memory") {
      Given("a MocInfo with an uninitialized memory")
      val core = new JnaNativeCubismAPILoader()
      val memoryInfo = JnaMemoryAllocator.allocate(1024, MocAlignment)
      val mocInfo = MocInfo(memoryInfo, 1024)(core)

      When("access the revivedMoc field")
      Then("it should throw MocNotRevivedException")
      a[MocNotRevivedException] shouldBe thrownBy {
        mocInfo.revivedMoc
      }
    }
  }
}
