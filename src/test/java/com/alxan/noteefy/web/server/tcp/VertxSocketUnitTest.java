package com.alxan.noteefy.web.server.tcp;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.SourceAddress;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.common.RemoteAddress;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class VertxSocketUnitTest extends TestHelper {
    @Test
    public void shouldSetHandler() throws InterruptedException {
        initialLatch(1);
        Handler<ByteBuff> handler = item -> countDownLatch();

        NetSocket netSocket = Mockito.mock(NetSocket.class);
        VertxSocket vertxSocket = new VertxSocket(netSocket);
        vertxSocket.setHandler(handler);

        vertxSocket.itemReceived(new VertxBuff());
        assertLatchCount(0);
    }

    @Test
    public void shouldReturnRemoteAddress() {
        SocketAddress socketAddress = Mockito.mock(SocketAddress.class);
        Mockito.when(socketAddress.port()).thenReturn(port);
        Mockito.when(socketAddress.host()).thenReturn(host);

        NetSocket netSocket = Mockito.mock(NetSocket.class);
        Mockito.when(netSocket.remoteAddress()).thenReturn(socketAddress);

        Socket vertxSocket = new VertxSocket(netSocket);
        SourceAddress sourceAddress = vertxSocket.getAddress();
        RemoteAddress remoteAddress = vertxSocket.getRemoteAddress();

        Assertions.assertEquals(port, remoteAddress.getPort());
        Assertions.assertEquals(host, remoteAddress.getHost());
        Assertions.assertEquals(sourceAddress, remoteAddress);
    }

    @Test
    public void shouldWriteToSocket() throws InterruptedException {
        initialLatch(1);

        NetSocket netSocket = Mockito.mock(NetSocket.class);
        countDownWhen(netSocket).write(Mockito.any(Buffer.class));

        VertxSocket vertxSocket = new VertxSocket(netSocket);
        vertxSocket.write(new VertxBuff());

        assertLatchCount(0);
    }

    @Test
    public void shouldReturnReadSource() {
        NetSocket netSocket = Mockito.mock(NetSocket.class);
        VertxSocket vertxSocket = new VertxSocket(netSocket);

        ReadSource<?> readSource = vertxSocket.getReadSource();
        Assertions.assertEquals(vertxSocket, readSource);
    }

    @Test
    public void shouldReturnWriteSource() {
        NetSocket netSocket = Mockito.mock(NetSocket.class);
        VertxSocket vertxSocket = new VertxSocket(netSocket);

        WriteSource<?> writeSource = vertxSocket.getWriteSource();
        Assertions.assertEquals(vertxSocket, writeSource);
    }
}
