package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.event.WebMessage;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class TcpBridgeWriterUnitTest extends TestHelper {
    @Test
    public void shouldSetAndGetSerializer() {
        TcpBridgeWriter bridgeWriter = createTestTcpBridgeWriter();
        NoteefySerializer<ByteBuff> serializer = Mockito.mock(NoteefySerializer.class);
        bridgeWriter.setSerializer(serializer);
        Assertions.assertEquals(serializer, bridgeWriter.getSerializer());
    }

    @Test
    public void shouldReturnWriteSubscriber() {
        TcpBridgeWriter bridgeWriter = createTestTcpBridgeWriter();
        Assertions.assertNotNull(bridgeWriter.getWriteSubscriber());
    }

    @Test
    public void shouldWriteWebMessage() throws InterruptedException {
        initialLatch(1);
        TcpBridgeWriter bridgeWriter = createTestTcpBridgeWriter();
        UUID publisherId = UUID.randomUUID();
        Event<?> event = new Event<>(256);
        WebMessage webMessage = new WebEvent(publisherId, event);

        bridgeWriter.writeMessage(webMessage);

        assertLatchCount(0);
    }

    private TcpBridgeWriter createTestTcpBridgeWriter() {
        WriteSource<ByteBuff> writeSource = Mockito.mock(WriteSource.class);
        WebMessageTypeRegistry messageTypeRegistry = Mockito.mock(WebMessageTypeRegistry.class);
        TcpBridgeWriter bridgeWriter = new TcpBridgeWriter(writeSource, messageTypeRegistry);
        countDownWhen(writeSource).write(Mockito.any());
        return bridgeWriter;
    }
}
