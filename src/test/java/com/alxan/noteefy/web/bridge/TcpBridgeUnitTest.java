package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.factory.BridgeStreamFactory;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TcpBridgeUnitTest extends TestHelper {
    @Test
    public void shouldReturnAddress() {
        BridgeStreamFactory<ByteBuff, ByteBuff> streamFactory = createTestStreamFactory();
        DataSource<ByteBuff, ByteBuff> dataSource = createMockDataSource();
        SourceAddress sourceAddress = createTestRemoteAddress();
        Mockito.when(dataSource.getAddress()).thenReturn(sourceAddress);
        TcpBridge bridge = new TcpBridge(streamFactory, dataSource);

        Assertions.assertEquals(sourceAddress, bridge.getAddress());
    }

    private BridgeStreamFactory<ByteBuff, ByteBuff> createTestStreamFactory() {
        BridgeStreamFactory<ByteBuff, ByteBuff> streamFactory = Mockito.mock(BridgeStreamFactory.class);
        BridgeReader<ByteBuff> bridgeReader = Mockito.mock(BridgeReader.class);
        BridgeWriter<ByteBuff> bridgeWriter = Mockito.mock(BridgeWriter.class);
        Mockito.when(streamFactory.createReader(Mockito.any())).thenReturn(bridgeReader);
        Mockito.when(streamFactory.createWriter(Mockito.any())).thenReturn(bridgeWriter);
        Mockito.doNothing().when(bridgeReader).registerRequestSubscriber(Mockito.any(), Mockito.any());
        return streamFactory;
    }
}
