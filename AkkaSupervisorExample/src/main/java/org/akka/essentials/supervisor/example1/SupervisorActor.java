package org.akka.essentials.supervisor.example1;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import akka.actor.*;
import org.akka.essentials.supervisor.example1.MyActorSystem.Result;

import scala.concurrent.duration.Duration;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;


public class SupervisorActor extends UntypedActor {

	public ActorRef childActor;

	public SupervisorActor() {
		childActor = getContext().actorOf(new Props(new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new WorkerActor();
					}
				}),
				"workerActor");
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
			childActor.tell(o, getSender());
		} else
			childActor.tell(o);
	}
}
