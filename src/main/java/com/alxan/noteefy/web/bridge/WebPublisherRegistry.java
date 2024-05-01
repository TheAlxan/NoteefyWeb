package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.web.Service;
import com.alxan.noteefy.web.ServiceDiscovery;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebPublisherRegistry extends Service {
    private final Map<UUID, WebPublisher> map = new HashMap<>();

    public WebPublisherRegistry(ServiceDiscovery aServiceDiscovery) {
        super(aServiceDiscovery);
    }

    public void registerPublisher(Publisher publisher) {
        WebPublisher webPublisher = WebPublisher.from(publisher);
        registerWebPublisher(webPublisher);
    }

    private void registerWebPublisher(WebPublisher webPublisher) {
        map.put(webPublisher.getUUID(), webPublisher);
    }

    public WebPublisher getWebPublisher(UUID id) {
        return map.getOrDefault(id, null);
    }
}
