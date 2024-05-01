package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.event.WebMessage;
import com.alxan.noteefy.web.serialize.KryoSerializer;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;

public class TcpBridgeWriter implements BridgeWriter<ByteBuff> {
    private final WriteSource<ByteBuff> socket;
    private final AsyncChannel writeChannel;
    private final WebMessageTypeRegistry typeRegistry;
    private NoteefySerializer<ByteBuff> serializer;

    public TcpBridgeWriter(WriteSource<ByteBuff> aSocket, WebMessageTypeRegistry aTypeRegistry) {
        socket = aSocket;
        Listener writeListener = new Listener();
        writeListener.doOnEvent(WebMessage.class, this::writeMessage);
        writeChannel = new AsyncChannel(writeListener);
        serializer = new KryoSerializer();
        typeRegistry = aTypeRegistry;
    }

    @Override
    public Subscriber getWriteSubscriber() {
        return writeChannel;
    }

    @Override
    public NoteefySerializer<ByteBuff> getSerializer() {
        return serializer;
    }

    public void setSerializer(NoteefySerializer<ByteBuff> aSerializer) {
        serializer = aSerializer;
    }

    @Override
    public WebMessageTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    @Override
    public void writeMessage(WebMessage message) {
        queueWriteRequest(message);
    }

    private void queueWriteRequest(WebMessage webMessage) {
        writeChannel.onEvent(new Event<>(webMessage));
    }

    private void writeMessage(Event<? extends WebMessage> event) {
        WebMessage message = event.getContent();
        int typeId = getTypeRegistry().getTypeId(message.getClass());
        ByteBuff serializedBuffer = getSerializer().serialize(message);
        byte[] messageBytes = serializedBuffer.getBytes();
        ByteBuff buffer = new VertxBuff();
        buffer.appendInt(messageBytes.length + 4);
        buffer.appendInt(typeId);
        buffer.appendBytes(messageBytes);
        write(buffer);
    }

    @Override
    public void write(ByteBuff buffer) {
        socket.write(buffer);
    }
}
