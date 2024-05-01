package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BridgeAbstractFactoryUnitTest {
    @Test
    public void shouldCreateTcpBridgeFactory() {
        TcpDataSourceInfo dataSourceInfo = TcpDataSourceInfo.getInstance();
        BridgeFactory<?, ?> tcpBridgeFactory = BridgeAbstractFactory.getBridgeFactory(dataSourceInfo);

        Assertions.assertEquals(TcpBridgeFactory.class, tcpBridgeFactory.getClass());
    }
}
