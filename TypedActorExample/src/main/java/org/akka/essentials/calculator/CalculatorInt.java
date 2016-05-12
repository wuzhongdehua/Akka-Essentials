package org.akka.essentials.calculator;

import akka.japi.Option;
import scala.concurrent.Future;

public interface CalculatorInt {

	public Future<Integer> add(Integer first, Integer second);

	public Future<Integer> subtract(Integer first, Integer second);

	public void incrementCount();

	public Option<Integer> incrementAndReturn();

}
