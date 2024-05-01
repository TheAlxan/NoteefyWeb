package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.*;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.bridge.parse.Parser;
import com.alxan.noteefy.web.serialize.NoteefySerializer;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

public class TcpBridgeStreamFactory implements BridgeStreamFactory<ByteBuff, ByteBuff> {
    private final WebMessageTypeRegistry webMessageTypeRegistry;
    private final NoteefySerializer<ByteBuff> serializer;

    public TcpBridgeStreamFactory(WebMessageTypeRegistry aWebMessageTypeRegistry, NoteefySerializer<ByteBuff> aSerializer) {
        webMessageTypeRegistry = aWebMessageTypeRegistry;
        serializer = aSerializer;
    }

    @Override
    public BridgeReader<ByteBuff> createReader(ReadSource<ByteBuff> readSource) {
        Parser parser = new Parser(webMessageTypeRegistry);
        parser.setSerializer(serializer);
        IncomingMessageDispatcher messageDispatcher = new IncomingMessageDispatcher();
        TcpBridgeReader tcpBridgeReader = new TcpBridgeReader(parser, messageDispatcher);
        readSource.setHandler(tcpBridgeReader);
        return tcpBridgeReader;
    }

    public BridgeWriter<ByteBuff> createWriter(WriteSource<ByteBuff> writeSource) {
        return new TcpBridgeWriter(writeSource, webMessageTypeRegistry);
    }
}
