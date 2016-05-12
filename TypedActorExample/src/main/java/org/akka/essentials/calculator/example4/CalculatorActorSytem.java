package org.akka.essentials.calculator.example4;

import org.akka.essentials.calculator.Calculator;
import org.akka.essentials.calculator.CalculatorInt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Option;
import akka.util.Timeout;

import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class CalculatorActorSytem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("TypedActorsExample",
				ConfigFactory.load().getConfig("TypedActorExample"));

		Timeout timeout = new Timeout(FiniteDuration.create(5, TimeUnit.SECONDS));

		CalculatorInt calculator = TypedActor.get(_system).typedActorOf(
				new TypedProps<Calculator>(CalculatorInt.class,
						Calculator.class).withDispatcher("defaultDispatcher"));

		// calling a fire and forget method
		calculator.incrementCount();

		// Invoke the method and wait for result
		Future<Integer> future = calculator.add(Integer.valueOf(14),
				Integer.valueOf(6));
		Integer result = Await.result(future, timeout.duration());

		System.out.println("Result is " + result);

		Option<Integer> counterResult = calculator.incrementAndReturn();
		System.out.println("Result is " + counterResult.get());

		counterResult = calculator.incrementAndReturn();
		System.out.println("Result is " + counterResult.get());

		// Get access to the ActorRef
		ActorRef calActor = TypedActor.get(_system).getActorRefFor(calculator);
		// call actor with a message
		calActor.tell("Hi there");
		
		_system.shutdown();

	}

}
