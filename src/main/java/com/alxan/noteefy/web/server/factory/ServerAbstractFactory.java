package com.alxan.noteefy.web.server.factory;

import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServerAbstractFactory {
    private static Map<String, Supplier<ServerFactory<?, ?>>> map = null;

    public static void registerFactory(DataSourceInfo<?, ?> dataSourceInfo, Supplier<ServerFactory<?, ?>> serverFactory) {
        getMap().put(dataSourceInfo.getBridgeType(), serverFactory);
    }

    @SuppressWarnings("unchecked")
    public static <I, O> ServerFactory<I, O> getServerFactory(DataSourceInfo<I, O> dataSourceInfo) {
        return (ServerFactory<I, O>) getMap().get(dataSourceInfo.getBridgeType()).get();
    }

    private static Map<String, Supplier<ServerFactory<?, ?>>> getMap() {
        if (map == null) {
            map = new HashMap<>();
            setUpDefaultFactories();
        }
        return map;
    }

    private static void setUpDefaultFactories() {
        TcpDataSourceInfo tcpDataSourceInfo = TcpDataSourceInfo.getInstance();
        registerFactory(tcpDataSourceInfo, ServerAbstractFactory::createTcpServerFactory);
    }

    private static TcpServerFactory createTcpServerFactory() {
        return new TcpServerFactory();
    }
}
