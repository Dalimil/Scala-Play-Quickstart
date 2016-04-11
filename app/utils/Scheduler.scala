package utils

import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}

import scala.concurrent.duration._

@Singleton
class Scheduler @Inject() (system: ActorSystem) {

  def schedule = { // Could be a parametrized function taking runnable
    import play.api.libs.concurrent.Execution.Implicits.defaultContext // this is required
    system.scheduler.scheduleOnce(8 seconds) { println("hello") } // in 8sec
    system.scheduler.schedule(5 seconds, 10 seconds) ( println ("hi")) // in 5sec, 15sec, 25sec,...
  }
}
