package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.event.WebMessage;

public class WebMessageTypeMeta {
    private final String classType;

    public WebMessageTypeMeta(String aClassType) {
        this.classType = aClassType;
    }

    public String getTypeName() {
        return classType;
    }

    public Class<? extends WebMessage> getTypeClass() throws ClassNotFoundException {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(classType);
        return type.asSubclass(WebMessage.class);
    }
}