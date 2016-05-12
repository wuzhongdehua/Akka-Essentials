package org.akka.essentials.actor;

import akka.actor.*;
import org.akka.essentials.data.OperationCmd;
import org.akka.essentials.data.Operator;

public class SnapshotExample {

	public static void main(String... args) throws Exception {
		final ActorSystem system = ActorSystem.create();

		final ActorRef calculationActor = system.actorOf(
				Props.create(new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new CalculationActor();
					}
				}), "calculationActor-java");

		calculationActor.tell(new OperationCmd(Operator.ADD, 5), null);
		calculationActor.tell(new OperationCmd(Operator.ADD, 7), null);
		calculationActor.tell("snap", null);
		calculationActor.tell(new OperationCmd(Operator.SUBTRACT, 6), null);
		calculationActor.tell(new OperationCmd(Operator.MULTIPLY, 3), null);
		calculationActor.tell("print", null);

		Thread.sleep(1000);
		system.shutdown();
	}
}