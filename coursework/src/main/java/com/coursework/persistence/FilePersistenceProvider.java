package com.coursework.persistence;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ILogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FilePersistenceProvider implements IPersistenceProvider {

    private String filePath;
    private ILogger logger;

    public FilePersistenceProvider(ILogger logger){
        this.logger = logger;
    }

    public void setFilePath(String file){
        this.filePath = file;
    }

    @Override
    public void persistIndex(InverseIndex index) {
        var json = index.toString();
        try {
            var writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
        } catch (IOException e) {
            logger.logError(e);
        }

    }
}
