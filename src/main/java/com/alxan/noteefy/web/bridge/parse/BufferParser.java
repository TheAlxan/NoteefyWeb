package com.alxan.noteefy.web.bridge.parse;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.web.server.tcp.ByteBuff;

import java.io.IOException;

public class BufferParser extends BaseIdentifiable {
    private static final int SIZE_LENGTH = 4;
    private static final int INCOMPLETE_INDICATOR = -1;
    protected ByteStream byteStream = new ByteStream();

    protected void appendBuffer(ByteBuff buffer) {
        byteStream.buffer(buffer);
    }

    protected boolean isItemReceived(int size) {
        return size != INCOMPLETE_INDICATOR && byteStream.length() >= size;
    }

    protected boolean isSizeReceived() {
        return byteStream.length() >= SIZE_LENGTH;
    }

    protected int readContentSize() throws IOException {
        if (byteStream.length() < SIZE_LENGTH) {
            return INCOMPLETE_INDICATOR;
        }
        return byteStream.readInt();
    }

    protected byte[] readContent(int size) throws IOException {
        return byteStream.getBytes(size);
    }
}
