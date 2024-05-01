package com.alxan.noteefy.web.bridge.parse;

import com.alxan.noteefy.web.server.tcp.ByteBuff;
import com.alxan.noteefy.web.server.tcp.VertxBuff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BufferParserUnitTest {
    @Test
    public void shouldAppendBuffer() throws IOException {
        ByteBuff byteBuff = new VertxBuff(new byte[]{1, 2, 3, 4});
        BufferParser bufferParser = new BufferParser();
        bufferParser.appendBuffer(byteBuff);
        Assertions.assertArrayEquals(bufferParser.byteStream.getBytes(bufferParser.byteStream.length()), byteBuff.getBytes());
    }

    @Test
    public void shouldCheckIfItemReceived() throws IOException {
        BufferParser bufferParser = new BufferParser();
        Assertions.assertFalse(bufferParser.isSizeReceived());

        ByteBuff sizeBuffer = new VertxBuff();
        sizeBuffer.appendInt(4);
        Assertions.assertFalse(bufferParser.isSizeReceived());
        bufferParser.appendBuffer(sizeBuffer);
        int contentSize = bufferParser.readContentSize();
        Assertions.assertFalse(bufferParser.isItemReceived(contentSize));

        ByteBuff itemBuffer = new VertxBuff(new byte[]{1, 2, 3, 4});
        bufferParser.appendBuffer(itemBuffer);
        Assertions.assertTrue(bufferParser.isItemReceived(4));
    }

    @Test
    public void shouldReadContentSize() throws IOException {
        BufferParser bufferParser = new BufferParser();
        int emptySize = bufferParser.readContentSize();
        Assertions.assertEquals(-1, emptySize);

        int size = 4;
        ByteBuff sizeBuffer = new VertxBuff();
        sizeBuffer.appendInt(size);
        bufferParser.appendBuffer(sizeBuffer);

        int fetchedSize = bufferParser.readContentSize();
        Assertions.assertEquals(size, fetchedSize);
    }

    @Test
    public void shouldReadContent() throws IOException {
        byte[] content = new byte[]{1, 2, 3, 4};
        BufferParser bufferParser = new BufferParser();
        ByteBuff contentBuffer = new VertxBuff();
        contentBuffer.appendInt(4);
        contentBuffer.appendBytes(content);
        bufferParser.appendBuffer(contentBuffer);

        int contentSize = bufferParser.readContentSize();
        byte[] fetchedContent = bufferParser.readContent(contentSize);
        Assertions.assertArrayEquals(content, fetchedContent);
    }
}
