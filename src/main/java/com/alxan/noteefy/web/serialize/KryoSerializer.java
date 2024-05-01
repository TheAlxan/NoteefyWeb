package com.alxan.noteefy.web.serialize;


import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements NoteefySerializer<ByteBuff> {
    private static final Pool<Kryo> kryoPool = new Pool<>(true, true) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.register(Event.class, new EventSerializer());
            kryo.register(WebEvent.class, new WebEventSerializer());
            kryo.register(RegisterRequest.class, new RegisterRequestSerializer());
            return kryo;
        }
    };

    @Override
    public ByteBuff serialize(Object object) {
        Kryo kryo = kryoPool.obtain();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        kryo.writeObject(output, object);
        output.close();
        kryoPool.free(kryo);
        return new VertxBuff(stream.toByteArray());
    }

    @Override
    public <T> T deserialize(ByteBuff buffer, Class<T> type) {
        Kryo kryo = kryoPool.obtain();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.getBytes());
        Input input = new Input(inputStream);
        Object obj = kryo.readObject(input, type);
        input.close();
        kryoPool.free(kryo);
        return type.cast(obj);
    }
}

