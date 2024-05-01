package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.web.ServiceDiscovery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebPublisherRegistryUnitTest {
    @Test
    public void shouldRegisterPublisher() {
        WebPublisherRegistry webPublisherRegistry = new WebPublisherRegistry(new ServiceDiscovery());
        Broadcaster broadcaster = new Broadcaster();
        webPublisherRegistry.registerPublisher(broadcaster);

        WebPublisher webPublisher = webPublisherRegistry.getWebPublisher(broadcaster.getUUID());

        Assertions.assertEquals(broadcaster.getUUID(), webPublisher.getUUID());
    }
}
