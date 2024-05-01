package com.alxan.noteefy.web.bridge;

import com.alxan.noteefy.web.event.WebEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebMessageTypeRegistryUnitTest {
    @Test
    public void shouldReturnDefaultRegistry() {
        WebMessageTypeRegistry typeRegistry = WebMessageTypeRegistry.getDefaultRegistry();
        Assertions.assertNotNull(typeRegistry);
    }

    @Test
    public void shouldRegisterTypeMeta() {
        WebMessageTypeRegistry typeRegistry = new WebMessageTypeRegistry();
        String classType = WebEvent.class.getCanonicalName();
        WebMessageTypeMeta typeMeta = new WebMessageTypeMeta(classType);

        typeRegistry.register(typeMeta);
        WebMessageTypeMeta fetchedMeta = typeRegistry.getTypeMeta(0);
        Assertions.assertNotNull(fetchedMeta);
    }

    @Test
    public void shouldRegisterClassTypes() {
        WebMessageTypeRegistry typeRegistry = new WebMessageTypeRegistry();
        typeRegistry.register(WebEvent.class);

        WebMessageTypeMeta typeMeta = typeRegistry.getTypeMeta(0);
        Assertions.assertNotNull(typeMeta);
    }

    @Test
    public void shouldReturnTypeId() {
        WebMessageTypeRegistry typeRegistry = new WebMessageTypeRegistry();
        typeRegistry.register(WebEvent.class);

        int typeId = typeRegistry.getTypeId(WebEvent.class);
        Assertions.assertNotNull(typeId);
    }
}
