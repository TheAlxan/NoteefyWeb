package com.alxan.noteefy.web.server.factory;

import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.server.Server;

public interface ServerFactory<I, O> {
    Server<I, O> createServer();

    DataSourceInfo<I, O> getDataSourceInfo();
}
