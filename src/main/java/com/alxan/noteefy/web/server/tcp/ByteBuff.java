package com.alxan.noteefy.web.server.tcp;


public interface ByteBuff {
    public int readInt();

    public void appendInt(int n);

    public void appendBytes(byte[] bytes);

    public void appendBuffer(ByteBuff buffer);

    public int length();

    public byte[] getBytes();

    public byte[] getBytes(int size);

    public ByteBuff trim(int size);
}
