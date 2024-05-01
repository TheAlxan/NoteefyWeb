package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.event.WebEvent;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class WebEventSerializerUnitTest {
    @Test
    public void shouldSerializeAndDeserialize() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Event.class, new EventSerializer());
        kryo.register(WebEvent.class, new WebEventSerializer());

        UUID publisherId = UUID.randomUUID();
        Event<?> event = new Event<>(256);
        WebEvent webEvent = new WebEvent(publisherId, event);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, webEvent);
        output.close();

        Input input = new Input(outputStream.toByteArray());
        WebEvent newWebEvent = kryo.readObject(input, WebEvent.class);

        Assertions.assertEquals(webEvent.getEvent().getUUID(), newWebEvent.getEvent().getUUID());
        Assertions.assertEquals(webEvent.getPublisherId(), newWebEvent.getPublisherId());
    }
}
