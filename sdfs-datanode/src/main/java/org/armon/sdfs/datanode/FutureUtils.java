/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class FutureUtils {

	public static Object awaitResult(ActorRef ref, Object msg, long seconds) throws Exception {
		Timeout timeout = new Timeout(Duration.create(seconds, TimeUnit.SECONDS));
		Future<Object> future = Patterns.ask(ref, msg, timeout);
		return Await.result(future, timeout.duration());
	}

	/**
	 * Send msg to ActorRef, wait the response up to seconds
	 *
	 * @param selection
	 *            the actorRef this msg sent to
	 * @param msg
	 *            msg to be sent
	 * @param seconds
	 *            the maximum waiting time
	 * @return The object wrapped in Future.
	 * @throws Exception
	 */
	public static Object awaitResult(ActorSelection selection, Object msg, long seconds) throws Exception {
		Timeout timeout = new Timeout(Duration.create(seconds, TimeUnit.SECONDS));
		Future<Object> future = Patterns.ask(selection, msg, timeout);
		return Await.result(future, timeout.duration());
	}
}
