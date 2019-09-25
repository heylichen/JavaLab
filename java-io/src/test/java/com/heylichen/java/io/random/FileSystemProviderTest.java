package com.heylichen.java.io.random;

import org.junit.Test;

import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class FileSystemProviderTest {
    @Test
    public void fileSystemProviderTest() {
        List<FileSystemProvider> fsProviders = FileSystemProvider.installedProviders();
        for (FileSystemProvider fsProvider : fsProviders) {
            System.out.println(fsProvider);
        }
    }
}
