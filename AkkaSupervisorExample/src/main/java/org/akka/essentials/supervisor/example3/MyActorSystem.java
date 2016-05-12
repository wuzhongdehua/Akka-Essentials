package org.akka.essentials.supervisor.example3;

import akka.actor.*;

public class MyActorSystem {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("faultTolerance");

		ActorRef supervisor = system.actorOf(new Props(new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new SupervisorActor();
					}
				}),
				"supervisor");

		supervisor.tell(Integer.valueOf(10));
		supervisor.tell("10");

		Thread.sleep(5000);

		supervisor.tell(Integer.valueOf(10));

		system.shutdown();
	}

}
