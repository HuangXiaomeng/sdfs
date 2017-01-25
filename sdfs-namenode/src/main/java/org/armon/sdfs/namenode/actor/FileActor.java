/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.actor;

import java.util.Set;

import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.NodeManager;
import org.armon.sdfs.namenode.filemap.FileMap;
import org.armon.sdfs.namenode.filemap.FileMapFactory;
import org.armon.sdfs.namenode.selector.NodeSelector;
import org.armon.sdfs.namenode.selector.NodeSelectorFactory;
import org.armon.sdfs.protocol.FileProtos.DataNodeEntry;
import org.armon.sdfs.protocol.FileProtos.FindDataNodeForReadRequest;
import org.armon.sdfs.protocol.FileProtos.FindDataNodeForWriteRequest;
import org.armon.sdfs.protocol.FileProtos.FindDataNodeResponse;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileActor extends UntypedActor {
	private FileMap fileMap = FileMapFactory.getFileMap();
	private NodeSelector selector = NodeSelectorFactory.getSelector();
	private NodeManager nodeManager = NodeManager.INSTANCE;

	public static Props props() {
		return Props.create(FileActor.class);
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj instanceof FindDataNodeForWriteRequest) {
		    findDataNode4Write((FindDataNodeForWriteRequest) obj);
		} else if (obj instanceof FindDataNodeForReadRequest) {
		    findDataNode4Read((FindDataNodeForReadRequest) obj);
		} else {
			unhandled(obj);
		}
	}

	private void findDataNode4Write(FindDataNodeForWriteRequest request) {
        FindDataNodeResponse response = null;
        String filename = request.getFileName();
        Set<DataNodeKey> nodeSet = fileMap.get(filename);
        DataNodeKey nodeKey = null;
        if (!nodeSet.isEmpty()) {
            for (DataNodeKey datanode : nodeSet) {
                nodeManager.getAllNodes().contains(datanode);
                nodeKey = datanode;
                break;
            }
            if (nodeKey == null) {
                response = FindDataNodeResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("datanode " + nodeSet + " are all unavailable!")
                        .build();
                getSender().tell(response, getSelf());
            }
        } else {
            nodeKey = selector.select();
            if (nodeKey == null) {
                response = FindDataNodeResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("There is no available datanode")
                        .build();
                getSender().tell(response, getSelf());
            }
        }

        if (nodeKey != null) {
            DataNodeEntry nodeEntry = DataNodeEntry.newBuilder()
                    .setIp(nodeKey.getIp())
                    .setPort(nodeKey.getPort())
                    .build();
            response = FindDataNodeResponse.newBuilder()
                    .setSuccess(true)
                    .setNodeEntry(nodeEntry)
                    .build();
        }
        getSender().tell(response, getSelf());
	}

	private void findDataNode4Read(FindDataNodeForReadRequest request) {
	    FindDataNodeResponse response;
	    String fileName = request.getFileName();
	    Set<DataNodeKey> nodeSet = fileMap.get(fileName);
	    DataNodeKey nodeKey = null;
	    if (!nodeSet.isEmpty()) {
	        for (DataNodeKey datanode : nodeSet) {
                nodeManager.getAllNodes().contains(datanode);
                nodeKey = datanode;
                break;
            }
            if (nodeKey == null) {
                response = FindDataNodeResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("datanode " + nodeSet + " are all unavailable!")
                        .build();
                getSender().tell(response, getSelf());
            }
	    } else {
	        response = FindDataNodeResponse.newBuilder()
	                .setSuccess(false)
	                .setMessage("can't find datanode of file=" + fileName)
	                .build();
	        getSender().tell(response, getSelf());
	    }

	    DataNodeEntry nodeEntry = DataNodeEntry.newBuilder()
                .setIp(nodeKey.getIp())
                .setPort(nodeKey.getPort())
                .build();
        response = FindDataNodeResponse.newBuilder()
                .setSuccess(true)
                .setNodeEntry(nodeEntry)
                .build();
        getSender().tell(response, getSelf());
	}

}
