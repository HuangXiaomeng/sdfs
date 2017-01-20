/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

import java.util.List;
import java.util.Random;

import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.namenode.NodeManager;

public class RandomSelector implements NodeSelector {
	private static RandomSelector instance = new RandomSelector();
    private RandomSelector() {}
    public static RandomSelector getInstance() {
        return instance;
    }

	private NodeManager nodeManager = NodeManager.INSTANCE;

	@Override
	public DataNodeKey select() {
		List<DataNodeKey> nodeKeys = nodeManager.getOnlineNodes();
		int size = nodeKeys.size();
		Random random = new Random();
		int index = random.nextInt(size - 1);
		return nodeKeys.get(index);
	}

}
