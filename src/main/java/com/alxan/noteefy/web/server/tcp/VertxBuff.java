package com.alxan.noteefy.web.server.tcp;

import io.vertx.core.buffer.Buffer;

public class VertxBuff implements ByteBuff {
    private final Buffer buffer;

    public VertxBuff() {
        buffer = Buffer.buffer();
    }

    public VertxBuff(Buffer aBuffer) {
        buffer = aBuffer;
    }

    public VertxBuff(byte[] bytes) {
        buffer = Buffer.buffer(bytes);
    }

    @Override
    public int readInt() {
        return buffer.getInt(0);
    }

    @Override
    public void appendInt(int n) {
        buffer.appendInt(n);
    }

    @Override
    public void appendBytes(byte[] bytes) {
        buffer.appendBytes(bytes);
    }

    @Override
    public void appendBuffer(ByteBuff aBuffer) {
        buffer.appendBuffer(Buffer.buffer(aBuffer.getBytes()));
    }

    @Override
    public int length() {
        return buffer.length();
    }

    @Override
    public byte[] getBytes() {
        return buffer.getBytes();
    }

    @Override
    public byte[] getBytes(int size) {
        return buffer.getBytes(0, size);
    }

    @Override
    public ByteBuff trim(int size) {
        return new VertxBuff(buffer.getBuffer(size, buffer.length()));
    }
}
