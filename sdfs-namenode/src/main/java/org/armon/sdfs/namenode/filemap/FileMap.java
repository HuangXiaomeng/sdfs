/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.filemap;

import java.util.Set;

import org.armon.sdfs.common.DataNodeKey;

public interface FileMap {
	Set<DataNodeKey> get(String filename);

	void addDataNode(String fileName, DataNodeKey node);
}
