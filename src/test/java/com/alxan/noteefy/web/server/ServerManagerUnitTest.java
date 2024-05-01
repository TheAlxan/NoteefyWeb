package com.alxan.noteefy.web.server;

import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.ServiceDiscovery;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.server.factory.ServerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServerManagerUnitTest extends TestHelper {
    @Test
    public void shouldCreateServer() {
        Server<Object, Object> server = Mockito.mock(Server.class);
        ServerFactory<Object, Object> serverFactory = Mockito.mock(ServerFactory.class);
        Mockito.when(serverFactory.createServer()).thenReturn(server);
        ServiceDiscovery serviceDiscovery = Mockito.mock(ServiceDiscovery.class);
        ServerManager serverManager = new ServerManager(new Listener(), serviceDiscovery);

        Server<Object, Object> createdServer = serverManager.createServer(serverFactory);

        Assertions.assertEquals(server, createdServer);
    }

    @Test
    public void shouldCreateConnection() throws InterruptedException {
        initialLatch(1);
        Server<Object, Object> server = Mockito.mock(Server.class);
        countDownWhen(server).connect(Mockito.any());
        ServerFactory<Object, Object> serverFactory = Mockito.mock(ServerFactory.class);
        Mockito.when(serverFactory.createServer()).thenReturn(server);
        ServiceDiscovery serviceDiscovery = Mockito.mock(ServiceDiscovery.class);
        ServerManager serverManager = new ServerManager(new Listener(), serviceDiscovery);
        RemoteAddress remoteAddress = Mockito.mock(RemoteAddress.class);

        serverManager.createConnection(serverFactory, remoteAddress);

        assertLatchCount(0);
    }
}
