/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common;

public class DataNodeInfo {

	private int available_disk;
	private DataNodeStatus status;

	public DataNodeInfo(int disk) {
		this.available_disk = disk;
	}

	public int getAvailable_disk() {
		return available_disk;
	}

	public void setAvailable_disk(int available_disk) {
		this.available_disk = available_disk;
	}

	public DataNodeStatus getStatus() {
		return status;
	}

	public void setStatus(DataNodeStatus status) {
		this.status = status;
	}

}
