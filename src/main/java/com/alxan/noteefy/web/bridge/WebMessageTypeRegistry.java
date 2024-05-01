package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.event.RegisterRequest;
import com.alxan.noteefy.web.event.WebEvent;
import com.alxan.noteefy.web.event.WebMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WebMessageTypeRegistry {
    private static WebMessageTypeRegistry defaultRegistry;
    private final Map<Integer, WebMessageTypeMeta> map = new HashMap<>();
    private final Map<String, Integer> reverseMap = new HashMap<>();
    private final AtomicInteger typeCounter = new AtomicInteger(0);

    public static WebMessageTypeRegistry getDefaultRegistry() {
        if (defaultRegistry == null) {
            defaultRegistry = new WebMessageTypeRegistry();
            initializeDefaultRegistry();
        }
        return defaultRegistry;
    }

    public static void initializeDefaultRegistry() {
        defaultRegistry.register(WebEvent.class);
        defaultRegistry.register(RegisterRequest.class);
    }

    public void register(WebMessageTypeMeta typeMeta) {
        int currentId = typeCounter.getAndIncrement();
        map.put(currentId, typeMeta);
        reverseMap.put(typeMeta.getTypeName(), currentId);
    }

    public void register(Class<? extends WebMessage> typeClass) {
        if (!typeClass.isAnnotationPresent(WebMessageType.class))
            return;
        String typeName = typeClass.getCanonicalName();
        WebMessageTypeMeta typeMeta = new WebMessageTypeMeta(typeName);
        register(typeMeta);
    }

    public WebMessageTypeMeta getTypeMeta(int number) {
        return map.get(number);
    }

    public int getTypeId(Class<? extends WebMessage> type) {
        return reverseMap.get(type.getCanonicalName());
    }
}