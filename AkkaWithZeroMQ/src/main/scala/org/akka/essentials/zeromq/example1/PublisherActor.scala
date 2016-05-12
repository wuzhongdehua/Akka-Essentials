package org.akka.essentials.zeromq.example1
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.zeromq.Bind
import akka.zeromq.SocketType
import akka.zeromq.ZeroMQExtension
import akka.zeromq._
import akka.actor.Cancellable

import scala.concurrent.duration._

case class Tick

class PublisherActor extends Actor with ActorLogging {
  val pubSocket = ZeroMQExtension(context.system).newSocket(SocketType.Pub, Bind("tcp://127.0.0.1:1234"))
  var count = 0
  var cancellable:Cancellable = null
  import scala.concurrent.ExecutionContext.Implicits.global
  override def preStart() {
    cancellable = context.system.scheduler.schedule(FiniteDuration(1, SECONDS), FiniteDuration(1, SECONDS), self, Tick)
  }
  def receive: Receive = {
    case Tick =>
      count += 1
      var payload = "This is the workload " + count;
      pubSocket ! ZMQMessage(Seq(Frame("someTopic"), Frame(payload)))
      if(count == 10){
      	cancellable.cancel()
      }
  }
}