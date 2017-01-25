/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.redis;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestRedisCluster {
    private static JedisCluster cluster;

    @BeforeClass
    public static void setup() {
        Set<HostAndPort> clusterNodes = Sets.newHashSet();
        clusterNodes.add(new HostAndPort("10.11.6.129", 7700));
        clusterNodes.add(new HostAndPort("10.11.6.129", 7701));
        clusterNodes.add(new HostAndPort("10.11.6.129", 7702));
        cluster = new JedisCluster(clusterNodes);
    }

    @Test
    public void testString() {
        cluster.del("hello");
        cluster.set("hello", "world");
        String value = cluster.get("hello");
        Assert.assertEquals("world", value);
    }

    @Test
    public void testList() {
        cluster.del("list1");
        cluster.rpush("list1", "b", "c");
        cluster.rpush("list1", "d");
        cluster.lpush("list1", "a");
        List<String> list = cluster.lrange("list1", 0, -1);
        System.out.println(list);
        Assert.assertArrayEquals(Lists.newArrayList("a", "b", "c", "d").toArray(), list.toArray());
    }

    @Test
    public void testQueue() {
        cluster.del("queue1");
        cluster.rpush("queue1", "a", "b");
        cluster.rpush("queue1", "c");
        Assert.assertEquals("a", cluster.lpop("queue1"));
        Assert.assertEquals("b", cluster.lpop("queue1"));
        Assert.assertEquals("c", cluster.lpop("queue1"));
    }

    @Test
    public void testSet() {
        cluster.del("set1");
        cluster.sadd("set1", "a", "b", "c");
        cluster.sadd("set1", "a", "d");
        Set<String> set = cluster.smembers("set1");
        Assert.assertEquals(4, set.size());
        Assert.assertTrue(set.contains("a"));
        Assert.assertTrue(set.contains("b"));
        Assert.assertTrue(set.contains("c"));
        Assert.assertTrue(set.contains("d"));
    }
}
