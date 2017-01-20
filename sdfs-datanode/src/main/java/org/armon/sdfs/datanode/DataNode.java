/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.ConfigUtils;
import org.armon.sdfs.common.SdfsConstants;
import org.armon.sdfs.protocol.HeartBeatProtos.HeartBeatRequest;
import org.armon.sdfs.protocol.HeartBeatProtos.HeartBeatResponse;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;

public class DataNode {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		LOGGER.info("DataNode starting...");
		Config akkaConfig = ConfigUtils.getAkkaConfig("akka-datanode.conf");
		ActorSystem actorSystem = null;
		try {
			actorSystem = ActorSystem.create(SdfsConstants.DATANODE_AKKA_SYSTEM_NAME, akkaConfig);

			initActors(actorSystem);

			LOGGER.info("DataNode started.");
		} catch (Exception e) {
			LOGGER.error("DataNode start error", e);
			Throwables.propagate(e);
			if (actorSystem != null) {
				actorSystem.terminate();
			}
			System.exit(1);
		}
	}

	private static void initActors(ActorSystem actorSystem) {
		String heartbeatAkkaPath = ConfigUtils.getConfig().get(DataNodeConstants.NAMENODE_AKKA_PATH) + "/user/"
				+ SdfsConstants.NAMENODE_AKKA_HEARTBEAT_NAME;
		ActorSelection hearbeatActor = actorSystem.actorSelection(heartbeatAkkaPath);
		actorSystem.scheduler().schedule(Duration.Zero(), Duration.create(5, TimeUnit.SECONDS), new Runnable() {
			@Override
			public void run() {
				HeartBeatRequest request = HeartBeatRequest.newBuilder().setAvailableDisk(100).build();
				try {
					HeartBeatResponse response = (HeartBeatResponse) FutureUtils.awaitResult(hearbeatActor, request,
							30);
					if (!response.getSuccess()) {
						LOGGER.error("send heartbeat failed!", response.getMessage());
					} else {
						LOGGER.debug("send heartbeat success.");
					}
				} catch (TimeoutException e) {
					LOGGER.error("send heartbeat timeout!");
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}, actorSystem.dispatcher());
	}
}
