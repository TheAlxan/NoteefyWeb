package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.Bridge;
import com.alxan.noteefy.web.bridge.TcpBridge;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

public class TcpBridgeFactory implements BridgeFactory<ByteBuff, ByteBuff> {
    private final TcpBridgeStreamFactory streamFactory;

    public TcpBridgeFactory(TcpBridgeStreamFactory aStreamFactory) {
        streamFactory = aStreamFactory;
    }

    @Override
    public Bridge<ByteBuff, ByteBuff> createBridge(DataSource<ByteBuff, ByteBuff> dataSource) {
        return new TcpBridge(streamFactory, dataSource);
    }
}
