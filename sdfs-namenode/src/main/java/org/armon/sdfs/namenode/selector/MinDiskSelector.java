/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

import java.util.Map;
import java.util.Map.Entry;

import org.armon.sdfs.common.DataNodeInfo;
import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.NodeManager;

public class MinDiskSelector implements NodeSelector {

	private NodeManager nodeManager = NodeManager.INSTANCE;

	@Override
	public DataNodeKey select() {
		Map<DataNodeKey, DataNodeInfo> onlineNodesInfo = nodeManager.getOnlineNodesInfo();
		int maxAvailableDisk = 0;
		DataNodeKey select = null;
		for (Entry<DataNodeKey, DataNodeInfo> entry : onlineNodesInfo.entrySet()) {
			if (entry.getValue().getAvailable_disk() > maxAvailableDisk) {
				select = entry.getKey();
				maxAvailableDisk = entry.getValue().getAvailable_disk();
			}
		}
		return select;
	}

}
