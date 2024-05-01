package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.web.event.WebEvent;

import java.util.UUID;

class WebPublisher extends Broadcaster {
    private final UUID uuid;

    public WebPublisher(UUID anUuid) {
        uuid = anUuid;
    }

    public static WebPublisher from(Publisher publisher) {
        WebPublisher webPublisher = new WebPublisher(publisher.getUUID());
        publisher.register(webPublisher);
        return webPublisher;
    }

    @Override
    public <T> void publish(Event<T> event) {
        WebEvent webEvent = new WebEvent(uuid, event);
        Event<WebEvent> newEvent = new Event<>(webEvent);
        super.publish(newEvent);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
