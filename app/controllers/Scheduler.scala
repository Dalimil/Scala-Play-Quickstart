package controllers

import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}
import play.api.mvc.Controller

@Singleton
class Scheduler @Inject() (system: ActorSystem) extends Controller {

  def schedule = {
    import scala.concurrent.duration._
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    system.scheduler.schedule(5 seconds, 10 seconds) ( println ("hi")) // 5, 15, 25,...
    system.scheduler.scheduleOnce(5 seconds) { println("hello") }

  }
}
