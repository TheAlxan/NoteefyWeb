package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.ServiceDiscovery;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.factory.BridgeFactory;
import com.alxan.noteefy.web.common.PublisherAddress;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.event.BridgeRegisterRequest;
import com.alxan.noteefy.web.event.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BridgeManagerUnitTest extends TestHelper {
    @Test
    public void shouldCreateBridge() {
        RemoteAddress remoteAddress = createTestRemoteAddress();
        DataSource<Object, Object> dataSource = createMockDataSource();
        Bridge<Object, Object> bridge = createMockBridge();
        BridgeFactory<Object, Object> bridgeFactory = createTestBridgeFactory(bridge);
        BridgeManager bridgeManager = createTestBridgeManager(new Listener(), new ServiceDiscovery());

        Assertions.assertTrue(bridgeManager.isNotRegistered(remoteAddress));
        bridgeManager.createBridge(bridgeFactory, dataSource);
        Assertions.assertFalse(bridgeManager.isNotRegistered(remoteAddress));
        Bridge<?, ?> fetchedBridge = bridgeManager.getBridge(remoteAddress);
        Assertions.assertEquals(bridge, fetchedBridge);
    }

    @Test
    public void shouldRegisterSubscriberToBridge() throws InterruptedException {
        initialLatch(1);
        RemoteAddress remoteAddress = createTestRemoteAddress();
        Bridge<Object, Object> bridge = createMockBridge();
        countDownWhen(bridge).registerSubscriberToWebPublisher(Mockito.any(), Mockito.any());
        BridgeManager bridgeManager = createTestBridgeManager(new Listener(), new ServiceDiscovery());
        BridgeRegistry bridgeRegistry = createTestBridgeRegistry(bridge);
        bridgeManager.setBridgeRegistry(bridgeRegistry);
        PublisherAddress publisherAddress = new PublisherAddress(remoteAddress, UUID.randomUUID());
        Subscriber subscriber = createMockSubscriber();

        bridgeManager.registerSubscriberToBridge(publisherAddress, subscriber);

        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterBridgeToPublisher() throws InterruptedException {
        initialLatch(1);
        Listener requestListener = new Listener();
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        BridgeManager bridgeManager = createTestBridgeManager(requestListener, serviceDiscovery);
        Bridge<?, ?> bridge = createMockBridge();
        countDownWhen(bridge).subscribeToOutgoingMessages(Mockito.any());
        BridgeRegistry bridgeRegistry = createTestBridgeRegistry(bridge);
        bridgeManager.setBridgeRegistry(bridgeRegistry);
        WebPublisherRegistry webPublisherRegistry = createTestWebPublisherRegistry(serviceDiscovery);
        Publisher publisher = createMockPublisher();
        webPublisherRegistry.registerPublisher(publisher);
        RegisterRequest request = new RegisterRequest(publisher.getUUID());
        BridgeRegisterRequest bridgeRequest = new BridgeRegisterRequest(bridge, request);

        requestListener.onEvent(new Event<>(bridgeRequest));

        assertLatchCount(0);
    }

    @SuppressWarnings("unchecked")
    private <I, O> BridgeFactory<I, O> createTestBridgeFactory(Bridge<Object, Object> createdBridge) {
        BridgeFactory<Object, Object> bridgeFactory = Mockito.mock(BridgeFactory.class);
        Mockito.when(bridgeFactory.createBridge(Mockito.any())).thenReturn(createdBridge);
        return (BridgeFactory<I, O>) bridgeFactory;
    }

    private WebPublisherRegistry createTestWebPublisherRegistry(ServiceDiscovery serviceDiscovery) {
        return new WebPublisherRegistry(serviceDiscovery);
    }

    private BridgeManager createTestBridgeManager(Listener requestListener, ServiceDiscovery serviceDiscovery) {
        return new BridgeManager(requestListener, serviceDiscovery);
    }

    private BridgeRegistry createTestBridgeRegistry(Bridge<?, ?> bridge) {
        BridgeRegistry bridgeRegistry = Mockito.mock(BridgeRegistry.class);
        Mockito.when(bridgeRegistry.getBridge(Mockito.any())).thenReturn((Bridge) bridge);
        return bridgeRegistry;
    }

    @SuppressWarnings("unchecked")
    private Bridge<Object, Object> createMockBridge() {
        RemoteAddress remoteAddress = createTestRemoteAddress();
        Bridge<Object, Object> bridge = Mockito.mock(Bridge.class);
        Mockito.when(bridge.getAddress()).thenReturn(remoteAddress);
        Mockito.doNothing().when(bridge).registerToRegisterRequest(Mockito.any());
        return bridge;
    }
}
