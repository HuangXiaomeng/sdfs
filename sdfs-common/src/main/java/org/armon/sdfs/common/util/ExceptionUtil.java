/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.common.util;

public class ExceptionUtil {

	public static String getErrMsg(Exception e) {
        return (e.getMessage() == null ? e.toString() : e.getMessage());
    }
}
