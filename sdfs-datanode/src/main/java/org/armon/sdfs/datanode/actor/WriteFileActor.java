/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.util.ExceptionUtil;
import org.armon.sdfs.datanode.io.FileStream;
import org.armon.sdfs.protocol.FileProtos.WriteFileRequest;
import org.armon.sdfs.protocol.FileProtos.WriteFileResponse;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class WriteFileActor extends UntypedActor {

	private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(WriteFileActor.class);
    }

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj instanceof WriteFileRequest) {
			writeFile((WriteFileRequest) obj);
		} else {
			unhandled(obj);
		}
	}

	private void writeFile(WriteFileRequest request) {
		WriteFileResponse response = null;
		try {
			String fileName = request.getFileName();
			String text = request.getContent().toStringUtf8();
			boolean isEnd = request.getIsEnd();
			FileStream stream = new FileStream(fileName);
			stream.writeText(text);
			if (isEnd) {
				stream.writeEndFlag();
			}
			response = WriteFileResponse.newBuilder().setSuccess(true).build();
		} catch (Exception e) {
			response = WriteFileResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
		} finally {
			getSender().tell(response, getSelf());
		}
	}

}
