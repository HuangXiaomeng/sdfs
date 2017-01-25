/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.selector;

import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.util.ConfigUtils;
import org.armon.sdfs.namenode.NameConstants;

public class NodeSelectorFactory {
    private static final Logger LOGGER = LogManager.getLogger();

	public static NodeSelector getSelector() {
		Configuration conf = ConfigUtils.getConfig();
		String className = conf.get(NameConstants.NAMENODE_NODE_SELECTOR_CLASS, MinDiskSelector.class.getName());
		NodeSelector selector = null;
		try {
		    selector = (NodeSelector) conf.getClassByName(className).newInstance();
		} catch (ClassNotFoundException e) {
		    LOGGER.error("class not found of " + NameConstants.NAMENODE_NODE_SELECTOR_CLASS, e);
		} catch (Exception e) {
            LOGGER.error("", e);
        }
		if (selector == null) {
		    selector = new MinDiskSelector();
		}
		return selector;
	}
}
