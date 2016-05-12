package org.akka.essentials.unittest.example;

import static akka.pattern.Patterns.ask;

import akka.actor.Actor;
import akka.actor.UntypedActorFactory;
import junit.framework.Assert;

import org.akka.essentials.unittest.actors.TickTock;
import org.akka.essentials.unittest.actors.TickTock.Tick;
import org.akka.essentials.unittest.actors.TickTock.Tock;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import scala.concurrent.Await;
import scala.concurrent.duration.*;

import com.typesafe.config.ConfigFactory;

import java.util.concurrent.TimeUnit;

public class TickTockTest {
	static ActorSystem _system = ActorSystem.create("TestSys", ConfigFactory
			.load().getConfig("TestSys"));

	@Test
	public void testOne() {
		TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(
				new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new TickTock();
					}
				}), _system);

		// get access to the underlying actor object
		TickTock actor = actorRef.underlyingActor();
		// access the methods the actor object and directly pass arguments and
		// test
		actor.tock(new Tock("tock something"));
		Assert.assertTrue(actor.state);

	}

	@Test
	public void testTwo() throws Exception {
		TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(
				new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new TickTock();
					}
				}), _system);

		String result = (String) Await.result(ask(actorRef, new Tick("msg"), 5000),
				FiniteDuration.create(5, TimeUnit.SECONDS));

		Assert.assertEquals("processed the tick message", result);
	}

	@Test
	public void testThree() throws Exception {
		TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(
				new UntypedActorFactory() {
					public Actor create() throws Exception {
						return new TickTock();
					}
				}), _system);
		try {
			actorRef.receive("do something");
			//should not reach here
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals(e.getMessage(), "boom!");
		}
	}
	
}
