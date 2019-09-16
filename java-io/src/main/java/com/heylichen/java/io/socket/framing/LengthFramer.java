package com.heylichen.java.io.socket.framing;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LengthFramer implements Framer {
    private DataInputStream in;
    public static final int MAX_BYTES = 65535;
    public static final int BYTE_MASK = 0xFF;

    public LengthFramer(InputStream in) {
        this.in = new DataInputStream(in);
    }

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        int len = message.length;
        if (len > MAX_BYTES) {
            throw new IllegalArgumentException("message too large. must be <= " + MAX_BYTES + " bytes");
        }
        out.write((len >> Byte.SIZE) & BYTE_MASK);
        out.write(len & BYTE_MASK);
        out.write(message);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        int len = 0;
        try {
            len = in.readUnsignedShort();
        } catch (IOException e) {

        }

        if (len > MAX_BYTES) {
            throw new IllegalArgumentException("message too large. must be <= " + MAX_BYTES + " bytes");
        }
        byte[] result = new byte[len];
        in.readFully(result);
        return result;
    }
}
