/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

import java.util.List;

import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.NodeManager;


public class RoundRobinSelector implements NodeSelector {
	private static RoundRobinSelector instance = new RoundRobinSelector();
    private RoundRobinSelector() {}
    public static RoundRobinSelector getInstance() {
        return instance;
    }

	private NodeManager nodeManager = NodeManager.INSTANCE;
	private int index = 0;

	@Override
	public synchronized DataNodeKey select() {
		List<DataNodeKey> nodeKeys = nodeManager.getOnlineNodes();
		int size = nodeKeys.size();
		if (size == 0) {
			return null;
		} else if (index >=size) {
			index = index % size;
		}
		DataNodeKey select = nodeKeys.get(index);
		index++;
		return select;
	}

}
