package org.akka.essentials.zeromq.example2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.zeromq.Bind;
import akka.zeromq.Frame;
import akka.zeromq.HighWatermark;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;
import scala.concurrent.duration.FiniteDuration;
import scala.concurrent.impl.ExecutionContextImpl;

public class RouterActor extends UntypedActor {
	public static final Object TICK = "TICK";

	Random random = new Random(3);
	int count = 0;
	Cancellable cancellable;

	ActorRef routerSocket = ZeroMQExtension.get(getContext().system())
			.newRouterSocket(
					new SocketOption[] { new Listener(getSelf()),
							new Bind("tcp://127.0.0.1:1237"),
							new HighWatermark(50000) });
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void preStart() {
		cancellable = getContext()
				.system()
				.scheduler()
				.schedule(FiniteDuration.create(1, TimeUnit.SECONDS),
						FiniteDuration.create(1, TimeUnit.SECONDS), getSelf(), TICK, ExecutionContextImpl.fromExecutor(null, null));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals(TICK)) {

			if (random.nextBoolean() == true) {
				routerSocket.tell(new ZMQMessage(new Frame("A"), new Frame(
						"This is the workload for A")));
			} else {
				routerSocket.tell(new ZMQMessage(new Frame("B"), new Frame(
						"This is the workload for B")));

			}
			count++;
			if (count == 10)
				cancellable.cancel();

		} else if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String replier = new String(m.payload(0));
			String msg = new String(m.payload(1));
			log.info("Received message from {} with mesg -> {}", replier, msg);
		}

	}

}
