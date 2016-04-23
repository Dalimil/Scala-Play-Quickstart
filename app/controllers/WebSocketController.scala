package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.libs.iteratee._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class WebSocketController @Inject() (implicit ec: ExecutionContext) extends Controller {

  def index = Action {
    Ok(views.html.sockets_example())
  }

  // Concurrent.broadcast returns (Enumerator, Concurrent.Channel)
  val (out, channel) = Concurrent.broadcast[String]

  /** GET endpoint (is automatically upgraded to WS) - can have multiple of these */
  def ws = WebSocket.using[String] { request =>
    // Enumerator and channel could also be created here (different for each user)

    // log the received message to stdout and send response back to client
    val in = Iteratee.foreach[String] {
      msg => Logger.info(msg)
        // the Enumerator returned by Concurrent.broadcast subscribes to the channel and will
        // receive the pushed messages
        channel.push("I received your message: " + msg)
    }

    (in,out) // always return both like this
  }

}
