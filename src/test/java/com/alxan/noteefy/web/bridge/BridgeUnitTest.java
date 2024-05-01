package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.factory.BridgeStreamFactory;
import com.alxan.noteefy.web.event.BridgeRegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BridgeUnitTest extends TestHelper {
    @Test
    public void shouldSubscribeToOutgoingMessages() throws InterruptedException {
        initialLatch(1);
        WebPublisher webPublisher = Mockito.mock(WebPublisher.class);
        countDownWhen(webPublisher).register(Mockito.any());
        Bridge<Object, Object> bridge = createTestBridge();

        bridge.subscribeToOutgoingMessages(webPublisher);

        assertLatchCount(0);
    }

    @Test
    public void shouldListenToRegisterRequests() throws InterruptedException {
        initialLatch(1);
        Bridge<Object, Object> bridge = createTestBridge();
        Listener requestListener = new Listener();
        requestListener.doOnEvent(BridgeRegisterRequest.class, event -> countDownLatch());
        bridge.registerToRegisterRequest(requestListener);
        BridgeRegisterRequest bridgeRegisterRequest = Mockito.mock(BridgeRegisterRequest.class);

        requestListener.onEvent(new Event<>(bridgeRegisterRequest));

        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterSubscriberToWebPublisher() throws InterruptedException {
        initialLatch(2);
        Bridge<Object, Object> bridge = createTestBridge();
        countDownWhen(bridge.bridgeReader).registerEventSubscriber(Mockito.any(), Mockito.any());
        countDownWhen(bridge.bridgeWriter).writeMessage(Mockito.any());
        UUID publisherId = UUID.randomUUID();
        Subscriber subscriber = createMockSubscriber();

        bridge.registerSubscriberToWebPublisher(publisherId, subscriber);
        assertLatchCount(0);
    }

    private Bridge<Object, Object> createTestBridge() {
        BridgeStreamFactory<Object, Object> streamFactory = createTestStreamFactory();
        DataSource<Object, Object> dataSource = createMockDataSource();
        return new Bridge<>(streamFactory, dataSource) {
            @Override
            public SourceAddress getAddress() {
                return null;
            }
        };
    }

    private BridgeStreamFactory<Object, Object> createTestStreamFactory() {
        BridgeStreamFactory<Object, Object> streamFactory = Mockito.mock(BridgeStreamFactory.class);
        BridgeReader<Object> bridgeReader = Mockito.mock(BridgeReader.class);
        BridgeWriter<Object> bridgeWriter = Mockito.mock(BridgeWriter.class);
        Mockito.when(streamFactory.createReader(Mockito.any())).thenReturn(bridgeReader);
        Mockito.when(streamFactory.createWriter(Mockito.any())).thenReturn(bridgeWriter);
        Mockito.doNothing().when(bridgeReader).registerRequestSubscriber(Mockito.any(), Mockito.any());
        return streamFactory;
    }
}
