package org.akka.essentials.java.future.example;

import akka.actor.*;

public class TestActorSystem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("FutureUsageExample");
		ActorRef processOrder = _system.actorOf(new Props(
				new UntypedActorFactory() {
					@Override
					public Actor create() throws Exception {
						return new ProcessOrderActor();
					}
				}));
		processOrder.tell(Integer.valueOf(456));

		Thread.sleep(5000);

		_system.shutdown();
	}

}
