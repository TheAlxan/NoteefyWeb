package com.alxan.noteefy.web.bridge.parse;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.WebMessageTypeRegistry;
import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.event.WebMessage;
import com.alxan.noteefy.web.serialize.KryoSerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ParseUnitTest extends TestHelper {
    @Test
    public void shouldParseBuffer() throws InterruptedException {
        initialLatch(1);
        Listener messageListener = createCountDownListenerFor(WebEvent.class);
        Parser parser = createTestParser(messageListener);
        UUID publisherId = UUID.randomUUID();
        Event<Integer> event = new Event<>(256);
        WebEvent webEvent = new WebEvent(publisherId, event);
        ByteBuff byteBuff = writeToBuffer(webEvent);

        parser.parse(byteBuff);

        assertLatchCount(0);
    }

    @Test
    public void shouldDispatchWebEvent() throws InterruptedException {
        initialLatch(1);
        Event<Integer> event = new Event<>(256);
        UUID publisherId = UUID.randomUUID();
        WebEvent webEvent = new WebEvent(publisherId, event);
        ByteBuff buffer = writeToBuffer(webEvent);
        Listener eventListener = new Listener();
        eventListener.doOnEvent(WebEvent.class, e -> {
            WebEvent newWebEvent = e.getContent();
            Event<?> newEvent = newWebEvent.getEvent();
            if (!newWebEvent.getPublisherId().toString().contentEquals(publisherId.toString())) return;
            if (((Integer) newEvent.getContent()) != 256) return;
            countDownLatch();
        });
        Parser parser = createTestParser(eventListener);

        parser.parse(buffer);

        assertLatchCount(0);
    }

    @Test
    public void shouldDispatchRegisterRequest() throws InterruptedException {
        initialLatch(1);
        UUID publisherId = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest(publisherId);
        ByteBuff buffer = writeToBuffer(request);
        Listener requestListener = new Listener();
        requestListener.doOnEvent(RegisterRequest.class, e -> {
            RegisterRequest newRequest = e.getContent();
            if (!newRequest.getPublisherId().toString().contentEquals(publisherId.toString())) return;
            countDownLatch();
        });
        Parser parser = createTestParser(requestListener);

        parser.parse(buffer);

        assertLatchCount(0);
    }

    private ByteBuff writeToBuffer(WebMessage message) {
        WebMessageTypeRegistry webMessageTypeRegistry = WebMessageTypeRegistry.getDefaultRegistry();
        ByteBuff serializedBuffer = new KryoSerializer().serialize(message);
        byte[] messageBytes = serializedBuffer.getBytes();
        int typeId = webMessageTypeRegistry.getTypeId(message.getClass());
        ByteBuff byteBuff = new VertxBuff();
        byteBuff.appendInt(messageBytes.length + 4);
        byteBuff.appendInt(typeId);
        byteBuff.appendBytes(messageBytes);
        return byteBuff;
    }

    private Parser createTestParser(Listener messageListener) {
        Parser parser = new Parser(WebMessageTypeRegistry.getDefaultRegistry());
        parser.setSerializer(new KryoSerializer());
        parser.registerMessageDispatcher(messageListener);
        return parser;
    }
}
