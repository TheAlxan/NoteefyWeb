package com.alxan.noteefy.web;

import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.BridgeManager;
import com.alxan.noteefy.web.bridge.WebPublisherRegistry;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.common.PublisherAddress;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.exception.NotConnectedException;
import com.alxan.noteefy.web.server.Server;
import com.alxan.noteefy.web.server.ServerManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NoteefyWebUnitTest extends TestHelper {
    @Test
    public void shouldCreateServer() throws InterruptedException {
        initialLatch(1);
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        DataSourceInfo<Object, Object> dataSourceInfo = createTestDataSourceInfo();
        ServerManager serverManager = createTestServerManager();
        Server<Object, Object> server = createTestServer();
        Mockito.when(serverManager.createServer(Mockito.any())).thenReturn(server);
        countDownWhen(server).listen(Mockito.anyInt(), Mockito.anyString());
        noteefyWeb.setServerManager(serverManager);

        RemoteAddress remoteAddress = new RemoteAddress(port, host);
        noteefyWeb.createServer(dataSourceInfo, remoteAddress);

        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterToRemotePublisher() throws InterruptedException, NotConnectedException {
        initialLatch(1);
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        BridgeManager bridgeManager = Mockito.mock(BridgeManager.class);
        RemoteAddress remoteAddress = createTestRemoteAddress();
        PublisherAddress publisherAddress = Mockito.mock(PublisherAddress.class);
        Mockito.when(publisherAddress.getRemoteAddress()).thenReturn(remoteAddress);
        Mockito.when(bridgeManager.isNotRegistered(remoteAddress)).thenReturn(false);
        Subscriber subscriber = createMockSubscriber();
        countDownWhen(bridgeManager).registerSubscriberToBridge(publisherAddress, subscriber);
        noteefyWeb.setBridgeManager(bridgeManager);

        noteefyWeb.registerToRemotePublisher(publisherAddress, subscriber);

        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterWebPublisher() throws InterruptedException {
        initialLatch(1);
        WebPublisherRegistry webPublishRegistry = Mockito.mock(WebPublisherRegistry.class);
        countDownWhen(webPublishRegistry).registerPublisher(Mockito.any());
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        noteefyWeb.setWebPublisherRegistry(webPublishRegistry);
        Publisher publisher = Mockito.mock(Publisher.class);

        noteefyWeb.registerWebPublisher(publisher);

        assertLatchCount(0);
    }

    @Test
    public void shouldConnectToRemoteBridge() throws InterruptedException {
        initialLatch(1);
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        DataSourceInfo<Object, Object> dataSourceInfo = createTestDataSourceInfo();
        ServerManager serverManager = Mockito.mock(ServerManager.class);
        noteefyWeb.setServerManager(serverManager);
        RemoteAddress remoteAddress = createTestRemoteAddress();
        countDownWhen(serverManager).createConnection(Mockito.any(), Mockito.any());

        noteefyWeb.connectToRemoteBridge(dataSourceInfo, remoteAddress);

        assertLatchCount(0);
    }

    private ServerManager createTestServerManager() {
        return Mockito.mock(ServerManager.class);
    }

    @SuppressWarnings("unchecked")
    private Server<Object, Object> createTestServer() {
        return (Server<Object, Object>) Mockito.mock(Server.class);
    }
}
