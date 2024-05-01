package com.alxan.noteefy.web.server.tcp;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.server.Server;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

public class VertxServer implements Server<ByteBuff, ByteBuff> {
    private final Vertx vertx;
    private Handler<DataSource<ByteBuff, ByteBuff>> handler;

    public VertxServer(Vertx aVertx) {
        vertx = aVertx;
    }

    @Override
    public void listen(int port, String host) {
        NetServer server = vertx.createNetServer();
        io.vertx.core.Handler<NetSocket> vertxHandler = getVertxHandler();
        server.connectHandler(vertxHandler);
        server.listen(port, host);
    }

    @Override
    public void onConnect(DataSource<ByteBuff, ByteBuff> socket) {
        handler.handle(socket);
    }

    @Override
    public void setHandler(Handler<DataSource<ByteBuff, ByteBuff>> aHandler) {
        handler = aHandler;
    }

    @Override
    public void connect(RemoteAddress remoteAddress) {
        NetClient client = vertx.createNetClient();
        Future<NetSocket> future = client.connect(remoteAddress.getPort(), remoteAddress.getHost());
        future.onComplete(result -> {
            Socket socket = new VertxSocket(result.result());
            onConnect(socket);
        });
    }

    private io.vertx.core.Handler<NetSocket> getVertxHandler() {
        return netSocket -> {
            DataSource<ByteBuff, ByteBuff> socket = new VertxSocket(netSocket);
            onConnect(socket);
        };
    }
}
