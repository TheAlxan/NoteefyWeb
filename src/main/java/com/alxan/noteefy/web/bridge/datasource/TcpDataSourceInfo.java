package com.alxan.noteefy.web.bridge.datasource;

import com.alxan.noteefy.web.server.tcp.ByteBuff;

public class TcpDataSourceInfo extends DataSourceInfo<ByteBuff, ByteBuff> {
    private static final TcpDataSourceInfo dataSourceInfo = new TcpDataSourceInfo();

    private TcpDataSourceInfo() {}

    public static TcpDataSourceInfo getInstance() {
        return dataSourceInfo;
    }

    @Override
    public Class<ByteBuff> getReadSourceType() {
        return ByteBuff.class;
    }

    @Override
    public Class<ByteBuff> getWriteSourceType() {
        return ByteBuff.class;
    }

    @Override
    public String getBridgeType() {
        return "BridgeType.TCP";
    }
}
