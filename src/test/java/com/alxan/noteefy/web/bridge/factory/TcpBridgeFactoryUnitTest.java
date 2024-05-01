package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.Bridge;
import com.alxan.noteefy.web.bridge.TcpBridge;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TcpBridgeFactoryUnitTest extends TestHelper {
    @Test
    public void shouldCreateTcpBridge() {
        DataSourceInfo<ByteBuff, ByteBuff> dataSourceInfo = TcpDataSourceInfo.getInstance();
        BridgeFactory<ByteBuff, ByteBuff> bridgeFactory = BridgeAbstractFactory.getBridgeFactory(dataSourceInfo);
        DataSource<ByteBuff, ByteBuff> dataSource = createMockDataSource();

        Bridge<ByteBuff, ByteBuff> bridge = bridgeFactory.createBridge(dataSource);
        Assertions.assertEquals(TcpBridge.class, bridge.getClass());
    }
}
