package com.alxan.noteefy.web.common;

import com.alxan.noteefy.web.bridge.SourceAddress;

import java.util.Objects;

public class RemoteAddress implements SourceAddress {
    private final int port;
    private final String host;

    public RemoteAddress(int aPort, String aHost) {
        port = aPort;
        host = aHost;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoteAddress that)) return false;
        return port == that.port && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, host);
    }
}
