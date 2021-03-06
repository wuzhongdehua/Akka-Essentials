package org.akka.essentials.supervisor.example2;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import akka.actor.*;
import org.akka.essentials.supervisor.example2.MyActorSystem2.Result;

import scala.concurrent.duration.Duration;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;


public class SupervisorActor2 extends UntypedActor {

	public ActorRef workerActor1;
	public ActorRef workerActor2;

	public SupervisorActor2() {
		workerActor1 = getContext().actorOf(new Props(new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new WorkerActor1();
					}
				}),
				"workerActor1");
		workerActor2 = getContext().actorOf(new Props(new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new WorkerActor2();
					}
				}),
				"workerActor2");
	}

	private static SupervisorStrategy strategy = new AllForOneStrategy(10,
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

	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Result) {
			workerActor1.tell(msg, getSender());
		} else
			workerActor1.tell(msg);
	}
}
