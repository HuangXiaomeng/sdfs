/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.filemap;

public class FileMapFactory {
	public static FileMap getFileMap() {
		//TODO
		return new RedisFileMap();
	}
}
