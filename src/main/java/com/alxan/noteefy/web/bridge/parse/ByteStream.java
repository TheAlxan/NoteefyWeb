package com.alxan.noteefy.web.bridge.parse;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.web.common.StreamIO;
import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteStream extends BaseIdentifiable {
    private static final int BUFFER_SIZE = 4096; // TODO Read from config file
    private ByteArrayInputStream inputStream;
    private ByteBuff buffer;

    public ByteStream() {
        buffer = new VertxBuff();
        inputStream = new ByteArrayInputStream(buffer.getBytes());
    }

    public void buffer(ByteBuff aBuffer) {
        buffer.appendBuffer(aBuffer);
    }

    public int readInt() throws IOException {
        assureSize(4);
        return StreamIO.byteArrayToInt(inputStream.readNBytes(4));
    }

    public byte[] getBytes(int size) throws IOException {
        assureSize(size);
        return inputStream.readNBytes(size);
    }

    public int length() {
        return inputStream.available() + buffer.length();
    }

    private void assureSize(int size) {
        if (size > inputStream.available())
            renewStream();
    }

    private synchronized void renewStream() {
        byte[] remaining = inputStream.readAllBytes();
        ByteBuff newBuffer = new VertxBuff(remaining);
        int maxSize = Math.min(BUFFER_SIZE - remaining.length, buffer.length());
        newBuffer.appendBytes(buffer.getBytes(maxSize));
        inputStream = new ByteArrayInputStream(newBuffer.getBytes());
        buffer = buffer.trim(maxSize);
    }
}
