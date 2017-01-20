/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

public class NodeSelectorFactory {
	public static NodeSelector getSelector() {
		//TODO
		return MinDiskSelector.getInstance();
	}
}
