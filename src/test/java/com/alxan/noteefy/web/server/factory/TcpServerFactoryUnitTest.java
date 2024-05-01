package com.alxan.noteefy.web.server.factory;

import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.server.Server;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TcpServerFactoryUnitTest {
    @Test
    public void shouldCreateServer() {
        DataSourceInfo<ByteBuff, ByteBuff> dataSourceInfo = TcpDataSourceInfo.getInstance();
        ServerFactory<ByteBuff, ByteBuff> serverFactory = ServerAbstractFactory.getServerFactory(dataSourceInfo);
        Server<ByteBuff, ByteBuff> server = serverFactory.createServer();


        Assertions.assertEquals(VertxServer.class, server.getClass());
    }

    @Test
    public void shouldStoreDataSourceInfo() {
        DataSourceInfo<ByteBuff, ByteBuff> dataSourceInfo = TcpDataSourceInfo.getInstance();
        TcpServerFactory serverFactory = new TcpServerFactory();
        Assertions.assertEquals(dataSourceInfo, serverFactory.getDataSourceInfo());
    }
}
