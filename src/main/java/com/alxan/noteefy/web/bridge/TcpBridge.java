package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.factory.BridgeStreamFactory;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

public class TcpBridge extends Bridge<ByteBuff, ByteBuff> {

    public TcpBridge(BridgeStreamFactory<ByteBuff, ByteBuff> streamFactory, DataSource<ByteBuff, ByteBuff> dataSource) {
        super(streamFactory, dataSource);
    }

    @Override
    public SourceAddress getAddress() {
        return address;
    }
}
