package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.event.EventHandler;
import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.exception.MessageHandlerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class MessageDispatcher extends Listener {
    public MessageDispatcher() {
        registerHandlers();
    }

    private void registerHandlers() {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            boolean isHandler = method.isAnnotationPresent(MessageHandler.class);
            if (!isHandler) continue;
            registerMethod(method);
        }
    }

    private void registerMethod(Method method) {
        MessageHandler messageHandler = method.getAnnotation(MessageHandler.class);
        registerHandler(messageHandler.type(), method);
    }

    private <T> void registerHandler(Class<T> type, Method method) {
        doOnEvent(type, (EventHandler<T>) event -> {
            try {
                method.invoke(this, event.getContent());
            } catch (IllegalAccessException | InvocationTargetException e) {
                MessageHandlerException exception = new MessageHandlerException(e, getUUID());
                NotificationCenter.error(exception);
            }
        });
    }
}
