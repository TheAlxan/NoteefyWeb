package com.alxan.noteefy.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ServiceDiscoveryUnitTest {
    @Test
    public void shouldRegisterService() {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        Service service1 = new Service(serviceDiscovery) {};
        Service service2 = new Service(serviceDiscovery) {};

        Set<Service> services = serviceDiscovery.getService(Service.class);
        Assertions.assertTrue(services.contains(service1));
        Assertions.assertTrue(services.contains(service2));
    }
}
