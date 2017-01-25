/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.filemap;

import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.common.Pair;
import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.common.util.HostUtil;
import org.armon.sdfs.namenode.NameConstants;

import com.google.common.collect.Sets;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisFileMap extends KVFileMap {

	private JedisCluster cluster;

	public RedisFileMap() {
	    Configuration conf = ConfigUtils.getConfig();
	    List<Pair<String, Integer>> hosts = HostUtil.getHostsByKey(conf, NameConstants.NAMENODE_REDIS_HOSTS);
	    Set<HostAndPort> clusterNodes = Sets.newHashSet();
	    for (Pair<String, Integer> host : hosts) {
	        clusterNodes.add(new HostAndPort(host.getFirst(), host.getSecond()));
	    }
        cluster = new JedisCluster(clusterNodes);
	}

    @Override
	public Set<DataNodeKey> get(String filename) {
	    Set<DataNodeKey> datanodes = Sets.newHashSet();
        Set<String> datanodeSet = cluster.smembers(filename);
        if (datanodeSet != null && !datanodes.isEmpty()) {
            for (String datanodeStr : datanodeSet) {
                String[] tokens = datanodeStr.trim().split(":");
                datanodes.add(new DataNodeKey(tokens[0], Integer.valueOf(tokens[1])));
            }
        }
        return datanodes;
	}

    @Override
    public void addDataNode(String fileName, DataNodeKey nodeKey) {
        cluster.sadd(fileName, HostUtil.toHost(nodeKey));
    }

}
