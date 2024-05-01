package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.event.CustomEvent;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventSerializer extends Serializer<Event<?>> {
    @Override
    public void write(Kryo kryo, Output output, Event<?> event) {
        output.writeLong(event.getOrder());
        output.writeString(event.getUUID().toString());
        output.writeString(event.getContentType().getCanonicalName());
        Set<String> tags = event.getTags();
        output.writeInt(tags.size());
        for (String tag : tags)
            output.writeString(tag);
        kryo.writeObject(output, event.getContent());
    }

    @Override
    public Event<?> read(Kryo kryo, Input input, Class<? extends Event<?>> aClass) {
        try {
            Long order = input.readLong();
            String uuidStr = input.readString();
            String inClassName = input.readString();
            Class<?> inClass = ClassLoader.getSystemClassLoader().loadClass(inClassName);
            int tagsSize = input.readInt();
            Set<String> tags = new HashSet<>(tagsSize);
            for (int i = 0; i < tagsSize; i++)
                tags.add(input.readString());
            Object content = kryo.readObject(new Input(input.readAllBytes()), inClass);
            CustomEvent<?> event = new CustomEvent<>(content);
            event.setOrder(order);
            event.setUuid(UUID.fromString(uuidStr));
            for (String tag : tags) {
                event.addTag(tag);
            }
            return event;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}