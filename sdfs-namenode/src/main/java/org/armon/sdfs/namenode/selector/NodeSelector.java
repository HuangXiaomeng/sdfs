/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

import org.armon.sdfs.common.DataNodeKey;

public interface NodeSelector {

	DataNodeKey select();

}
