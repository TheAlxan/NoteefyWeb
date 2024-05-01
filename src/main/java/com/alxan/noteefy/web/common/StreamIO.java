package com.alxan.noteefy.web.common;

public class StreamIO {
    public static byte[] intToByteArray(int n) {
        return new byte[]{(byte) (n >> 24), (byte) (n >> 16), (byte) (n >> 8), (byte) n};
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | ((bytes[3] & 0xFF));
    }
}
