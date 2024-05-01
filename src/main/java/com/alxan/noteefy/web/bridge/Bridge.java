package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.bridge.factory.BridgeStreamFactory;
import com.alxan.noteefy.web.event.BridgeRegisterRequest;
import com.alxan.noteefy.web.event.RegisterRequest;

import java.util.UUID;

public abstract class Bridge<I, O> {
    protected final SourceAddress address;
    protected final BridgeReader<I> bridgeReader;
    protected final BridgeWriter<O> bridgeWriter;
    protected final ReadSource<I> readSource;
    protected final WriteSource<O> writeSource;
    private final Broadcaster registerRequestPublisher;

    public Bridge(BridgeStreamFactory<I, O> streamFactory, DataSource<I, O> dataSource) {
        address = dataSource.getAddress();
        registerRequestPublisher = new Broadcaster();
        Listener registerRequestListener = new Listener();
        registerRequestListener.doOnEvent(RegisterRequest.class, this::passRegisterEvent);
        readSource = dataSource.getReadSource();
        writeSource = dataSource.getWriteSource();
        bridgeReader = streamFactory.createReader(readSource);
        bridgeWriter = streamFactory.createWriter(writeSource);
        bridgeReader.registerRequestSubscriber(RegisterRequest.class, registerRequestListener);
    }

    public abstract SourceAddress getAddress();

    public void subscribeToOutgoingMessages(WebPublisher webPublisher) {
        Subscriber writeSubscriber = bridgeWriter.getWriteSubscriber();
        webPublisher.register(writeSubscriber);
    }

    protected void registerToRegisterRequest(Subscriber subscriber) {
        registerRequestPublisher.register(subscriber);
    }

    protected void registerSubscriberToWebPublisher(UUID publisherId, Subscriber subscriber) {
        bridgeReader.registerEventSubscriber(publisherId, subscriber);
        sendRegisterRequest(publisherId);
    }

    protected void sendRegisterRequest(UUID publisherId) {
        RegisterRequest request = new RegisterRequest(publisherId);
        bridgeWriter.writeMessage(request);
    }

    private void passRegisterEvent(Event<RegisterRequest> requestEvent) {
        RegisterRequest registerRequest = requestEvent.getContent();
        BridgeRegisterRequest bridgeRequest = new BridgeRegisterRequest(this, registerRequest);
        registerRequestPublisher.publish(bridgeRequest);
    }
}
