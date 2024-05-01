package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.broker.Broker;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.event.WebRequest;

import java.util.UUID;

public class IncomingMessageDispatcher extends MessageDispatcher {
    private final Broker eventDispatcher;
    private final Broker requestDispatcher;

    public IncomingMessageDispatcher() {
        super();
        eventDispatcher = new Broker();
        requestDispatcher = new Broker();
    }

    public void registerToEvents(UUID publisherId, Subscriber subscriber) {
        eventDispatcher.register(publisherId.toString(), subscriber);
    }

    public void registerToRequests(Class<? extends WebRequest> type, Subscriber subscriber) {
        requestDispatcher.register(type.getCanonicalName(), subscriber);
    }

    @MessageHandler(type = WebEvent.class)
    protected void handleWebEvent(WebEvent webEvent) {
        Event<?> contentEvent = webEvent.getEvent();
        String topic = webEvent.getPublisherId().toString();
        eventDispatcher.publish(topic, contentEvent);
    }

    @MessageHandler(type = RegisterRequest.class)
    protected void handleRegisterRequest(RegisterRequest request) {
        String topic = request.getRequestTopic();
        requestDispatcher.publish(topic, request);
    }
}
