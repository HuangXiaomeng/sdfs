/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common;

public interface SdfsConstants {

	String NAMENODE_AKKA_SYSTEM_NAME = "namenode";
	String NAMENODE_AKKA_USER_NAME = "namenode";
	String NAMENODE_AKKA_USER_PATH = "/user/" + NAMENODE_AKKA_USER_NAME;

	String NAMENODE_AKKA_HEARTBEAT_NAME = "heartbeat";
	String NAMENODE_AKKA_FILE_NAME = "file";

	String DATANODE_AKKA_SYSTEM_NAME = "datanode";
	String DATANODE_AKKA_USER_NAME = "datanode";
	String DATANODE_AKKA_USER_PATH = "/user/" + DATANODE_AKKA_USER_NAME;

}
