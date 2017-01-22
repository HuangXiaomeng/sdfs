/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.filemap;

import org.armon.sdfs.common.DataNodeKey;

public interface FileMap {
	DataNodeKey get(String filename);
}
