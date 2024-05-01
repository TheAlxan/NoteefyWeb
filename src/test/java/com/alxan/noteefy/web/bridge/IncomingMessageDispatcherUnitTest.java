package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class IncomingMessageDispatcherUnitTest extends TestHelper {
    @Test
    public void shouldRegisterToEvents() throws InterruptedException {
        initialLatch(1);
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        Listener listener = createCountDownListenerFor(Integer.class);
        UUID publisherId = UUID.randomUUID();
        messageDispatcher.registerToEvents(publisherId, listener);
        Event<?> event = new Event<>(256);
        WebEvent webEvent = new WebEvent(publisherId, event);

        messageDispatcher.onEvent(new Event<>(webEvent));

        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterToRequests() throws InterruptedException {
        initialLatch(1);

        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        Listener listener = createCountDownListenerFor(RegisterRequest.class);
        messageDispatcher.registerToRequests(RegisterRequest.class, listener);

        UUID publisherId = UUID.randomUUID();
        RegisterRequest request = new RegisterRequest(publisherId);
        messageDispatcher.onEvent(new Event<>(request));
        assertLatchCount(0);
    }
}
