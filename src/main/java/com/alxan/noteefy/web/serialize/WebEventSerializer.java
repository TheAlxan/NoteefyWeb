package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.event.WebEvent;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.UUID;

public class WebEventSerializer extends Serializer<WebEvent> {
    @Override
    public void write(Kryo kryo, Output output, WebEvent webEvent) {
        Event<?> event = webEvent.getEvent();
        output.writeString(webEvent.getPublisherId().toString());
        kryo.writeObject(output, event);
    }

    @Override
    public WebEvent read(Kryo kryo, Input input, Class<? extends WebEvent> aClass) {
        String uuidStr = input.readString();
        UUID publisherId = UUID.fromString(uuidStr);
        Event<?> event = kryo.readObject(input, Event.class);
        return new WebEvent(publisherId, event);
    }
}
