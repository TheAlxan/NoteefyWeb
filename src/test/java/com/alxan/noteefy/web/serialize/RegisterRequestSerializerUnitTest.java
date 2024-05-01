package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.web.event.RegisterRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class RegisterRequestSerializerUnitTest {
    @Test
    public void shouldSerializeAndDeserialize() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(RegisterRequest.class, new RegisterRequestSerializer());

        UUID publisherId = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest(publisherId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, request);
        output.close();

        Input input = new Input(outputStream.toByteArray());
        RegisterRequest newRequest = kryo.readObject(input, RegisterRequest.class);

        Assertions.assertEquals(request.getPublisherId(), newRequest.getPublisherId());
        Assertions.assertEquals(request.getRequestTopic(), newRequest.getRequestTopic());
    }
}
