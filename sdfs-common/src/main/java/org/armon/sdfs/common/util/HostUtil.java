/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common.util;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.armon.sdfs.common.DataNodeKey;
import org.armon.sdfs.common.Pair;

import com.google.common.collect.Lists;

public class HostUtil {
    public static List<Pair<String, Integer>> getHostsByKey(Configuration conf, String key) {
        List<Pair<String, Integer>> hosts = Lists.newArrayList();
        String hostStr = conf.get(key);
        String[] hostArray = hostStr.trim().split(",");
        for (String host : hostArray) {
            String[] tokens = host.trim().split(":");
            hosts.add(new Pair<String, Integer>(tokens[0].trim(), Integer.valueOf(tokens[1].trim())));
        }
        return hosts;
    }

    public static String toHost(DataNodeKey nodeKey) {
        return nodeKey.getIp() + ":" + nodeKey.getPort();
    }
}
