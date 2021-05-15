package com.coursework.suppliers;

import com.coursework.loggers.ILogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileTextSupplier implements ITextSupplier {

    private final ILogger logger;

    public FileTextSupplier( ILogger logger) {
        this.logger = logger;
    }

    @Override
    public List<String> getAllLines(String fileName) {
        List<String> textLines = null;
        try {
            textLines = Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            logger.logError(e);
        }
        return textLines;
    }
}
