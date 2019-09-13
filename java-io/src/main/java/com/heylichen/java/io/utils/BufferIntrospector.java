package com.heylichen.java.io.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.Buffer;
@Slf4j
public class BufferIntrospector {

    public static void viewBufferMeta(Buffer buffer, String msg) {
        log.info("{} bufferClass={} capacity= {} position={} limit={}", msg,
                buffer.getClass(), buffer.capacity(), buffer.position(), buffer.limit());
    }
}
