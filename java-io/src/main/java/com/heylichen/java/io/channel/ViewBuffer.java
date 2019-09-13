package com.heylichen.java.io.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class ViewBuffer {
    public static final String MSG = "hello";
    public static final int MSG_BYTES = MSG.length() * 2;

    public static void main(String[] args) throws Exception {
        try (RandomAccessFile fin = new RandomAccessFile("tmp.txt", "rw");
             FileChannel fc = fin.getChannel();
        ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(MSG_BYTES);
            CharBuffer charBuffer = byteBuffer.asCharBuffer();
            charBuffer.put(MSG);
            charBuffer.flip();
            fc.write(byteBuffer);
            //if you open the tmp file, will see  h e l l o ,but it doesn't matter,
            //because you can read it back by charBuffer
            fc.position(0);
            byteBuffer.clear();
            charBuffer.clear();
            fc.read(byteBuffer);
            while (charBuffer.hasRemaining()) {
                System.out.print(charBuffer.get());
            }
        }
    }
}
