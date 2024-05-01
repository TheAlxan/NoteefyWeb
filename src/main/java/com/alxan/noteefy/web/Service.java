package com.alxan.noteefy.web;

public abstract class Service {
    protected ServiceDiscovery serviceDiscovery;

    public Service(ServiceDiscovery aServiceDiscovery) {
        serviceDiscovery = aServiceDiscovery;
        serviceDiscovery.registerService(this);
    }

    public void setServiceDiscovery(ServiceDiscovery aServiceDiscovery) {
        serviceDiscovery = aServiceDiscovery;
        serviceDiscovery.registerService(this);
    }
}
