package com.heylichen.java.io.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BufferIntrospector {

    public static void viewBufferMeta(Buffer buffer, String msg) {
        log.info("{} bufferClass={} capacity= {} position={} limit={}", msg,
                buffer.getClass(), buffer.capacity(), buffer.position(), buffer.limit());
    }

    public static String toString(ByteBuffer byteBuffer) {
        return new String(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit(), StandardCharsets.UTF_8);
    }
}
