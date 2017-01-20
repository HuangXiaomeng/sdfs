/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.namenode.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileActor extends UntypedActor {

	public static Props props() {
		return Props.create(FileActor.class);
	}

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
