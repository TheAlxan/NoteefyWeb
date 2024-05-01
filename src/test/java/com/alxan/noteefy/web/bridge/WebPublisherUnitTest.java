package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.event.WebEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebPublisherUnitTest extends TestHelper {
    @Test
    public void shouldHaveOriginalPublishersId() {
        Broadcaster broadcaster = new Broadcaster();
        WebPublisher webPublisher = WebPublisher.from(broadcaster);

        Assertions.assertEquals(broadcaster.getUUID(), webPublisher.getUUID());
    }

    @Test
    public void shouldPublishEvent() throws InterruptedException {
        initialLatch(1);

        Broadcaster broadcaster = new Broadcaster();
        WebPublisher webPublisher = WebPublisher.from(broadcaster);

        Listener listener = createCountDownListenerFor(WebEvent.class);
        webPublisher.register(listener);

        broadcaster.publish(1);
        assertLatchCount(0);
    }
}
