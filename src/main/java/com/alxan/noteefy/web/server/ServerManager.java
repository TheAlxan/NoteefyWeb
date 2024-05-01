package com.alxan.noteefy.web.server;

import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.Service;
import com.alxan.noteefy.web.ServiceDiscovery;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.server.factory.ServerFactory;

public class ServerManager extends Service {
    private final AsyncChannel connectionListener;

    public ServerManager(Subscriber aConnectionListener, ServiceDiscovery aServiceDiscovery) {
        super(aServiceDiscovery);
        connectionListener = new AsyncChannel(aConnectionListener);
    }

    public <I, O> Server<I, O> createServer(ServerFactory<I, O> serverFactory) {
        Server<I, O> server = serverFactory.createServer();
        server.setHandler(this::connected);
        return server;
    }

    public <I, O> void createConnection(ServerFactory<I, O> serverFactory, RemoteAddress remoteAddress) {
        Server<I, O> server = createServer(serverFactory);
        server.setHandler(this::connected);
        server.connect(remoteAddress);
    }

    private void connected(DataSource<?, ?> dataSource) {
        connectionListener.onEvent(new Event<>(dataSource));
    }
}
