package com.heylichen.java.io.random;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PartDBTest {

    @Test
    public void partDbTest() throws IOException {
        File tmpFile = File.createTempFile("PartDBTest", "tmp");
        tmpFile.deleteOnExit();

        try (PartDB partDB = new PartDB(tmpFile.getCanonicalPath())) {
            partDB.append(new PartDB.Part("part1", "this is part 1", 123, 2));
            PartDB.Part part1 = new PartDB.Part("part2", "this is part 2", 122, 33);
            partDB.append(part1);

            System.out.println(partDB.select(1));
            System.out.println(partDB.select(0));
            Assert.assertEquals("size must be 2",2,partDB.size());

            part1.setDesc("modified desc");
            partDB.update(1,part1);
            System.out.println(partDB.select(1));
        }
    }
}