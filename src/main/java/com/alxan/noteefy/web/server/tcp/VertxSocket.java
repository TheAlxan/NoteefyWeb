package com.alxan.noteefy.web.server.tcp;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.common.RemoteAddress;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

public class VertxSocket implements Socket {
    private final NetSocket netSocket;
    private final DataSourceInfo<ByteBuff, ByteBuff> dataSourceInfo;
    private Handler<ByteBuff> handler;

    public VertxSocket(NetSocket aNetSocket) {
        netSocket = aNetSocket;
        netSocket.handler(buffer -> itemReceived(new VertxBuff(buffer)));
        dataSourceInfo = TcpDataSourceInfo.getInstance();
    }

    @Override
    public void itemReceived(ByteBuff buffer) {
        handler.handle(buffer);
    }

    @Override
    public void setHandler(Handler<ByteBuff> aHandler) {
        handler = aHandler;
    }

    @Override
    public synchronized void write(ByteBuff buffer) {
        netSocket.write(Buffer.buffer(buffer.getBytes()));
    }

    @Override
    public RemoteAddress getRemoteAddress() {
        SocketAddress socketAddress = netSocket.remoteAddress();
        int port = socketAddress.port();
        String host = socketAddress.host();
        return new RemoteAddress(port, host);
    }

    @Override
    public ReadSource<ByteBuff> getReadSource() {
        return this;
    }

    @Override
    public WriteSource<ByteBuff> getWriteSource() {
        return this;
    }

    @Override
    public DataSourceInfo<ByteBuff, ByteBuff> getDataSourceInfo() {
        return dataSourceInfo;
    }
}
