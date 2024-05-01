package com.alxan.noteefy.web.serialize;

import com.alxan.noteefy.web.event.RegisterRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.UUID;

public class RegisterRequestSerializer extends Serializer<RegisterRequest> {
    @Override
    public void write(Kryo kryo, Output output, RegisterRequest request) {
        output.writeString(request.getPublisherId().toString());
    }

    @Override
    public RegisterRequest read(Kryo kryo, Input input, Class<? extends RegisterRequest> aClass) {
        String uuidStr = input.readString();
        UUID publisherId = UUID.fromString(uuidStr);

        return new RegisterRequest(publisherId);
    }
}