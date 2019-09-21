package com.heylichen.java.io.socket.channel;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TcpEchoServerSelector {
    private static TcpProtocol tcpProtocol = new TcpProtocolImpl();
    public static final int SERVER_PORT = 8081;
    public static final int BUFFER_SIZE = 1024;
    public static final long TIMEOUT = 1000;

    public static void main(String[] args) throws IOException {
        SocketAddress serverSocket = new InetSocketAddress(Inet4Address.getLocalHost(), SERVER_PORT);

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false);
            serverChannel.bind(serverSocket);

            Selector selector = Selector.open();
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT, byteBuffer);

            while (true) {
                if (selector.select(TIMEOUT) <= 0) {
                    continue;
                }
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey selectedKey = keysIterator.next();
                    if (selectedKey.isValid() && selectedKey.isAcceptable()) {
                        tcpProtocol.handleAccept(selectedKey);
                    }
                    if (selectedKey.isValid() && selectedKey.isReadable()) {
                        tcpProtocol.handleRead(selectedKey);
                    }
                    if (selectedKey.isValid() && selectedKey.isWritable()) {//check isValid in case of CancelledKeyException
                        tcpProtocol.handleWrite(selectedKey);
                    }
                }
                keysIterator.remove();
            }
        }
    }
}
