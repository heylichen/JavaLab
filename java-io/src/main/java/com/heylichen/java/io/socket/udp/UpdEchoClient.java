package com.heylichen.java.io.socket.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * code is simple, thoughts are valuable:
 * 1. DatagramPacket.send usually is not considered blocking, unless the underlying os buffer is full
 * 2. UPD not reliable, so we try multiple times
 * 3. UPD do not connect, so the received DatagramPacket's address may not be expected, so we check
 * 4. DatagramSocket.receive is blocking, so we set soTimeout and catch SocketTimeoutException in case of blocking forever
 */
@Slf4j
public class UpdEchoClient {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final int TIME_OUT = 2000;
    public static final String MSG = "Hello, world!";
    public static final int MAX_TRIES = 3;


    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), UpdEchoServer.PORT);
        try (DatagramSocket socket = new DatagramSocket()) {
            tryRequest(socket, socketAddress);
        }
    }

    private static void tryRequest(DatagramSocket socket, InetSocketAddress socketAddress) throws IOException {
        byte[] buffer = MSG.getBytes(UTF_8);
        int msgBytes = buffer.length;
        DatagramPacket sendPack = new DatagramPacket(buffer, msgBytes, socketAddress);
        DatagramPacket respPack = new DatagramPacket(new byte[msgBytes], msgBytes);

        socket.setSoTimeout(TIME_OUT);

        boolean receivedResponse = false;
        int tried = 0;
        while (!receivedResponse && tried < MAX_TRIES) {//udp may be lost, so we may try multiple times
            log.info("have tried {} times. try again", tried);
            socket.send(sendPack);
            try {
                socket.receive(respPack);
            } catch (SocketTimeoutException e) {//keep exception try block as small as possible
                tried++;
                log.error("request timeout for {} ms", TIME_OUT);
                continue;
            }
            //check
            if (!respPack.getAddress().equals(socketAddress.getAddress())) {
                throw new IOException("received resp from an unknown source server " + respPack.getAddress());
            }
            receivedResponse = true;
        }

        if (receivedResponse) {
            byte[] destBuf = Arrays.copyOfRange(sendPack.getData(), sendPack.getOffset(),
                    sendPack.getOffset() + sendPack.getLength());
            String receivedMsg = new String(destBuf, UTF_8);
            log.info("msg received:{}", receivedMsg);
        } else {
            log.warn("msg NOT received");
        }
    }
}
