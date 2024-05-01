package com.alxan.noteefy.web.server.tcp;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.web.bridge.SourceAddress;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.common.RemoteAddress;

public interface Socket extends DataSource<ByteBuff, ByteBuff>, ReadSource<ByteBuff>, WriteSource<ByteBuff> {

    void itemReceived(ByteBuff buffer);

    void setHandler(Handler<ByteBuff> handler);

    void write(ByteBuff buffer);

    RemoteAddress getRemoteAddress();

    @Override
    default SourceAddress getAddress() {
        return getRemoteAddress();
    }
}
