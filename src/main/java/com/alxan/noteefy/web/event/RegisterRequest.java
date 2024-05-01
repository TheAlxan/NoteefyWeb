package com.alxan.noteefy.web.event;

import com.alxan.noteefy.web.bridge.WebMessageType;

import java.util.UUID;

@WebMessageType
public class RegisterRequest extends WebRequest {
    private final UUID publisherId;

    public RegisterRequest(UUID aPublisherId) {
        publisherId = aPublisherId;
    }

    public UUID getPublisherId() {
        return publisherId;
    }
}
