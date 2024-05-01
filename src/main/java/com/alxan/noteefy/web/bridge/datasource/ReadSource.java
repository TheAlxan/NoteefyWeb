package com.alxan.noteefy.web.bridge.datasource;

import com.alxan.noteefy.event.Handler;

public interface ReadSource<T> {
    void setHandler(Handler<T> handler);
}
