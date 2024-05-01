package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KryoSerializerUnitTest {
    @Test
    public void shouldSerializeAndDeserialize() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        Event<?> event = new Event<>(256);
        ByteBuff serializedBuff = kryoSerializer.serialize(event);
        Event<?> newEvent = kryoSerializer.deserialize(serializedBuff, Event.class);
        Assertions.assertEquals(event.getUUID(), newEvent.getUUID());
    }
}
