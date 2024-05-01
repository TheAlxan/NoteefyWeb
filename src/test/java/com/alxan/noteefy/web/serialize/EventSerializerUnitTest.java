package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.event.Event;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

public class EventSerializerUnitTest {
    @Test
    public void shouldSerializeAndDeserialize() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Event.class, new EventSerializer());

        Event<?> event = new Event<>(256);
        event.addTag("Tag1");
        event.addTag("Tag2");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, event);
        output.close();

        Input input = new Input(outputStream.toByteArray());
        Event<?> newEvent = kryo.readObject(input, Event.class);

        Assertions.assertEquals(event.getContent(), newEvent.getContent());
        Assertions.assertEquals(event.getOrder(), newEvent.getOrder());
        Assertions.assertEquals(event.getUUID(), newEvent.getUUID());
        Assertions.assertEquals(event.getContentType(), newEvent.getContentType());
        Assertions.assertArrayEquals(event.getTags().toArray(), newEvent.getTags().toArray());
    }
}
