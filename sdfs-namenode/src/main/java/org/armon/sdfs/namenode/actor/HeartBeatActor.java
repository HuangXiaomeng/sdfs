/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.DataNodeInfo;
import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.NodeManager;
import org.armon.sdfs.protocol.HeartBeatProtos.HeartBeatRequest;
import org.armon.sdfs.protocol.HeartBeatProtos.HeartBeatResponse;

import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HeartBeatActor extends UntypedActor {
	private static final Logger LOGGER = LogManager.getLogger("heartbeat");
	private NodeManager nodeManager = NodeManager.INSTANCE;

	public static Props props() {
		return Props.create(HeartBeatActor.class);
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj instanceof HeartBeatRequest) {
			HeartBeatRequest request = (HeartBeatRequest) obj;
			Address address = getSender().path().address();
			String ip = address.host().get();
			int port = Integer.parseInt(address.port().get().toString());
			LOGGER.info("receive HeartBeatRequest from [{}:{}]", ip, port);
			nodeManager.update(new DataNodeKey(ip, port), new DataNodeInfo(request.getAvailableDisk()));
			HeartBeatResponse response = HeartBeatResponse.newBuilder().setSuccess(true).build();
			getSender().tell(response, getSelf());
		} else {
			unhandled(obj);
		}
	}

}
