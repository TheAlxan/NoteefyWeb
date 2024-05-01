package com.alxan.noteefy.web.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StreamIOUnitTest {
    @Test
    public void shouldConvertIntToByteArray() {
        int number = 256;
        byte[] expectedBytes = new byte[]{0, 0, 1, 0};
        byte[] convertedBytes = StreamIO.intToByteArray(number);
        Assertions.assertArrayEquals(expectedBytes, convertedBytes);
    }

    @Test
    public void shouldConvertByteArrayToInt() {
        byte[] bytes = new byte[]{0, 0, 1, 0};
        int expectedInt = 256;
        int convertedInt = StreamIO.byteArrayToInt(bytes);
        Assertions.assertEquals(expectedInt, convertedInt);
    }
}
