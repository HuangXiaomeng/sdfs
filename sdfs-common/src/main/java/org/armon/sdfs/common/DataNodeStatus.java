/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common;

public enum DataNodeStatus {
	ONLINE(1, "在线"),
	OFFLINE(-1, "下线");

	private int value;
	private String description;

	DataNodeStatus(int value, String desc) {
		this.value = value;
		this.description = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
