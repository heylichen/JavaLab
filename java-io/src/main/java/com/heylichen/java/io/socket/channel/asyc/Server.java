package com.heylichen.java.io.socket.channel.asyc;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

@Slf4j
public class Server {
    public static final int PORT = 8081;

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel asyncServerChannel = AsynchronousServerSocketChannel.open();
        SocketAddress serverAddress = new InetSocketAddress(Inet4Address.getLoopbackAddress(), PORT);
        log.info("server start listening at {}", serverAddress);
        asyncServerChannel.bind(serverAddress);

        Attachment attachment = new Attachment();
        attachment.setServerChannel(asyncServerChannel);
        asyncServerChannel.accept(attachment, new AcceptHandler());

        Thread.sleep(20000);
    }
}
