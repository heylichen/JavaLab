package com.heylichen.java.io.socket.channel.asyc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

    @Override
    public void completed(Integer readBytes, Attachment attachment) {
        AsynchronousSocketChannel clientChannel = attachment.getClientChannel();
        if (attachment.isRead()) {
            readComplete(readBytes, attachment, clientChannel);
        } else {
            writeComplete(readBytes, attachment, clientChannel);
        }
    }

    private void writeComplete(Integer writtenBytes, Attachment attachment, AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = attachment.getBuffer();
        buffer.compact();
        attachment.setRead(true);
        clientChannel.read(buffer, attachment, this);
    }

    private void readComplete(Integer readBytes, Attachment attachment, AsynchronousSocketChannel clientChannel) {
        if (readBytes != null && readBytes.intValue() == -1) {
            try {
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteBuffer buffer = attachment.getBuffer();
        buffer.flip();
        byte[] readByteArr = buffer.array();
        String msg = new String(readByteArr, buffer.position(), buffer.limit(), StandardCharsets.UTF_8);
        log.info("read from client {} msg:{}", attachment.getClientAddress(), msg);
        buffer.rewind();

        attachment.setRead(false);
        clientChannel.write(buffer, attachment, this);
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        log.error("error during {}", attachment.isRead(), exc);
    }
}
