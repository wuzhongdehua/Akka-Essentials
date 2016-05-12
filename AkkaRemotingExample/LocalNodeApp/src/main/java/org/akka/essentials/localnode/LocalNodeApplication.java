package org.akka.essentials.localnode;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 * 
 */
public class LocalNodeApplication {
	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("LocalNodeApp",ConfigFactory
				.load().getConfig("LocalSys"));
		ActorRef localActor = _system.actorOf(new Props(new UntypedActorFactory() {
			public Actor create() throws Exception {
				return new LocalActor();
			}
		}));
		localActor.tell("Hello");

		Thread.sleep(5000);
		_system.shutdown();
	}
}
