/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.util;

import java.io.File;

import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.datanode.DataNodeConstants;

public class DiskUtil {
	public static long getAvailableDisk() {
	    String path = ConfigUtils.getConfig().get(DataNodeConstants.DATANODE_FILE_PATH, "/tmp/sdfs");
		File root = new File(path);
		return root.getFreeSpace();
	}

}
