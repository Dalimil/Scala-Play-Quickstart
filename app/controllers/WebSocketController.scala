package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.iteratee._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class WebSocketController @Inject() (implicit ec: ExecutionContext) extends Controller {

  def index = Action {
    Ok(views.html.sockets_example()).withSession(("username", java.util.UUID.randomUUID().toString))
  }

  def debug = Action {
    pushAll
    Ok("done")
  }

  /** global channel for everyone - not used at the moment */
  val (broadcast, globalChannel) = Concurrent.broadcast[String] // returns (Enumerator, Concurrent.Channel)

  /** every user has his own channel */
  var users = Map[String, (Enumerator[String], Concurrent.Channel[String])]()

  /** users can be grouped into rooms - todo */
  var groups = Map[String, Set[String]]()

  def pushAll = users.foreach(x => x._2._2.push("Hello - " + x._1)) // globalChannel.push("Hello everyone")

  def pushOne(username: String) = users(username)._2.push("Hello "+ username)

  /** GET endpoint (is automatically upgraded to WS) - can have multiple of these */
  def ws = WebSocket.using[String] { implicit request =>
    val username = request.session.get("username").get // or uuid previously stored in session

    if(users.contains(username)) { // existing user
      (Iteratee.ignore[String], users(username)._1) // (in, out)
    } else {
      // create a new private channel
      val (enumerator, channel) = Concurrent.broadcast[String] // only broadcasts for people using the same channel
      users += ((username, (enumerator, channel)))

      val in = Iteratee.foreach[String]( msg => { // do something with an incoming message
        Logger.info(msg)
        channel.push("I received your message: " + msg)
      }).map{ _ => // WebSocket closed by the user
        users(username)._2.eofAndEnd() // close channel
        users -= username
      }

      (in, enumerator) // always return both like this
    }
  }

}
