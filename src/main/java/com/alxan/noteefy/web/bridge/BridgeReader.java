package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.event.WebRequest;
import com.alxan.noteefy.web.serialize.NoteefySerializer;

import java.util.UUID;

public interface BridgeReader<T> extends Handler<T> {
    void registerEventSubscriber(UUID publisherId, Subscriber subscriber);

    void registerRequestSubscriber(Class<? extends WebRequest> type, Subscriber subscriber);

    NoteefySerializer<T> getSerializer();
}
