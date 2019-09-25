package com.heylichen.java.io.socket.channel.asyc;

import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

@Getter
@Setter
public class Attachment {
    private AsynchronousServerSocketChannel serverChannel;
    private AsynchronousSocketChannel clientChannel;
    private boolean read;
    private ByteBuffer buffer;
    private SocketAddress clientAddress;
}
