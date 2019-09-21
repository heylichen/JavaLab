package com.heylichen.java.io.socket.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UpdEchoClientNonBlocking {
    public static final String MSG = "Hello, World!";
    public static final int TIME_OUT = 500;
    public static final int MAX_TRIES = 3;

    public static void main(String[] args) throws IOException, InterruptedException {
        try (DatagramChannel datagramChannel = DatagramChannel.open();) {
            datagramChannel.configureBlocking(false);
            datagramChannel.connect(new InetSocketAddress(Inet4Address.getLoopbackAddress(), UdpEchoServerSelector.PORT));//pure local action

            int tried = 0;
            boolean success = false;
            while (tried < MAX_TRIES && !success) {
                byte[] msgBytes = MSG.getBytes(StandardCharsets.UTF_8);
                ByteBuffer buffer = ByteBuffer.wrap(msgBytes);
                datagramChannel.write(buffer);

                ByteBuffer readBuffer = ByteBuffer.allocate(msgBytes.length);
                int read = datagramChannel.read(readBuffer);
                tried++;
                if (read > 0) {
                    success = true;
                    readBuffer.flip();
                    String received = StandardCharsets.UTF_8.decode(readBuffer).toString();
                    log.info("tried {} times. msg received {}", tried, received);
                } else {
                    Thread.sleep(TIME_OUT);
                }
            }
        }
    }
}
