package com.heylichen.java.io.socket.encoding;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Setter;

import java.util.List;

@Setter
public class BytesView {
    private int charsPerLine = 8;
    private Joiner joiner = Joiner.on(" ");

    public String asLines(List<String> byteStringList) {
        if (byteStringList == null || byteStringList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<List<String>> lines = Lists.partition(byteStringList, charsPerLine);
        int last = lines.size() - 1;
        for (int i = 0; i <= last; i++) {
            List<String> lineOfChars = lines.get(i);
            sb.append(joiner.join(lineOfChars));
            if (i != last) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }


}
