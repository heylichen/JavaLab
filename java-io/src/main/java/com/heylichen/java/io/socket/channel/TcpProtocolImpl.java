package com.heylichen.java.io.socket.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TcpProtocolImpl implements TcpProtocol {

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, key.attachment());
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        int readBytes = socketChannel.read(buffer);
        if (readBytes == -1) {
            socketChannel.close();//if the client closed the channel, all bytes must be echoed.
        } else {
            socketChannel.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE, buffer);
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            socketChannel.register(key.selector(), SelectionKey.OP_READ, buffer);
        }
        buffer.compact();
    }
}
