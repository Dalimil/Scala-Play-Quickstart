package utils

import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/** Simple scheduler of repeated (or one-off) tasks */

@Singleton
class Scheduler @Inject() (implicit ec: ExecutionContext, system: ActorSystem) {

  def schedule = { // Could be a parametrized function taking runnable
    system.scheduler.scheduleOnce(8 seconds) { println("hello") } // in 8sec
    system.scheduler.schedule(5 seconds, 10 seconds) ( println ("hi")) // in 5sec, 15sec, 25sec,...
  }
}
