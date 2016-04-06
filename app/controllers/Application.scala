package controllers

import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready"))
  }

}
