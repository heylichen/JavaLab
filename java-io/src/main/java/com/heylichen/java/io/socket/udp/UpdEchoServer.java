package com.heylichen.java.io.socket.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
public class UpdEchoServer {
    public static final int PORT = 8081;
    public static final int BUFFER_SIZE = 65507;

    public static void main(String[] args) throws IOException {
        int delayMs = args.length >= 1 ? Integer.parseInt(args[0]) : 0;
        try (DatagramSocket socket = new DatagramSocket(PORT);) {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket dp = new DatagramPacket(buffer, BUFFER_SIZE);

            while (true) {
                socket.receive(dp);
                log.info("received request from {}:{}", dp.getAddress().toString(), dp.getPort());
                delay(delayMs);
                socket.send(dp);
                dp.setLength(BUFFER_SIZE); //clear space
            }
        }
    }

    private static void delay(int delayMs) {
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            e.printStackTrace();//ignore
        }
    }
}
