package com.alxan.noteefy.web;

import com.alxan.noteefy.notification.NotificationListener;
import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;
import com.alxan.noteefy.web.bridge.SourceAddress;
import com.alxan.noteefy.web.bridge.datasource.DataSource;
import com.alxan.noteefy.web.bridge.datasource.DataSourceInfo;
import com.alxan.noteefy.web.bridge.datasource.ReadSource;
import com.alxan.noteefy.web.bridge.datasource.WriteSource;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.server.factory.ServerAbstractFactory;
import com.alxan.noteefy.web.server.factory.ServerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TestHelper {
    protected final int port = 8080;
    protected final String host = "127.0.0.1";
    private final Set<NotificationListener> notificationListeners = new HashSet<>();
    private CountDownLatch latch;

    protected void initialLatch(int initialValue) {
        latch = new CountDownLatch(initialValue);
    }

    protected void assertLatchCount(int shouldRemain) throws InterruptedException {
        latch.await(1, TimeUnit.SECONDS);
        Assertions.assertEquals(shouldRemain, latch.getCount());
    }

    protected void countDownLatch() {
        latch.countDown();
    }

    protected <T> void countDownOnNotification(Class<T> notificationType) {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnNotification(notificationType, event -> countDownLatch());
        NotificationCenter.registerListener(notificationListener);
    }

    protected <T extends Exception> void countDownOnException(Class<T> notificationType) {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnException(notificationType, event -> countDownLatch());
        NotificationCenter.registerListener(notificationListener);
    }

    protected Subscriber createMockSubscriber() {
        UUID subscriberId = UUID.randomUUID();
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        Mockito.when(subscriber.getUUID()).thenReturn(subscriberId);
        return subscriber;
    }

    protected <T> Listener createCountDownListenerFor(Class<T> type) {
        Listener listener = new Listener();
        listener.doOnEvent(type, event -> countDownLatch());
        return listener;
    }

    protected Publisher createMockPublisher() {
        UUID publisherId = UUID.randomUUID();
        Publisher publisher = Mockito.mock(Publisher.class);
        Mockito.when(publisher.getUUID()).thenReturn(publisherId);
        return publisher;
    }

    protected <I, O> DataSource<I, O> createMockDataSource() {
        DataSource<Object, Object> dataSource = Mockito.mock(DataSource.class);
        SourceAddress sourceAddress = Mockito.mock(SourceAddress.class);
        Mockito.when(dataSource.getAddress()).thenReturn(sourceAddress);
        ReadSource<Object> readSource = Mockito.mock(ReadSource.class);
        WriteSource<Object> writeSource = Mockito.mock(WriteSource.class);
        Mockito.doNothing().when(readSource).setHandler(Mockito.any());
        Mockito.when(dataSource.getReadSource()).thenReturn(readSource);
        Mockito.when(dataSource.getWriteSource()).thenReturn(writeSource);
        return (DataSource<I, O>) dataSource;
    }

    protected <I, O> DataSourceInfo<I, O> createTestDataSourceInfo() {
        String bridgeType = "SomeBridgeType";
        DataSourceInfo<I, O> dataSourceInfo = Mockito.mock(DataSourceInfo.class);
        Mockito.when(dataSourceInfo.getBridgeType()).thenReturn(bridgeType);
        Supplier<ServerFactory<?, ?>> serverFactorySupplier = () -> Mockito.mock(ServerFactory.class);
        ServerAbstractFactory.registerFactory(dataSourceInfo, serverFactorySupplier);
        return dataSourceInfo;
    }

    protected RemoteAddress createTestRemoteAddress() {
        return new RemoteAddress(port, host);
    }

    protected <T> T countDownWhen(T mockObject) {
        return Mockito.doAnswer(i -> {
            countDownLatch();
            return null;
        }).when(mockObject);
    }

    @AfterEach
    private void cleanUpNotificationListeners() {
        for (NotificationListener listener : notificationListeners)
            NotificationCenter.unregisterListener(listener);
        notificationListeners.clear();
    }
}
