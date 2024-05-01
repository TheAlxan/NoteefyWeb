package com.alxan.noteefy.web.bridge.parse;

import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.WebMessageTypeMeta;
import com.alxan.noteefy.web.bridge.WebMessageTypeRegistry;
import com.alxan.noteefy.web.common.SingleThreadWorker;
import com.alxan.noteefy.web.event.WebMessage;
import com.alxan.noteefy.web.exception.ClassLoadingException;
import com.alxan.noteefy.web.exception.ParseException;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Parser extends BufferParser {
    private final SingleThreadWorker parsingWorker = new SingleThreadWorker();
    private final WebMessageTypeRegistry typeRegistry;
    private final ReentrantLock bufferLock = new ReentrantLock();
    private final Condition itemSignal = bufferLock.newCondition();
    private final Broadcaster messagePublisher = new Broadcaster();
    private final AtomicInteger waitCounter = new AtomicInteger(0);
    private NoteefySerializer<ByteBuff> serializer;

    public Parser(WebMessageTypeRegistry aTypeRegistry) {
        typeRegistry = aTypeRegistry;
    }

    public void parse(ByteBuff buffer) {
        bufferLock.lock();
        appendBuffer(buffer);
        itemSignal.signalAll();
        bufferLock.unlock();
        if (waitCounter.get() == 0) {
            waitCounter.set(0);
            parsingWorker.appendJob(this::startParsing);
        }
    }

    private WebMessage deserializeItem(byte[] bytes) throws ClassNotFoundException {
        ByteBuff buffer = new VertxBuff(bytes);
        Class<? extends WebMessage> typeClass = getTypeClass(buffer);
        buffer = buffer.trim(4);
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer.getBytes());
        return serializer.deserialize(new VertxBuff(stream.readAllBytes()), typeClass);
    }

    private Class<? extends WebMessage> getTypeClass(ByteBuff buffer) throws ClassNotFoundException {
        int typeOrdinal = buffer.readInt();
        WebMessageTypeMeta typeMeta = typeRegistry.getTypeMeta(typeOrdinal);
        return typeMeta.getTypeClass();
    }

    private synchronized WebMessage getCurrentItem() {
        try {
            waitForSizeToReceive();
            int size = readContentSize();
            waitForItemToReceive(size);
            byte[] content = readContent(size);
            return deserializeItem(content);
        } catch (InterruptedException | IOException e) {
            ParseException exception = new ParseException(e, getUUID());
            NotificationCenter.error(exception);
            return null;
        } catch (ClassNotFoundException e) {
            ClassLoadingException exception = new ClassLoadingException(e, getUUID());
            NotificationCenter.error(exception);
            return null;
        }
    }

    private void waitForSizeToReceive() throws InterruptedException {
        if (!isSizeReceived()) {
            waitCounter.incrementAndGet();
            itemSignal.await();
        }
    }

    private void waitForItemToReceive(int size) throws InterruptedException {
        if (!isItemReceived(size)) {
            waitCounter.incrementAndGet();
            itemSignal.await();
        }
    }

    public void registerMessageDispatcher(Subscriber subscriber) {
        messagePublisher.register(subscriber);
    }

    public void setSerializer(NoteefySerializer<ByteBuff> aSerializer) {
        serializer = aSerializer;
    }

    private void startParsing() {
        WebMessage webMessage;
        while (true) {
            bufferLock.lock();
            webMessage = getCurrentItem();
            bufferLock.unlock();
            publishMessage(webMessage);
        }
    }

    private void publishMessage(WebMessage item) {
        messagePublisher.publish(item);
    }
}
