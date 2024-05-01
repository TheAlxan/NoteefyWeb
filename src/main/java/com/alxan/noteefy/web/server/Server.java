package com.alxan.noteefy.web.server;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.common.RemoteAddress;

public interface Server<I, O> {
    void listen(int port, String host);

    void onConnect(DataSource<I, O> dataSource);

    void setHandler(Handler<DataSource<I, O>> handler);

    void connect(RemoteAddress remoteAddress);
}
