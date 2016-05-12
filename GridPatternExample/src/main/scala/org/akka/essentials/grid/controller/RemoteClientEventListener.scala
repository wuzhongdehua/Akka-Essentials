package org.akka.essentials.grid.controller
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import akka.remote.RemoteClientError
import akka.remote.RemoteClientConnected
import akka.remote.RemoteClientDisconnected
import akka.remote.RemoteClientStarted
import akka.remote.RemoteClientShutdown

class RemoteClientEventListener(val jobScheduler: ActorRef) extends Actor with ActorLogging {

  def receive: Receive = {
    case event: RemoteClientError =>
      log.info("Received remote client error event from address "
        + event.getRemoteAddress);
      jobScheduler ! new StopWorker(event.getRemoteAddress.toString);
      log.info("Cause of the event was {}", event.getCause());
    case event: RemoteClientShutdown =>
      log.info("Received remote client shutdown event from address-> "
        + event.getRemoteAddress.toString);
      jobScheduler ! new StopWorker(event.getRemoteAddress.toString);
  }
}