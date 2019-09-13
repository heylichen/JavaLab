package com.heylichen.java.io.channel;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

@Slf4j
public class FileLockMultipleThreads {
    public static final String MSG = "hello";
    public static final int MSG_BYTES = MSG.length() * 2;

    public static void main(String[] args) throws Exception {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try (RandomAccessFile fin = new RandomAccessFile("tmp.txt", "rw");
                     FileChannel fc = fin.getChannel();
                ) {
                    FileLock lock1 = fc.lock(0, MSG_BYTES, false);
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(MSG_BYTES);
                        CharBuffer charBuffer = byteBuffer.asCharBuffer();
                        charBuffer.put(MSG);
                        charBuffer.flip();
                        fc.write(byteBuffer);
                        Thread.sleep(1000);
                    } finally {
                        lock1.release();
                    }
                } catch (IOException | InterruptedException e) {
                    log.error("error", e);
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try (RandomAccessFile fin = new RandomAccessFile("tmp.txt", "rw");
                     FileChannel fc = fin.getChannel();
                ) {
                    //OverlappingFileLockException here
                    FileLock lock1 = fc.lock(0, MSG_BYTES, false);
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(MSG_BYTES);
                        CharBuffer charBuffer = byteBuffer.asCharBuffer();
                        charBuffer.put(MSG);
                        charBuffer.flip();
                        fc.write(byteBuffer);
                    } finally {
                        lock1.release();
                    }
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        };

        t1.start();
        Thread.sleep(500);
        t2.start();
        Thread.sleep(1000);

    }

    public static void sharedChannel() throws Exception {
        try (RandomAccessFile fin = new RandomAccessFile("tmp.txt", "rw");
             FileChannel fc = fin.getChannel();
        ) {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    try {
                        FileLock lock1 = fc.lock(0, MSG_BYTES, false);
                        try {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(MSG_BYTES);
                            CharBuffer charBuffer = byteBuffer.asCharBuffer();
                            charBuffer.put(MSG);
                            charBuffer.flip();
                            fc.write(byteBuffer);
                            Thread.sleep(1000);
                        } finally {
                            lock1.release();
                        }
                    } catch (IOException | InterruptedException e) {
                        log.error("error", e);
                    }
                }
            };
            Thread t2 = new Thread() {
                @Override
                public void run() {
                    try {
                        //OverlappingFileLockException here
                        FileLock lock1 = fc.lock(0, MSG_BYTES, false);
                        try {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(MSG_BYTES);
                            CharBuffer charBuffer = byteBuffer.asCharBuffer();
                            charBuffer.put(MSG);
                            charBuffer.flip();
                            fc.write(byteBuffer);
                        } finally {
                            lock1.release();
                        }
                    } catch (IOException e) {
                        log.error("error", e);
                    }
                }
            };

            t1.start();
            Thread.sleep(500);
            t2.start();
            Thread.sleep(1000);
        }
    }

}
