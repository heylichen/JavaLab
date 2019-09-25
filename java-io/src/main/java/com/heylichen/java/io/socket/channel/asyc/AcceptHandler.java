package com.heylichen.java.io.socket.channel.asyc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

@Slf4j
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {
    public static final int BUFFER_SIZE = 512;
    @Override
    public void completed(AsynchronousSocketChannel result, Attachment attachment) {
        try {
            SocketAddress clientAddress = result.getRemoteAddress();
            log.info("received client: {}", clientAddress);

            attachment.getServerChannel().accept(attachment, this);

            Attachment clientAttach = new Attachment();
            clientAttach.setRead(true);
            clientAttach.setServerChannel(attachment.getServerChannel());
            clientAttach.setClientAddress(clientAddress);
            clientAttach.setClientChannel(result);
            clientAttach.setBuffer(ByteBuffer.allocate(BUFFER_SIZE));

            result.read(clientAttach.getBuffer(), clientAttach, new ReadWriteHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        log.error("failed to accept", exc);
    }
}
