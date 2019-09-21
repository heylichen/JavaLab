package com.heylichen.java.io.socket.channel;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface TcpProtocol {
    void handleAccept(SelectionKey key) throws IOException;

    void handleRead(SelectionKey key) throws IOException;

    void handleWrite(SelectionKey key) throws IOException;
}
