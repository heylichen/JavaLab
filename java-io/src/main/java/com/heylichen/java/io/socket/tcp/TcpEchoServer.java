package com.heylichen.java.io.socket.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class TcpEchoServer {
    public static final int PORT = 8081;
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        byte[] bytesBuffer = new byte[BUFFER_SIZE];

        while (true) {
            log.info("");
            log.info("block in accepting ... ");
            try (Socket socket = serverSocket.accept();
                 InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {

                log.info("accepted {}", socket.getRemoteSocketAddress());

                int readTimes = 0;
                for (int read = in.read(bytesBuffer); read != -1; read = in.read(bytesBuffer)) {
                    readTimes++;
                    out.write(bytesBuffer, 0, read);
                }

                log.info("readTimes:{} closing {}", readTimes, socket.getRemoteSocketAddress());
            }
        }
    }
}
