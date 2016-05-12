package org.akka.essentials.supervisor.example3;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import akka.actor.*;
import scala.concurrent.duration.Duration;
import akka.actor.SupervisorStrategy.Directive;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;


public class SupervisorActor extends UntypedActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	ActorRef workerActor = getContext().actorOf(new Props(new UntypedActorFactory() {
				public Actor create() throws Exception {
					return new WorkerActor();
				}
			}),
			"workerActor");

	ActorRef monitor = getContext().system().actorOf(
			new Props(new UntypedActorFactory() {
				public Actor create() throws Exception {
					return new MonitorActor();
				}
			}), "monitorActor");

	@Override
	public void preStart() {
		monitor.tell(new RegisterWorker(workerActor, self()));
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create("10 second"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof ArithmeticException) {
						return resume();
					} else if (t instanceof NullPointerException) {
						return restart();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Result) {
			workerActor.tell(o, getSender());
		} else if (o instanceof DeadWorker) {
			log.info("Got a DeadWorker message, restarting the worker");
			workerActor = getContext().actorOf(new Props(new UntypedActorFactory() {
						public Actor create() throws Exception {
							return new WorkerActor();
						}
					}),
					"workerActor");
		} else
			workerActor.tell(o);
	}

	public ActorRef getWorker() {
		return workerActor;
	}
}
