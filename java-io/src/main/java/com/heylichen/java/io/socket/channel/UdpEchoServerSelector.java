package com.heylichen.java.io.socket.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

@Slf4j
public class UdpEchoServerSelector {
    public static final int PORT = 8081;
    public static final int BUFFER_SIZE = 65507;
    public static final long TIME_OUT = 2000;

    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(Inet4Address.getLoopbackAddress(), PORT));
        datagramChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Selector selector = Selector.open();

        datagramChannel.register(selector, SelectionKey.OP_READ, new ClientRecord(buffer, null));

        while (true) {
            if (selector.select(TIME_OUT) <= 0) {
                log.info("waiting .");
                continue;
            }
            Iterator<SelectionKey> selectedKeyIterator = selector.selectedKeys().iterator();
            while (selectedKeyIterator.hasNext()) {
                SelectionKey selectedKey = selectedKeyIterator.next();
                if (selectedKey.isValid() && selectedKey.isReadable()) {
                    log.info("read ready");
                    handleRead(selectedKey);
                }

                if (selectedKey.isValid() && selectedKey.isWritable()) {
                    log.info("write ready");
                    handleWrite(selectedKey);
                }
                selectedKeyIterator.remove();
            }
        }
    }

    private static void handleRead(SelectionKey selectedKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectedKey.channel();
        ClientRecord clientRecord = (ClientRecord) selectedKey.attachment();
        ByteBuffer buffer = clientRecord.getBuffer();

        buffer.clear();//discard any unsent buffered data.
        SocketAddress clientAddress = datagramChannel.receive(buffer);
        if (clientAddress != null) {//if received
            clientRecord.setClientAddress(clientAddress);
            datagramChannel.register(selectedKey.selector(), SelectionKey.OP_WRITE, clientRecord);
        }
    }

    private static void handleWrite(SelectionKey selectedKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectedKey.channel();
        ClientRecord clientRecord = (ClientRecord) selectedKey.attachment();
        ByteBuffer buffer = clientRecord.getBuffer();

        buffer.flip();
        int sendBytes = datagramChannel.send(buffer, clientRecord.getClientAddress());//all sent or none sent
        log.info("write bytes {}", sendBytes);
        if (sendBytes > 0) {//all sent
            datagramChannel.register(selectedKey.selector(), SelectionKey.OP_READ, clientRecord);
        }//else
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class ClientRecord {
        private ByteBuffer buffer;
        private SocketAddress clientAddress;

        public ClientRecord(ByteBuffer buffer, SocketAddress clientSocket) {
            this.buffer = buffer;
            this.clientAddress = clientSocket;
        }
    }
}
