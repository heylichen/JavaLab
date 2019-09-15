package com.heylichen.java.io.socket.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TcpEchoClient {

    public static final Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        String helloMsg = "Hello,World";
        if (args.length >= 1) {
            helloMsg = args[0];
        }

        Socket socket = new Socket(InetAddress.getLoopbackAddress(), TcpEchoServer.PORT);

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()
        ) {
            byte[] msgBytes = helloMsg.getBytes(charset);
            out.write(msgBytes);
            log.info("sending {}", helloMsg);

            int totalReadBytes = 0;
            int currentRead = 0;

            while (totalReadBytes < msgBytes.length) {
                currentRead = in.read(msgBytes, totalReadBytes, msgBytes.length - totalReadBytes);
                if (currentRead == -1) {
                    throw new IllegalArgumentException("server socket closed prematurely");
                }
                totalReadBytes += currentRead;
            }
            log.info("received : {}", new String(msgBytes, charset));
        }

    }
}
