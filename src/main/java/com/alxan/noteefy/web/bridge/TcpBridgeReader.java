package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.parse.Parser;
import com.alxan.noteefy.web.event.WebRequest;
import com.alxan.noteefy.web.serialize.KryoSerializer;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

import java.util.UUID;

public class TcpBridgeReader implements BridgeReader<ByteBuff> {
    private final Parser parser;
    private final IncomingMessageDispatcher messageDispatcher;
    private NoteefySerializer<ByteBuff> serializer;

    public TcpBridgeReader(Parser aParser, IncomingMessageDispatcher aMessageDispatcher) {
        messageDispatcher = aMessageDispatcher;
        serializer = new KryoSerializer();
        parser = aParser;
        parser.setSerializer(serializer);
        parser.registerMessageDispatcher(messageDispatcher);
    }

    @Override
    public void handle(ByteBuff buffer) {
        parser.parse(buffer);
    }

    @Override
    public NoteefySerializer<ByteBuff> getSerializer() {
        return serializer;
    }

    public void setSerializer(NoteefySerializer<ByteBuff> aSerializer) {
        serializer = aSerializer;
        parser.setSerializer(serializer);
    }

    @Override
    public void registerEventSubscriber(UUID publisherId, Subscriber subscriber) {
        messageDispatcher.registerToEvents(publisherId, subscriber);
    }

    @Override
    public void registerRequestSubscriber(Class<? extends WebRequest> type, Subscriber subscriber) {
        messageDispatcher.registerToRequests(type, subscriber);
    }
}
