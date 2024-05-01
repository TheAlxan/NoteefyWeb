package com.alxan.noteefy.web.bridge.datasource;

import com.alxan.noteefy.web.bridge.SourceAddress;

public interface DataSource<I, O> {
    public ReadSource<I> getReadSource();

    public WriteSource<O> getWriteSource();

    public SourceAddress getAddress();

    public DataSourceInfo<I, O> getDataSourceInfo();
}
