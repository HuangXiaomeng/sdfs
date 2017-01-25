/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.filemap;

import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.namenode.NameConstants;

public class FileMapFactory {
    private static final Logger LOGGER = LogManager.getLogger();

	public static FileMap getFileMap() {
		Configuration conf = ConfigUtils.getConfig();
		String className = conf.get(NameConstants.NAMENODE_KV_CLASS, RedisFileMap.class.getName());
		FileMap fileMap = null;
        try {
            fileMap = (FileMap) conf.getClassByName(className).newInstance();
        } catch (ClassNotFoundException e) {
            LOGGER.error("class not found of " + NameConstants.NAMENODE_KV_CLASS, e);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        if (fileMap == null) {
            fileMap = new RedisFileMap();
        }
		return fileMap;
	}
}
