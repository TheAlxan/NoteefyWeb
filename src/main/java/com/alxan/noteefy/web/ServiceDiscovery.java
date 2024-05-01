package com.alxan.noteefy.web;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ServiceDiscovery {
    private final Set<Service> services = new HashSet<>();

    public void registerService(Service service) {
        services.add(service);
    }

    public <T extends Service> Set<T> getService(Class<? extends T> serviceType) {
        Set<T> result = new HashSet<>();
        Stream<Service> stream = services.parallelStream().filter(serviceType::isInstance);
        for (Object service : stream.toArray()) {
            result.add(serviceType.cast(service));
        }
        return result;
    }
}
