package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.*;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.serialize.KryoSerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TcpBridgeStreamFactoryUnitTest {
    @Test
    public void shouldCreateReader() {
        WebMessageTypeRegistry defaultRegistry = WebMessageTypeRegistry.getDefaultRegistry();
        KryoSerializer aSerializations = new KryoSerializer();
        TcpBridgeStreamFactory streamFactory = new TcpBridgeStreamFactory(defaultRegistry, aSerializations);

        ReadSource<ByteBuff> readSource = Mockito.mock(ReadSource.class);
        Mockito.doNothing().when(readSource).setHandler(Mockito.any());
        BridgeReader<?> bridgeReader = streamFactory.createReader(readSource);
        Assertions.assertEquals(TcpBridgeReader.class, bridgeReader.getClass());
    }

    @Test
    public void shouldCreateWriter() {
        WebMessageTypeRegistry defaultRegistry = WebMessageTypeRegistry.getDefaultRegistry();
        KryoSerializer aSerializations = new KryoSerializer();
        TcpBridgeStreamFactory streamFactory = new TcpBridgeStreamFactory(defaultRegistry, aSerializations);

        WriteSource<ByteBuff> writeSource = Mockito.mock(WriteSource.class);
        BridgeWriter<?> bridgeWriter = streamFactory.createWriter(writeSource);
        Assertions.assertEquals(TcpBridgeWriter.class, bridgeWriter.getClass());
    }
}
