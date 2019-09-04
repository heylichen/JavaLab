package com.heylichen.java.io.random;

import lombok.Getter;
import lombok.Setter;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * can jump pointer, but without buffer
 */
public class PartDB implements Closeable {

    private RandomAccessFile raf;
    private static final String MODE = "rw";
    public static final int PART_NUM_CHARS = 20;
    public static final int DESC_CHARS = 30;
    public static final int INT_BYTES = 4;
    public static final int RECORD_BYTES = (PART_NUM_CHARS + DESC_CHARS) * 2 + INT_BYTES + INT_BYTES;

    public PartDB(String name) throws FileNotFoundException {
        raf = new RandomAccessFile(name, MODE);
    }

    public void append(Part part) throws IOException {
        raf.seek(raf.length());
        write(part);
    }

    public void update(int recordNo, Part part) throws IOException {
        if (recordNo < 0 || recordNo > size()) {
            throw new IllegalArgumentException("index out of bound");
        }
        raf.seek(recordNo * RECORD_BYTES);
        write(part);
    }

    public int size() throws IOException {
        return (int) raf.length() / RECORD_BYTES;
    }

    public Part select(int recordNo) throws IOException {
        if (recordNo < 0 || recordNo > size()) {
            throw new IllegalArgumentException("index out of bound");
        }
        raf.seek(recordNo * RECORD_BYTES);
        return read();
    }

    public void close() throws IOException {
        raf.close();
    }

    private Part read() throws IOException {
        String num = readChars(PART_NUM_CHARS);
        String desc = readChars(DESC_CHARS);
        int quantity = raf.readInt();
        int unitCost = raf.readInt();
        return new Part(num, desc, quantity, unitCost);
    }

    private String readChars(int len) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (; len > 0; len--) {
            sb.append(raf.readChar());
        }
        return sb.toString();
    }

    private void write(Part part) throws IOException {
        raf.writeChars(toLength(part.partNum, PART_NUM_CHARS));
        raf.writeChars(toLength(part.desc, DESC_CHARS));
        raf.writeInt(part.quantity);
        raf.writeInt(part.unitCost);
    }

    private String toLength(String str, int len) {
        if (str == null) {
            str = "";
        }
        if (str.length() == len) {
            return str;
        }
        if (str.length() > len) {
            return str.substring(0, len);
        }
        StringBuilder sb = new StringBuilder(len);
        sb.append(str);
        int left = len - str.length();
        while (left > 0) {
            sb.append(" ");
            left--;
        }
        return sb.toString();
    }

    @Getter
    @Setter
    public static class Part {
        private String partNum;
        private String desc;
        private int quantity;
        private int unitCost;

        public Part(String partNum, String desc, int quantity, int unitCost) {
            this.partNum = partNum;
            this.desc = desc;
            this.quantity = quantity;
            this.unitCost = unitCost;
        }

        @Override
        public String toString() {
            return partNum + "\t" + desc + "\t" + quantity + "\t" + unitCost;
        }
    }
}
