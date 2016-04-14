package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import utils.{HttpClient, Scheduler}

class Application @Inject() (scheduler: Scheduler, httpClient: HttpClient) extends Controller {

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
    Logger.debug("Simple usage of the default logger")
    scheduler.schedule
    val res = httpClient.demo
    Ok("Got request: "+ request +" --- \n " + res)
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
    val jsonVal: JsValue = Json.parse("""{"name": "Watership Down", "location": {"lat" : 51.23, "lng": -1.30}}""")
    val minifiedString: String = Json.stringify(jsonVal)
    val readableString: String = Json.prettyPrint(jsonVal)
    Ok("Hi: "+name+" --- "+readableString)
  }

  // Define a case class to store form data
  case class UserData(name: String, age: Int, email: Option[String])
  // Define the actual form that can map the data to our case class
  val userForm: Form[UserData] = Form(
    mapping(
      "name" -> nonEmptyText, // or 'text'
      "age" -> default(number(min = 0, max = 90), 42), // 42 when omitted
      "email" -> optional(email) // may be be omitted
    )(UserData.apply)(UserData.unapply) // to transform the data into an instance of our case class
  )

  def login() = Action { implicit request =>
    val userData = userForm.bindFromRequest.get // UserData object - request is implicit
    println(userData.name +" " + userData.age)
    Redirect(routes.Application.showUser(userData.name))
  }

  def fileUpload = Action(parse.multipartFormData) { request =>
      val picture = request.body.file("profilePicture").get
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new java.io.File(s"/tmp/pictures/$filename"))
      Ok("File uploaded")
  }

}

