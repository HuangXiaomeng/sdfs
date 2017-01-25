/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common.util;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.armon.sdfs.common.Pair;
import org.junit.Assert;
import org.junit.Test;

public class TestHostUtil {

    @Test
    public void testRedisHost() {
        Configuration conf = new Configuration();
        conf.set("namenode.redis.hosts", "10.11.6.129:7700,10.11.6.129:7701,10.11.6.129:7702");
        List<Pair<String, Integer>> hosts = HostUtil.getHostsByKey(conf, "namenode.redis.hosts");
        Assert.assertEquals("10.11.6.129", hosts.get(0).getFirst());
        Assert.assertEquals(Integer.valueOf(7700), hosts.get(0).getSecond());
        Assert.assertEquals("10.11.6.129", hosts.get(1).getFirst());
        Assert.assertEquals(Integer.valueOf(7701), hosts.get(1).getSecond());
        Assert.assertEquals("10.11.6.129", hosts.get(2).getFirst());
        Assert.assertEquals(Integer.valueOf(7702), hosts.get(2).getSecond());
    }
}
