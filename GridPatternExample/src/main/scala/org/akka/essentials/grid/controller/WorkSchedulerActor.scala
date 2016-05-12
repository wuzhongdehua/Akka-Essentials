package org.akka.essentials.grid.controller
import akka.actor.Actor
import akka.actor.ActorLogging

import scala.concurrent.duration._

class WorkSchedulerActor extends Actor with ActorLogging {
  import scala.concurrent.ExecutionContext.Implicits.global

  def receive: Receive = {
    case value:Int =>
      val cancellable = context.system.scheduler.scheduleOnce(value.milliseconds, sender,  SendWork)
    case _ =>
      log.info("Received work sending request")
      val cancellable = context.system.scheduler.scheduleOnce(6000 milliseconds, sender,  SendWork)
  }
}