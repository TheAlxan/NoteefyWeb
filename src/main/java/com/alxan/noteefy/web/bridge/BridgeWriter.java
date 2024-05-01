package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.event.WebMessage;
import com.alxan.noteefy.web.serialize.NoteefySerializer;

public interface BridgeWriter<O> {
    public void write(O data);

    public void writeMessage(WebMessage webMessage);

    public NoteefySerializer<O> getSerializer();

    default public WebMessageTypeRegistry getTypeRegistry() {
        return WebMessageTypeRegistry.getDefaultRegistry();
    }

    public Subscriber getWriteSubscriber();
}
