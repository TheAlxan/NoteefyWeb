package com.alxan.noteefy.web.server.tcp;

import com.alxan.noteefy.web.TestHelper;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.common.RemoteAddress;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class VertxServerUnitTest extends TestHelper {
    @Test
    public void shouldListen() throws InterruptedException {
        initialLatch(1);

        NetSocket netSocket = Mockito.mock(NetSocket.class);
        NetServer netServer = Mockito.mock(NetServer.class);
        Mockito.doAnswer(i -> {
            ((Handler<NetSocket>) i.getArgument(0)).handle(netSocket);
            return null;
        }).when(netServer).connectHandler(Mockito.any());
        Vertx vertx = Mockito.mock(Vertx.class);
        Mockito.when(vertx.createNetServer()).thenReturn(netServer);

        VertxServer vertxServer = new VertxServer(vertx);
        vertxServer.setHandler(item -> countDownLatch());
        vertxServer.listen(port, host);

        assertLatchCount(0);
    }

    @Test
    public void shouldCreateSocket() throws InterruptedException {
        initialLatch(0);

        com.alxan.noteefy.event.Handler<DataSource<ByteBuff, ByteBuff>> handler = item -> countDownLatch();

        NetClient netClient = Mockito.mock(NetClient.class);
        NetSocket netSocket = Mockito.mock(NetSocket.class);
        Mockito.when(netClient.connect(Mockito.anyInt(), Mockito.anyString())).thenReturn(Future.succeededFuture(netSocket));
        RemoteAddress remoteAddress = createTestRemoteAddress();
        Vertx vertx = Mockito.mock(Vertx.class);
        Mockito.when(vertx.createNetClient()).thenReturn(netClient);
        VertxServer vertxServer = new VertxServer(vertx);
        vertxServer.setHandler(handler);

        vertxServer.connect(remoteAddress);
        assertLatchCount(0);
    }

    @Test
    public void shouldReturnDataSourceInfo() {
        NetSocket netSocket = Mockito.mock(NetSocket.class);
        VertxSocket vertxSocket = new VertxSocket(netSocket);
        Assertions.assertEquals(TcpDataSourceInfo.class, vertxSocket.getDataSourceInfo().getClass());
    }
}
