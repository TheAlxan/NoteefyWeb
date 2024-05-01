package com.alxan.noteefy.web.bridge.datasource;

public abstract class DataSourceInfo<I, O> {
    public abstract Class<I> getReadSourceType();

    public abstract Class<O> getWriteSourceType();

    public abstract String getBridgeType();
}
