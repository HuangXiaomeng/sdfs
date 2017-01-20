/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common;

import java.util.Objects;

public class DataNodeKey {
	private String ip;
    private int port;

    public DataNodeKey(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

	public String getAkkaRootPath() {
        return "akka.tcp://" + SdfsConstants.DATANODE_AKKA_USER_PATH + "@" + ip + ":" + port;
    }

    public String getDataNodePath() {
        return getAkkaRootPath() + SdfsConstants.DATANODE_AKKA_USER_PATH;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        DataNodeKey other = (DataNodeKey) obj;
        return Objects.equals(ip, other.ip) && Objects.equals(port, other.port);
    }

	@Override
	public String toString() {
		return "DataNodeKey [ip=" + ip + ", port=" + port + "]";
	}

}
