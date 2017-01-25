/*
 * Author: HuangXiaomeng
 * Mail: xiaom@apache.org
 */
package org.armon.sdfs.datanode.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.armon.sdfs.common.util.ExceptionUtil;
import org.armon.sdfs.protocol.FileProtos.ReadFileRequest;
import org.armon.sdfs.protocol.FileProtos.ReadFileResponse;
import org.armon.sdfs.protocol.FileProtos.TailFileRequest;
import org.armon.sdfs.protocol.FileProtos.TailFileResponse;
import org.armon.sdfs.protocol.FileProtos.WriteFileRequest;
import org.armon.sdfs.protocol.FileProtos.WriteFileResponse;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileRoutingActor extends UntypedActor {

	private final static Logger LOGGER = LogManager.getLogger();
    private int size;
    private static List<ActorRef> writerActors = new ArrayList<ActorRef>();

    public FileRoutingActor(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            writerActors.add(getContext().actorOf(WriteFileActor.props()));
        }
    }

    public static Props props(int size) {
        return Props.create(FileRoutingActor.class, size);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof ReadFileRequest) {
            readFile((ReadFileRequest) obj);
        } else if (obj instanceof WriteFileRequest) {
            writeFile((WriteFileRequest) obj);
        } else if (obj instanceof TailFileRequest) {
            tailFile((TailFileRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void writeFile(WriteFileRequest request) {
        try {
            String filename = request.getFileName();
            int hashcode = filename.hashCode();
            writerActors.get((hashcode == Integer.MIN_VALUE ? 0 : Math.abs(hashcode)) % size).forward(request,
                    getContext());
        } catch (Exception e) {
            WriteFileResponse response = WriteFileResponse.newBuilder().setSuccess(false)
                    .setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error(e);
            throw e;
        }
    }

    private void readFile(ReadFileRequest request) {
        try {
            ActorRef ref = getContext().actorOf(ReadFileActor.props());
            ref.forward(request, getContext());
            ref.forward(PoisonPill.getInstance(), getContext());
        } catch (Exception e) {
            ReadFileResponse response = ReadFileResponse.newBuilder().setSuccess(false)
                    .setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error(e);
            throw e;
        }
    }

    private void tailFile(TailFileRequest request) {
        try {
            ActorRef ref = getContext().actorOf(ReadFileActor.props());
            ref.forward(request, getContext());
            ref.forward(PoisonPill.getInstance(), getContext());
        } catch (Exception e) {
            TailFileResponse response = TailFileResponse.newBuilder().setSuccess(false)
                    .setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error(e);
            throw e;
        }
    }

}
