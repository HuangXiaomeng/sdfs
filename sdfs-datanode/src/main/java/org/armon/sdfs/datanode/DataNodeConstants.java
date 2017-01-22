/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode;

public interface DataNodeConstants {
	String NAMENODE_AKKA_PATH = "namenode.akka.path";

	String DATANODE_WRITEACTOR_NUM = "datanode.writeactor.num";

	String DATANODE_FILE_PATH = "datanode.file.path";

	String DATANODE_FILE_READ_MAX_SIZE = "datanode.file.read.max.size";

	char END_OF_LOG = '\004';   //LOG结束符
}
