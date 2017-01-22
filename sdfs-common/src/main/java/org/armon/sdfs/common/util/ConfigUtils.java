/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common.util;

import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.hadoop.conf.Configuration;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigUtils {
	private static Configuration conf;

    static {
        conf = new Configuration(false);
        conf.addResource("sdfs-site.xml");
    }

    public static Configuration getConfig() {
        return conf;
    }

    public static Configuration newConfig() {
        conf = new Configuration(false);
        conf.addResource("sdfs-site.xml");
        return conf;
    }

    public static Configuration newConfig(String name) {
        conf = new Configuration(false);
        conf.addResource(name);
        return conf;
    }

    /**
     * 获取Akka的配置
     *
     * @param fileName
     * @return
     */
    public static Config getAkkaConfig(String fileName) {
        String key = "akka.remote.netty.tcp.hostname";
        Config akkaConfig = ConfigFactory.load(fileName);
        if (akkaConfig.hasPath(key) && !akkaConfig.getString(key).isEmpty()) {
            return akkaConfig;
        } else {
            try {
                return ConfigFactory.parseString(key + "=" + IPUtils.getIPV4Address()).withFallback(akkaConfig);
            } catch (UnknownHostException | SocketException ex) {
                Throwables.propagate(ex);
                return null;
            }
        }
    }
}
