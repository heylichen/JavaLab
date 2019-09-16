package com.heylichen.java.io.socket.encoding;

import java.util.ArrayList;
import java.util.List;

public class BruteForceCoding {

    public static final int BYTE_MASK = 0xFF;
    public static final int LOW4_MASK = 0x0F;
    public static final HexCharset HEX_CHARSET = new HexCharset();
    private List<String> byteStringList = null;
    private BytesView bytesView = new BytesView();

    private BruteForceCoding(List<String> byteStringList) {
        this.byteStringList = byteStringList;
    }

    public static final BruteForceCoding asDecimal(byte[] bytes) {
        List<String> result = new ArrayList<>();

        for (byte aByte : bytes) {
            result.add(String.valueOf(aByte & BYTE_MASK));

        }
        return new BruteForceCoding(result);
    }

    public static final BruteForceCoding asHex(byte[] bytes) {
        List<String> result = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.delete(0, sb.length());
            int high4Bits = ((int) aByte & BYTE_MASK) >>> 4;
            int low4Bites = (int) aByte & LOW4_MASK;
            sb.append(HEX_CHARSET.toChar(high4Bits)).append(HEX_CHARSET.toChar(low4Bites));
            result.add(sb.toString());
        }
        return new BruteForceCoding(result);
    }

    public static final int encodeIntBigEndian(byte[] dest, long value, int offset, int size) {
        for (int i = 0; i < size; i++) {
            dest[i] = (byte) (value >> ((size - 1) * Byte.SIZE));
        }
        return offset;
    }

    public static final long decodeIntBigEndian(byte[] dest, int offset, int size) {
        int end = offset + size;
        long value = 0l;
        for (int i = offset; i < end; i++) {
            value = value << Byte.SIZE | ((long) dest[i] & BYTE_MASK);
        }
        return value;
    }

    public String lines() {
        return bytesView.asLines(byteStringList);
    }

    public void setCharsPerLine(int charsPerLine) {
        bytesView.setCharsPerLine(charsPerLine);
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{0x0F, 127, -128, -127};
        System.out.println(BruteForceCoding.asDecimal(bytes).lines());
        System.out.println(BruteForceCoding.asHex(bytes).lines());

        long a = 65535;
        byte[] aBytes = new byte[2];
        encodeIntBigEndian(aBytes, a, 6, 2);
        long aResult = decodeIntBigEndian(aBytes, 0, 2);
        System.out.println(aResult);
    }
}
