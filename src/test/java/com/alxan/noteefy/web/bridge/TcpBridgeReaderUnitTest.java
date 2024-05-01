package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.parse.Parser;
import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class TcpBridgeReaderUnitTest extends TestHelper {
    @Test
    public void shouldHandleBuffer() throws InterruptedException {
        initialLatch(1);
        Parser parser = Mockito.mock(Parser.class);
        countDownWhen(parser).parse(Mockito.any());
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        TcpBridgeReader bridgeReader = new TcpBridgeReader(parser, messageDispatcher);
        ByteBuff buffer = Mockito.mock(ByteBuff.class);

        bridgeReader.handle(buffer);

        assertLatchCount(0);
    }

    @Test
    public void shouldSetAndGetSerializer() {
        Parser parser = Mockito.mock(Parser.class);
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        TcpBridgeReader bridgeReader = new TcpBridgeReader(parser, messageDispatcher);
        NoteefySerializer<ByteBuff> serializer = Mockito.mock(NoteefySerializer.class);

        bridgeReader.setSerializer(serializer);

        Assertions.assertEquals(serializer, bridgeReader.getSerializer());
    }

    @Test
    public void shouldReadWebEvents() throws InterruptedException {
        initialLatch(1);
        Listener listener = createCountDownListenerFor(Integer.class);
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        TcpBridgeReader bridgeReader = createTestTcpBridgeReader(messageDispatcher);
        UUID publisherId = UUID.randomUUID();
        bridgeReader.registerEventSubscriber(publisherId, listener);
        Event<?> event = new Event<>(256);
        WebEvent webEvent = new WebEvent(publisherId, event);
        Event<?> dispatchEvent = new Event<>(webEvent);

        messageDispatcher.onEvent(dispatchEvent);

        assertLatchCount(0);
    }

    @Test
    public void shouldReadRegisterRequests() throws InterruptedException {
        initialLatch(1);
        Listener listener = createCountDownListenerFor(RegisterRequest.class);
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        TcpBridgeReader bridgeReader = createTestTcpBridgeReader(messageDispatcher);
        bridgeReader.registerRequestSubscriber(RegisterRequest.class, listener);
        RegisterRequest request = new RegisterRequest(UUID.randomUUID());
        Event<?> dispatchEvent = new Event<>(request);

        messageDispatcher.onEvent(dispatchEvent);

        assertLatchCount(0);
    }

    private TcpBridgeReader createTestTcpBridgeReader(IncomingMessageDispatcher messageDispatcher) {
        Parser parser = Mockito.mock(Parser.class);
        Mockito.doNothing().when(parser).setSerializer(Mockito.any());
        TcpBridgeReader bridgeReader = new TcpBridgeReader(parser, messageDispatcher);
        return bridgeReader;
    }
}
