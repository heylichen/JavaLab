package com.heylichen.java.io.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class CopyByChannel {
    public static final int BUFFER_SIZE = 2048;

    public static void main(String[] args) throws IOException {

        try (ReadableByteChannel rc = Channels.newChannel(System.in);
             WritableByteChannel wc = Channels.newChannel(System.out)) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

            while (rc.read(buffer) != -1) {
                buffer.flip();
                wc.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                wc.write(buffer);
            }
            buffer.clear();
        }
    }
}
