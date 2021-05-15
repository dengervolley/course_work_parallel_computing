package com.coursework.suppliers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StubTextSupplier implements ITextSupplier {

    private ConcurrentHashMap<String, String> fileContents;

    public StubTextSupplier() {
        this.fileContents = new ConcurrentHashMap<>();
    }

    public void addFileContent(String fileName, String text) {
        if (fileName != null && fileName.length() != 0
                && text != null && text.length() != 0) {
            fileContents.put(fileName, text);
        }
    }

    public void clearFiles(){
        this.fileContents = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> getAllLines(String fileName) {
        return Arrays.stream(this.fileContents.get(fileName).split("\n")).toList();
    }
}
