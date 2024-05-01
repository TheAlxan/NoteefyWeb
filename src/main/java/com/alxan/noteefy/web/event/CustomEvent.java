package com.alxan.noteefy.web.event;

import com.alxan.noteefy.event.Event;

import java.util.UUID;

public class CustomEvent<T> extends Event<T> {
    private Long order;
    private UUID uuid;

    public CustomEvent(T aContent) {
        super(aContent);
    }

    public void setUuid(UUID anUuid) {
        uuid = anUuid;
    }

    @Override
    public Long getOrder() {
        return order;
    }

    public void setOrder(Long anOrder) {
        order = anOrder;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
