package com.alxan.noteefy.web.bridge.factory;

import com.alxan.noteefy.web.bridge.BridgeReader;
import com.alxan.noteefy.web.bridge.BridgeWriter;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;

public interface BridgeStreamFactory<I, O> {
    public BridgeReader<I> createReader(ReadSource<I> readSource);

    public BridgeWriter<O> createWriter(WriteSource<O> writeSource);
}
