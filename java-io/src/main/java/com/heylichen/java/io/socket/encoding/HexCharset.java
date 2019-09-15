package com.heylichen.java.io.socket.encoding;

import static com.heylichen.java.io.socket.encoding.NumberChars.*;

public class HexCharset implements NumberCharset {

    public static final char[] CHARS = new char[]{CH_0, CH_1, CH_2, CH_3, CH_4, CH_5, CH_6, CH_7, CH_8, CH_9, CH_10,
            CH_11, CH_12, CH_13, CH_14, CH_15};

    @Override
    public int radix() {
        return CHARS.length;
    }

    @Override
    public char toChar(int index) {
        return CHARS[index];
    }

    @Override
    public int toIndex(char ch) {
        if (ch >= CH_0 && ch <= CH_9) {
            return ch - CH_0;
        }
        switch (ch) {
            case CH_10:
                return 10;
            case CH_11:
                return 11;
            case CH_12:
                return 12;
            case CH_13:
                return 13;
            case CH_14:
                return 14;
            case CH_15:
                return 15;
        }
        return 0;
    }
}
