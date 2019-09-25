package com.heylichen.java.io.random;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

@Slf4j
public class AsyncFileChannelDemoTest {
    public static final String FILE_PATH = "C:\\Users\\lc\\Desktop\\临时\\hello.txt";

    @Test
    public void testFuture() throws Exception {
        String pathStr = FILE_PATH;
        Path path = Paths.get(pathStr);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(16);
        Future<Integer> readBytesFuture = afc.read(buffer, 0);
        while (!readBytesFuture.isDone()) {
            Thread.sleep(200);
            System.out.print(".");
        }
        int length = readBytesFuture.get();
        String str = new String(buffer.array(), 0, length, StandardCharsets.UTF_8);
        log.info("----->read string:{}", str);
    }

    @Test
    public void testHandler() throws Exception {
        String pathStr = FILE_PATH;
        Path path = Paths.get(pathStr);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(16);

        afc.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                String str = new String(buffer.array(), 0, result, StandardCharsets.UTF_8);
                log.info("----->read string:{}", str);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                log.error("failed to reaad");
            }
        });
        Thread.sleep(1000);
    }
}
