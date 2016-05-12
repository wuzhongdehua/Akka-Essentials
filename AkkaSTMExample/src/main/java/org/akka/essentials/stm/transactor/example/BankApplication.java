package org.akka.essentials.stm.transactor.example;

import akka.actor.*;
import org.akka.essentials.stm.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example.msg.TransferMsg;

public class BankApplication {
	ActorSystem _system = ActorSystem.apply("STM-Example");
	ActorRef bank = _system.actorOf(new Props(new UntypedActorFactory() {
		public UntypedActor create() {
			return new BankActor();
		}
	}), "BankActor");

	public static void main(String args[]) {

		BankApplication bankApp = new BankApplication();

		bankApp.showBalances();

		bankApp.bank.tell(new TransferMsg(Float.valueOf("1500")));

		bankApp.showBalances();

		bankApp.bank.tell(new TransferMsg(Float.valueOf("1400")));

		bankApp.showBalances();

		bankApp.bank.tell(new TransferMsg(Float.valueOf("3500")));

		bankApp.showBalances();

		bankApp._system.shutdown();

	}

	private void showBalances() {
		try {
			Thread.sleep(2000);
			bank.tell(new AccountBalance("XYZ"));
			bank.tell(new AccountBalance("ABC"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}