package controllers

import org.scalatestplus.play.OneAppPerTest
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._


class HomeControllerTest extends PlaySpec with OneAppPerTest {
  "HomeController" should {

    "return a status health check" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }
}
