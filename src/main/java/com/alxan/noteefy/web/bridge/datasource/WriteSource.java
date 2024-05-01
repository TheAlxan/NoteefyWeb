package com.alxan.noteefy.web.bridge.datasource;

public interface WriteSource<T> {
    public void write(T data);
}
