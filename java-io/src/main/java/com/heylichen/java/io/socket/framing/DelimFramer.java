package com.heylichen.java.io.socket.framing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DelimFramer implements Framer {
    private InputStream in;
    public static final byte DELIM = '\n';

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        for (byte b : message) {
            if (b == DELIM) {
                throw new IllegalArgumentException("invalid msg, containing newline");
            }
        }
        out.write(message);
        out.write(DELIM);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int readBytes = 0;
        int read;
        while ((read = in.read()) != (int) DELIM) {
            if (read == -1) {
                if (readBytes == 0) {
                    return null;
                } else{
                    throw new IllegalArgumentException("invalid msg");
                }
            }
            bout.write(read);
        }
        return bout.toByteArray();
    }
}
