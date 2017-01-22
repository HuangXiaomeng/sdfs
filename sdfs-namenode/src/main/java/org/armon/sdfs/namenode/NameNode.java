/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.SdfsConstants;
import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.namenode.actor.FileActor;
import org.armon.sdfs.namenode.actor.HeartBeatActor;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;

public class NameNode {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		LOGGER.info("Namenode starting...");
		Config akkaConfig = ConfigUtils.getAkkaConfig("akka-namenode.conf");
		ActorSystem actorSystem = null;
		try {
			actorSystem = ActorSystem.create(SdfsConstants.NAMENODE_AKKA_SYSTEM_NAME, akkaConfig);

			initActors(actorSystem);

			LOGGER.info("Namenode started.");
		} catch (Exception e) {
			LOGGER.error("NameNode start error", e);
			Throwables.propagate(e);
			if (actorSystem != null) {
				actorSystem.terminate();
			}
			System.exit(1);
		}

	}

	private static void initActors(ActorSystem actorSystem) {
		int heartbeatActorNum = ConfigUtils.getConfig().getInt(NameConstants.NAMENODE_HEARTBEAT_ACTOR_NUM, 10);
		actorSystem.actorOf(HeartBeatActor.props().withRouter(new RoundRobinPool(heartbeatActorNum)), SdfsConstants.NAMENODE_AKKA_HEARTBEAT_NAME);
		int fileActorNum = ConfigUtils.getConfig().getInt(NameConstants.NAMENODE_FILE_ACTOR_NUM, 10);
		actorSystem.actorOf(FileActor.props().withRouter(new RoundRobinPool(fileActorNum)),
				SdfsConstants.NAMENODE_AKKA_FILE_NAME);
	}
}
