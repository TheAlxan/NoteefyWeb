package com.alxan.noteefy.web.event;

public abstract class WebRequest extends WebMessage {
    public String getRequestTopic() {
        return this.getClass().getCanonicalName();
    }
}
