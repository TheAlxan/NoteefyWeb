package com.alxan.noteefy.web.event;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.bridge.WebMessageType;

import java.util.UUID;

@WebMessageType
public class WebEvent extends WebMessage {
    private final UUID publisherId;
    private final Event<?> event;

    public WebEvent(UUID aPublisherId, Event<?> anEvent) {
        publisherId = aPublisherId;
        event = anEvent;
    }

    public UUID getPublisherId() {
        return publisherId;
    }

    public Event<?> getEvent() {
        return event;
    }
}
