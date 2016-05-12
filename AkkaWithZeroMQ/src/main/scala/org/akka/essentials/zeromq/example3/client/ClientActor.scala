package org.akka.essentials.zeromq.example3.client
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.zeromq.Connect
import akka.zeromq.Frame
import akka.zeromq.Listener
import akka.zeromq.SocketType
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension
import akka.actor.Cancellable

import scala.concurrent.duration._

case class Tick

class ClientActor extends Actor with ActorLogging {
  val reqSocket = ZeroMQExtension(context.system).newSocket(SocketType.Req, Connect("tcp://127.0.0.1:1234"), Listener(self))

  var count = 0
  var cancellable: Cancellable = null
  import scala.concurrent.ExecutionContext.Implicits.global
  override def preStart() {
    cancellable = context.system.scheduler.schedule(FiniteDuration(1, SECONDS), FiniteDuration(1, SECONDS), self, Tick)
  }

  def receive: Receive = {
    case Tick =>
      count += 1
      var payload = "Hi there! (" + context.self.hashCode() + ")->"
      reqSocket ! ZMQMessage(Seq(Frame(payload)))
      if (count == 5) {
        cancellable.cancel()
      }
    case m: ZMQMessage =>
      var mesg = new String(m.payload(0))

      log.info("recieved msg! {}", mesg)
  }
}