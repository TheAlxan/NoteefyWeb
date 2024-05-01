package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.Bridge;
import com.alxan.noteefy.web.bridge.datasource.DataSource;

public interface BridgeFactory<I, O> {
    public Bridge<I, O> createBridge(DataSource<I, O> dataSource);
}
