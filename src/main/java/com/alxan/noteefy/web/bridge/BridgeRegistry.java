package com.alxan.noteefy.web.bridge;

import java.util.HashMap;
import java.util.Map;

public class BridgeRegistry {
    private final Map<SourceAddress, Bridge<?, ?>> map = new HashMap<>();

    public void registerBridge(SourceAddress address, Bridge<?, ?> bridge) {
        map.put(address, bridge);
    }

    public Bridge<?, ?> getBridge(SourceAddress address) {
        return map.get(address);
    }

    public boolean isRegistered(SourceAddress address) {
        return map.containsKey(address);
    }

    public boolean isNotRegistered(SourceAddress address) {
        return !isRegistered(address);
    }
}
