package controllers

import play.api.mvc._

class Application extends Controller {

  def index = Action { implicit request => // Make implicit to be able to use it later
    val myCookie = request.cookies.get("theme") // Option[Cookie]
    val mySession = request.session.get("connected") // Option[String]

    Ok(views.html.index("Your new application is ready")) // Send an html template
      .withHeaders(CACHE_CONTROL -> "max-age=3600") // Add header
      .withCookies(Cookie("theme", "blue")) // Add cookie
      .discardingCookies(DiscardingCookie("skin")) // Remove cookie
      .withSession(request.session + ("connected" -> "user@gmail.com")) // Add to session (can only be String)
      .withSession(request.session - "theme") // Remove from session
  }

  def debug = Action { request =>
    Ok("Got request: "+ request)
  }

  def test(id: Long) = TODO

  def flashDemo = Action { implicit request =>
    val fl = request.flash.get("success")
    if(fl.isDefined){
      Ok(fl.get)
    } else {
      Redirect("/flash").flashing("success" -> "You have been logged in")
    }
  }

  def showUser(name: String) = Action {
    Ok("Hi: "+name)
  }

}
