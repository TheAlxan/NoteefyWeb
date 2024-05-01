package com.alxan.noteefy.web.event;

import com.alxan.noteefy.web.bridge.Bridge;

public class BridgeRegisterRequest {
    private final Bridge<?, ?> bridge;
    private final RegisterRequest registerRequest;

    public BridgeRegisterRequest(Bridge<?, ?> aBridge, RegisterRequest aRegisterRequest) {
        bridge = aBridge;
        registerRequest = aRegisterRequest;
    }

    public Bridge<?, ?> getBridge() {
        return bridge;
    }

    public RegisterRequest getRegisterRequest() {
        return registerRequest;
    }
}
