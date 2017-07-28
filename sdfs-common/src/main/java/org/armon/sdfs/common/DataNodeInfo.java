/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common;

public class DataNodeInfo {

	private long available_disk;
	private DataNodeStatus status;

	public DataNodeInfo(long disk) {
		this.available_disk = disk;
	}

	public long getAvailable_disk() {
		return available_disk;
	}

	public void setAvailable_disk(long available_disk) {
		this.available_disk = available_disk;
	}

	public DataNodeStatus getStatus() {
		return status;
	}

	public void setStatus(DataNodeStatus status) {
		this.status = status;
	}

}
