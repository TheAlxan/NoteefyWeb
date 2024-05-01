package com.alxan.noteefy.web;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.BridgeManager;
import com.alxan.noteefy.web.bridge.WebPublisherRegistry;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.factory.BridgeAbstractFactory;
import com.alxan.noteefy.web.bridge.factory.BridgeFactory;
import com.alxan.noteefy.web.common.PublisherAddress;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.exception.NotConnectedException;
import com.alxan.noteefy.web.server.Server;
import com.alxan.noteefy.web.server.ServerManager;
import com.alxan.noteefy.web.server.factory.ServerAbstractFactory;
import com.alxan.noteefy.web.server.factory.ServerFactory;
import com.alxan.noteefy.web.server.tcp.VertxSocket;

public class NoteefyWeb extends BaseIdentifiable {
    private final ServiceDiscovery serviceDiscovery;
    private WebPublisherRegistry webPublishRegistry;
    private BridgeManager bridgeManager;
    private ServerManager serverManager;

    public NoteefyWeb() {
        Listener connectionListener = new Listener();
        connectionListener.doOnEvent(VertxSocket.class, this::connected);
        serviceDiscovery = new ServiceDiscovery();
        serverManager = new ServerManager(connectionListener, serviceDiscovery);
        webPublishRegistry = new WebPublisherRegistry(serviceDiscovery);
        bridgeManager = new BridgeManager(new Listener(), serviceDiscovery);
    }

    public <I, O> void createServer(DataSourceInfo<I, O> dataSourceInfo, RemoteAddress remoteAddress) {
        ServerFactory<I, O> serverFactory = ServerAbstractFactory.getServerFactory(dataSourceInfo);
        Server<I, O> server = serverManager.createServer(serverFactory);
        server.listen(remoteAddress.getPort(), remoteAddress.getHost());
    }

    public <I, O> void connectToRemoteBridge(DataSourceInfo<I, O> dataSourceInfo, RemoteAddress remoteAddress) {
        ServerFactory<I, O> serverFactory = ServerAbstractFactory.getServerFactory(dataSourceInfo);
        serverManager.createConnection(serverFactory, remoteAddress);
    }

    public void registerToRemotePublisher(PublisherAddress address, Subscriber subscriber) throws NotConnectedException {
        RemoteAddress remoteAddress = address.getRemoteAddress();
        if (bridgeManager.isNotRegistered(remoteAddress)) {
            NotConnectedException notConnectedException = new NotConnectedException(getUUID());
            NotificationCenter.error(notConnectedException);
            throw notConnectedException;
        }
        bridgeManager.registerSubscriberToBridge(address, subscriber);
    }

    public void registerWebPublisher(Publisher publisher) {
        webPublishRegistry.registerPublisher(publisher);
    }

    public void setWebPublisherRegistry(WebPublisherRegistry webPublishRegistry) {
        this.webPublishRegistry = webPublishRegistry;
        webPublishRegistry.setServiceDiscovery(serviceDiscovery);
    }

    public void setBridgeManager(BridgeManager bridgeManager) {
        this.bridgeManager = bridgeManager;
        bridgeManager.setServiceDiscovery(serviceDiscovery);
    }

    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
        serverManager.setServiceDiscovery(serviceDiscovery);
    }

    private <I, O, T extends DataSource<I, O>> void connected(Event<T> event) {
        DataSource<I, O> dataSource = event.getContent();
        BridgeFactory<I, O> bridgeFactory = BridgeAbstractFactory.getBridgeFactory(dataSource.getDataSourceInfo());
        bridgeManager.createBridge(bridgeFactory, dataSource);
    }
}
