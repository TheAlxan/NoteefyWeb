package com.alxan.noteefy.web.server.factory;

import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.server.Server;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxServer;
import io.vertx.core.Vertx;

public class TcpServerFactory implements ServerFactory<ByteBuff, ByteBuff> {
    private static final Vertx vertx = Vertx.vertx();

    public TcpServerFactory() {}

    @Override
    public Server<ByteBuff, ByteBuff> createServer() {
        return new VertxServer(vertx);
    }

    @Override
    public DataSourceInfo<ByteBuff, ByteBuff> getDataSourceInfo() {
        return TcpDataSourceInfo.getInstance();
    }
}
