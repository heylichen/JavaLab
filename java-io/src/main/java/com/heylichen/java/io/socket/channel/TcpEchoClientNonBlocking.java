package com.heylichen.java.io.socket.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TcpEchoClientNonBlocking {
    public static final int SERVER_PORT = 8081;
    public static final String MSG = "Hello, world!";
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException, InterruptedException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            SocketAddress serverSocket = new InetSocketAddress(Inet4Address.getLocalHost(), SERVER_PORT);
            if (!socketChannel.connect(serverSocket)) {
                while (!socketChannel.finishConnect()) {
                    System.out.print(".");
                    Thread.sleep(200);
                }
            }

            ByteBuffer writeBuffer = ByteBuffer.wrap(MSG.getBytes(CHARSET));
            int msgBytes = writeBuffer.limit();
            ByteBuffer readBuffer = ByteBuffer.allocate(msgBytes);

            int receivedBytes = 0;
            int currentRead = 0;
            while (receivedBytes < msgBytes) {
                if (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }

                currentRead = socketChannel.read(readBuffer);
                if (currentRead == -1) {
                    throw new IllegalStateException("channel closed prematurely");
                } else {
                    receivedBytes += currentRead;
                }
            }
            readBuffer.flip();
            String readMsg = new String(readBuffer.array(), readBuffer.position(), readBuffer.limit(), CHARSET);
            log.info("msg send:{} received:{}", MSG, readMsg);
        }
    }
}
