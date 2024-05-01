package com.alxan.noteefy.web.common;

import java.util.UUID;

public class PublisherAddress {
    private final RemoteAddress remoteAddress;
    private final UUID publisherId;

    public PublisherAddress(RemoteAddress aRemoteAddress, UUID aPublisherId) {
        remoteAddress = aRemoteAddress;
        publisherId = aPublisherId;
    }

    public RemoteAddress getRemoteAddress() {
        return remoteAddress;
    }

    public UUID getPublisherId() {
        return publisherId;
    }
}
