/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.armon.sdfs.common.DataNodeInfo;
import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.common.DataNodeStatus;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum NodeManager {
	INSTANCE;

	public static final int MAX_HEART_BEAT_TIMEOUT_SECONDS = 15;
	public static Cache<DataNodeKey, DataNodeInfo> NODES = CacheBuilder.newBuilder()
			.expireAfterWrite(MAX_HEART_BEAT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
			.build();

	public void update(DataNodeKey key, DataNodeInfo info) {
		DataNodeInfo oldInfo = NODES.getIfPresent(key);
		if (oldInfo != null) {
			info.setStatus(oldInfo.getStatus());
			NODES.put(key, info);
		} else {
			info.setStatus(DataNodeStatus.ONLINE);
			NODES.put(key, info);
		}
	}

	public void offline(DataNodeKey key) {
		DataNodeInfo nodeInfo = NODES.getIfPresent(key);
		if (nodeInfo != null) {
			nodeInfo.setStatus(DataNodeStatus.OFFLINE);
			NODES.put(key, nodeInfo);
		}
	}

	public void online(DataNodeKey key) {
		DataNodeInfo nodeInfo = NODES.getIfPresent(key);
		if (nodeInfo != null) {
			nodeInfo.setStatus(DataNodeStatus.ONLINE);
			NODES.put(key, nodeInfo);
		}
	}

	public List<DataNodeKey> getAllNodes() {
        return Lists.newArrayList(NODES.asMap().keySet());
    }

	public List<DataNodeKey> getOnlineNodes() {
        List<DataNodeKey> onlineNodes = Lists.newArrayList();
        for (Entry<DataNodeKey, DataNodeInfo> entry : NODES.asMap().entrySet()) {
        	if (entry.getValue().getStatus().equals(DataNodeStatus.ONLINE)) {
        		onlineNodes.add(entry.getKey());
        	}
        }
        return onlineNodes;
    }

	public Map<DataNodeKey, DataNodeInfo> getOnlineNodesInfo() {
		Map<DataNodeKey, DataNodeInfo> onlieNodesInfo = Maps.newHashMap();
		for (Entry<DataNodeKey, DataNodeInfo> entry : NODES.asMap().entrySet()) {
        	if (entry.getValue().getStatus().equals(DataNodeStatus.ONLINE)) {
        		onlieNodesInfo.put(entry.getKey(), entry.getValue());
        	}
        }
		return onlieNodesInfo;
	}
}
