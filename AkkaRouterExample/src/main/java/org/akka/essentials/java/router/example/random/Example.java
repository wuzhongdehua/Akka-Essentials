package org.akka.essentials.java.router.example.random;

import akka.actor.*;
import org.akka.essentials.java.router.example.MsgEchoActor;

import akka.routing.RandomRouter;

public class Example {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RandomRouterExample");
		ActorRef randomRouter = _system.actorOf(new Props(new UntypedActorFactory(){
			@Override
			public Actor create() throws Exception {
				return new MsgEchoActor();
			}
		})
				.withRouter(new RandomRouter(5)),"myRandomRouterActor");

		for (int i = 1; i <= 10; i++) {
			//sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}

}
