package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.event.WebMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageHandler {
    public Class<? extends WebMessage> type();
}
