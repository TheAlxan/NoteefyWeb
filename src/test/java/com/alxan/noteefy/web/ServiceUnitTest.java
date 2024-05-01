package com.alxan.noteefy.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServiceUnitTest extends TestHelper {
    @Test
    public void shouldRegisterItselfToRegistry() throws InterruptedException {
        initialLatch(1);

        ServiceDiscovery serviceDiscovery = Mockito.mock(ServiceDiscovery.class);
        countDownWhen(serviceDiscovery).registerService(Mockito.any());
        Service service = new Service(serviceDiscovery) {};

        service.setServiceDiscovery(serviceDiscovery);
        assertLatchCount(0);
    }
}
