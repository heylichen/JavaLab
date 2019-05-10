package com.heylichen.java.serialization;

import java.io.*;

public class SerializationUtil {

    public static final void serialize(Object object, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(object);
            //will flush when close
        }
    }

    public Object deserialize(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            return ois.readObject();
            //will flush when close
        }
    }
}
