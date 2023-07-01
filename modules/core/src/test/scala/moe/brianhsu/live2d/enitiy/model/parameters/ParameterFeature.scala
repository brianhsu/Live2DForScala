package moe.brianhsu.live2d.enitiy.model.parameters

import moe.brianhsu.live2d.enitiy.model.parameter.{JavaVMParameter, ParameterType}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ParameterFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("Update parameter value with default weight = 1.0") {
    Scenario("The new value is inside [min, max]") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      When("update the value to 15")
      parameter.update(15)

      Then("the parameter value should be 15")
      parameter.current shouldBe 15
    }

    Scenario("The new value is under [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      When("update the value to 5")
      parameter.update(5)

      Then("the new value should equal to min value, which is 10")
      parameter.current shouldBe 10
    }

    Scenario("The new value is over [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", ParameterType.Normal, max = 20)

      When("update the value to 100")
      parameter.update(100)

      Then("the new value should equal to max value, which is 20")
      parameter.current shouldBe 20
    }

  }

  Feature("Update parameter value with weight = 0.3") {
    Scenario("The new value is inside [min, max]") {
      Given("a parameter backed by JVM with min=20, max=100")
      val parameter = new JavaVMParameter("parameterId", min = 20, max = 100)

      And("current value is 20")
      parameter.update(20)

      When("update the value to 30 with weight = 0.3")
      parameter.update(30, 0.3f)

      //20 * 0.7 + 30 * 0.3 = 23.0
      Then("the parameter value should be 23.0f")
      parameter.current shouldBe 23.0f
    }

    Scenario("The new value is under [min] value") {
      Given("a parameter backed by JVM with min=20, max=100")
      val parameter = new JavaVMParameter("parameterId", min = 20, max = 100)

      And("current value is 30")
      parameter.update(30)

      When("update the value to 10 with weight = 0.3")
      parameter.update(10, 0.3f)

      //30 * 0.7 + 20 * 0.3 = 27.0
      Then("it will use 20 instead of 10 to calculate new value")
      And("the parameter value should be 23.0f")
      parameter.current shouldBe 27
    }

    Scenario("The new value is over [min] value") {
      Given("a parameter backed by JVM with min=20, max=100")
      val parameter = new JavaVMParameter("parameterId", min = 20, max = 100)

      And("current value is 30")
      parameter.update(30)

      When("update the value to 500 with weight = 0.3")
      parameter.update(500, 0.3f)

      //30 * 0.7 + 100 * 0.3 = 51
      Then("it will use 100 instead of 500 to calculate new value")
      And("the parameter value should be 23.0f")
      parameter.current shouldBe 51
    }

  }

  Feature("Add parameter value with default weight = 1.0") {
    Scenario("The value after addition is inside [min, max]") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with 3")
      parameter.add(3)

      Then("the parameter value should be 18")
      parameter.current shouldBe 18
    }

    Scenario("The value after addition is under [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with -10")
      parameter.add(-10)

      Then("the parameter value should be 10")
      parameter.current shouldBe 10
    }

    Scenario("The value after addition is over [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with 100")
      parameter.add(100)

      Then("the parameter value should be 20")
      parameter.current shouldBe 20
    }

  }

  Feature("Add parameter value with weight = 0.3") {
    Scenario("The value after addition is inside [min, max]") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with 5, weight is 0.3")
      parameter.add(5, 0.3f)

      Then("the parameter value should be 16.5")
      // 15 + 5 * 0.3 = 16.5
      parameter.current shouldBe 16.5
    }

    Scenario("The value after addition is under [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with -100, weight = 0.3")
      parameter.add(-100, 0.3f)

      Then("the parameter value should be 10")
      parameter.current shouldBe 10
    }

    Scenario("The value after addition is over [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("add it with 100, weight = 0.3")
      parameter.add(100, 0.3f)

      Then("the parameter value should be 20")
      parameter.current shouldBe 20
    }

  }

  Feature("Multiply parameter value with default weight = 1.0") {
    Scenario("The value after addition is inside [min, max]") {
      Given("a parameter backed by JVM with min=10, max=100")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with 1.5")
      parameter.multiply(1.2f)

      Then("the parameter value should be 18.0")
      parameter.current shouldBe 18.0
    }

    Scenario("The value after addition is under [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with 0.2")
      parameter.multiply(0.2f)

      Then("the parameter value should be 10")
      parameter.current shouldBe 10
    }

    Scenario("The value after addition is over [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with 10")
      parameter.add(10)

      Then("the parameter value should be 20")
      parameter.current shouldBe 20
    }

  }

  Feature("Multiply parameter value with default weight = 0.3") {
    Scenario("The value after addition is inside [min, max]") {
      Given("a parameter backed by JVM with min=10, max=100")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with 1.5, weight = 0.3")
      parameter.multiply(1.5f, 0.3f)

      Then("the parameter value should be 17.25")
      // current * (1.0f + (value - 1.0f) * weight)
      // 15 * (1.0f + (1.5 - 1.0f) * 0.3) = 17.25
      parameter.current shouldBe 17.25
    }

    Scenario("The value after addition is under [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with -1.5, weight = 0.3")
      parameter.multiply(-1.5f, 0.3f)

      Then("the parameter value should be 10")
      // current * (1.0f + (value - 1.0f) * weight)
      // 15 * (1.0f + (1.5 - 1.0f) * 0.3) = 17.25, less then 10
      parameter.current shouldBe 10
    }

    Scenario("The value after addition is over [min] value") {
      Given("a parameter backed by JVM with min=10, max=20")
      val parameter = new JavaVMParameter("parameterId", min = 10, max = 20)

      And("the current value is 15")
      parameter.update(15)

      When("multiply it with 10, weight = 0.3")
      parameter.multiply(100, 0.3f)

      Then("the parameter value should be 20")
      // current * (1.0f + (value - 1.0f) * weight)
      // 15 * (1.0f + (100 - 1.0f) * 0.3) = 460.5, grater then 20
      parameter.current shouldBe 20
    }

  }

}
