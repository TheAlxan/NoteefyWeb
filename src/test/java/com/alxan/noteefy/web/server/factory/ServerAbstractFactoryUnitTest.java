package com.alxan.noteefy.web.server.factory;

import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Supplier;

public class ServerAbstractFactoryUnitTest {
    @Test
    public void shouldCreateTcpServerFactory() {
        TcpDataSourceInfo dataSourceInfo = TcpDataSourceInfo.getInstance();
        ServerFactory<?, ?> serverFactory = ServerAbstractFactory.getServerFactory(dataSourceInfo);

        Assertions.assertEquals(TcpServerFactory.class, serverFactory.getClass());
    }

    @Test
    public void shouldRegisterCustomServerFactory() {
        DataSourceInfo<?, ?> dataSourceInfo = Mockito.mock(DataSourceInfo.class);
        Mockito.when(dataSourceInfo.getBridgeType()).thenReturn("SomeBridgeType");
        ServerFactory<?, ?> factory = Mockito.mock(ServerFactory.class);
        Mockito.when(factory.getDataSourceInfo()).thenReturn((DataSourceInfo) dataSourceInfo);
        Supplier<ServerFactory<?, ?>> factoryCreator = () -> factory;
        ServerAbstractFactory.registerFactory(dataSourceInfo, factoryCreator);

        ServerFactory<?, ?> fetchedFactory = ServerAbstractFactory.getServerFactory(dataSourceInfo);

        Assertions.assertEquals(dataSourceInfo, fetchedFactory.getDataSourceInfo());
    }
}
