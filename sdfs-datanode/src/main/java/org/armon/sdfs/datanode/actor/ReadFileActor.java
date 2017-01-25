/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.util.ExceptionUtil;
import org.armon.sdfs.datanode.io.FileStream;
import org.armon.sdfs.datanode.io.ReadResult;
import org.armon.sdfs.protocol.FileProtos.ReadFileRequest;
import org.armon.sdfs.protocol.FileProtos.ReadFileResponse;
import org.armon.sdfs.protocol.FileProtos.TailFileRequest;
import org.armon.sdfs.protocol.FileProtos.TailFileResponse;

import com.google.protobuf.ByteString;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class ReadFileActor extends UntypedActor {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(ReadFileActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof ReadFileRequest) {
            readFile((ReadFileRequest) obj);
        } else if (obj instanceof TailFileRequest) {
            tailFile((TailFileRequest) obj);
        } else {
            unhandled(obj);
        }

    }

    private void readFile(ReadFileRequest request) {
        ReadFileResponse response = null;
        try {
            String fileName = request.getFileName();
            long offset = request.getOffset();
            int size = request.getSize();
            FileStream stream = new FileStream(fileName);
            ReadResult result = stream.readText(offset, size);
            response = ReadFileResponse.newBuilder()
                    .setSuccess(true)
                    .setContent(ByteString.copyFromUtf8(result.getContent()))
                    .setIsEnd(result.isEnd())
                    .setOffset(result.getOffset()).build();
        } catch (Exception e) {
            response = ReadFileResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
        } finally {
            getSender().tell(response, getSelf());
        }

    }

    private void tailFile(TailFileRequest request) {
        TailFileResponse response = null;
        try {
            String fileName = request.getFileName();
            int size = request.getSize();
            FileStream stream = new FileStream(fileName);
            ReadResult result = stream.tail(size);
            response = TailFileResponse.newBuilder()
                    .setSuccess(true)
                    .setContent(ByteString.copyFromUtf8(result.getContent()))
                    .build();
        } catch (Exception e) {
            response = TailFileResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
        } finally {
            getSender().tell(response, getSelf());
        }
    }

}
