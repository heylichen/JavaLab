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
    }
}
