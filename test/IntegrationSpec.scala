import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
class IntegrationSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {

  "Application" should {
    "work from within a browser" in {
      go to ("http://localhost:" + port)
      pageSource must include ("button")
      println(find(id("myButton")))
      println(find(name("name")))
      click on find(id("myButton")).value
      eventually { pageSource must include ("<body") }
    }
  }
}
