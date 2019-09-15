package com.heylichen.java.io.socket.encoding;

public interface NumberCharset {
    int radix();

    char toChar(int index);

    int toIndex(char ch);
}
