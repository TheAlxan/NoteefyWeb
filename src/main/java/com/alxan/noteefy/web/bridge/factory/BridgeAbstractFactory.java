package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.WebMessageTypeRegistry;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.serialize.KryoSerializer;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BridgeAbstractFactory {
    private static Map<String, Supplier<BridgeFactory<?, ?>>> map = null;

    public static void registerFactory(DataSourceInfo<?, ?> dataSourceInfo, Supplier<BridgeFactory<?, ?>> serverFactory) {
        getMap().put(dataSourceInfo.getBridgeType(), serverFactory);
    }

    @SuppressWarnings("unchecked")
    public static <I, O> BridgeFactory<I, O> getBridgeFactory(DataSourceInfo<I, O> dataSourceInfo) {
        return (BridgeFactory<I, O>) getMap().get(dataSourceInfo.getBridgeType()).get();
    }

    private static Map<String, Supplier<BridgeFactory<?, ?>>> getMap() {
        if (map == null) {
            map = new HashMap<>();
            setUpDefaultFactories();
        }
        return map;
    }

    private static void setUpDefaultFactories() {
        TcpDataSourceInfo tcpDataSourceInfo = TcpDataSourceInfo.getInstance();
        registerFactory(tcpDataSourceInfo, BridgeAbstractFactory::createTcpBridgeFactory);
    }

    private static BridgeFactory<ByteBuff, ByteBuff> createTcpBridgeFactory() {
        WebMessageTypeRegistry webMessageTypeRegistry = WebMessageTypeRegistry.getDefaultRegistry();
        NoteefySerializer<ByteBuff> serializations = new KryoSerializer();
        TcpBridgeStreamFactory streamFactory = new TcpBridgeStreamFactory(webMessageTypeRegistry, serializations);
        return new TcpBridgeFactory(streamFactory);
    }
}
