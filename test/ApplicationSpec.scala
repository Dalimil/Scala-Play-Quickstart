import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * You can mock out a whole application including requests, plugins etc.
  * https://playframework.com/documentation/2.5.x/ScalaTestingWithScalaTest
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {
    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/xyz")).map(status(_)) mustBe Some(NOT_FOUND)
    }
  }

  "Application controller" should {
    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("<body>")
    }
  }

}
