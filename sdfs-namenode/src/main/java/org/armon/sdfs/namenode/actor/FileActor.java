/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.actor;

import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.filemap.FileMap;
import org.armon.sdfs.namenode.filemap.FileMapFactory;
import org.armon.sdfs.namenode.selector.NodeSelector;
import org.armon.sdfs.namenode.selector.NodeSelectorFactory;
import org.armon.sdfs.protocol.FileProtos.DataNodeEntry;
import org.armon.sdfs.protocol.FileProtos.File2DataNodeRequest;
import org.armon.sdfs.protocol.FileProtos.File2DataNodeResponse;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileActor extends UntypedActor {
	private FileMap fileMap = FileMapFactory.getFileMap();
	private NodeSelector selector = NodeSelectorFactory.getSelector();

	public static Props props() {
		return Props.create(FileActor.class);
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj instanceof File2DataNodeRequest) {
			File2DataNodeRequest request = (File2DataNodeRequest) obj;
			String filename = request.getFileName();
			DataNodeKey nodeKey = fileMap.get(filename);
			if (nodeKey == null) {
				nodeKey = selector.select();
			}

			File2DataNodeResponse response;
			if (nodeKey == null) {
				response = File2DataNodeResponse.newBuilder()
						.setSuccess(false)
						.setMessage("can't find a datanode of filename=" + filename + " and a new datanode")
						.build();
			} else {
				DataNodeEntry nodeEntry = DataNodeEntry.newBuilder()
						.setIp(nodeKey.getIp())
						.setPort(nodeKey.getPort())
						.build();
				response = File2DataNodeResponse.newBuilder()
						.setSuccess(true)
						.setNodeEntry(nodeEntry)
						.build();
			}
			getSender().tell(response, getSelf());
		} else {
			unhandled(obj);
		}
	}

}
