package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.Service;
import com.alxan.noteefy.web.ServiceDiscovery;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.factory.BridgeFactory;
import com.alxan.noteefy.web.common.PublisherAddress;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.event.BridgeRegisterRequest;

import java.util.Set;
import java.util.UUID;

public class BridgeManager extends Service {
    private final Listener registerListener;
    private BridgeRegistry bridgeRegistry;

    public BridgeManager(Listener aRegisterListener, ServiceDiscovery aServiceDiscovery) {
        super(aServiceDiscovery);
        registerListener = aRegisterListener;
        registerListener.doOnEvent(BridgeRegisterRequest.class, this::registerBridgeToPublisher);
        bridgeRegistry = new BridgeRegistry();
    }

    public Bridge<?, ?> getBridge(SourceAddress address) {
        return bridgeRegistry.getBridge(address);
    }

    public boolean isNotRegistered(RemoteAddress address) {
        return bridgeRegistry.isNotRegistered(address);
    }

    public <I, O> void createBridge(BridgeFactory<I, O> bridgeFactory, DataSource<I, O> dataSource) {
        Bridge<?, ?> bridge = bridgeFactory.createBridge(dataSource);
        bridge.registerToRegisterRequest(registerListener);
        SourceAddress address = bridge.getAddress();
        bridgeRegistry.registerBridge(address, bridge);
    }

    public void registerSubscriberToBridge(PublisherAddress address, Subscriber subscriber) {
        Bridge<?, ?> bridge = getBridge(address.getRemoteAddress());
        bridge.registerSubscriberToWebPublisher(address.getPublisherId(), subscriber);
    }

    protected void setBridgeRegistry(BridgeRegistry aBridgeRegistry) {
        bridgeRegistry = aBridgeRegistry;
    }

    private void registerBridgeToPublisher(Event<BridgeRegisterRequest> event) {
        BridgeRegisterRequest request = event.getContent();
        UUID publisherId = request.getRegisterRequest().getPublisherId();
        Bridge<?, ?> bridge = request.getBridge();
        Set<WebPublisherRegistry> publisherRegistries = serviceDiscovery.getService(WebPublisherRegistry.class);
        for (WebPublisherRegistry registry : publisherRegistries) {
            WebPublisher publisher = registry.getWebPublisher(publisherId);
            if (publisher == null) continue;
            bridge.subscribeToOutgoingMessages(publisher);
        }
    }
}
