package com.heylichen.java.io.socket.channel.asyc;

import com.heylichen.java.io.utils.BufferIntrospector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Client {
    public static final String MSG = "Hello";

    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        try {
            clientChannel.connect(new InetSocketAddress(Inet4Address.getLoopbackAddress(), Server.PORT)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        ByteBuffer buffer = ByteBuffer.wrap(MSG.getBytes(StandardCharsets.UTF_8));
        Attachment attachment = new Attachment();
        attachment.setBuffer(buffer);
        attachment.setRead(false);
        attachment.setClientChannel(clientChannel);

        clientChannel.write(buffer, attachment, new ReadWriteHandler());
        Thread.sleep(20000);
    }

    private static class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attachment) {
            ByteBuffer buffer = attachment.buffer;
            AsynchronousSocketChannel clientChannel = attachment.clientChannel;
            if (attachment.isRead()) {
                buffer.flip();
                String readMsg = BufferIntrospector.toString(buffer);
                log.info("client read msg:{}",readMsg);

            } else {
                buffer.clear();//TODO case when buffer not all written
                attachment.setRead(true);
                clientChannel.read(buffer, attachment, this);
                log.info("client msg write");
            }
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            log.error("failed ", exc);
        }
    }


    @Getter
    @Setter
    private static class Attachment {
        private boolean read;
        private AsynchronousSocketChannel clientChannel;
        private ByteBuffer buffer;
    }
}
