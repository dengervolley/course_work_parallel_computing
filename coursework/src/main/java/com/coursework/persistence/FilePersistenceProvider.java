package com.coursework.persistence;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ILogger;
import com.coursework.models.IndexItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilePersistenceProvider implements IPersistenceProvider {

    private String filePath;
    private final ILogger logger;

    public FilePersistenceProvider(ILogger logger) {
        this.logger = logger;
    }

    public void setPath(String file) {
        this.filePath = file;
    }

    @Override
    public void persistIndex(InverseIndex index) {
        var json = index.toString();
        try {
            var file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            var writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            logger.logError(e);
        }
    }

    public List<IndexItem> readIndex(String path) {
        var mapper = new ObjectMapper();
        var file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        List<IndexItem> indexItems;

        try {
            indexItems = new ArrayList<>(Arrays.asList(mapper.readValue(Paths.get(path).toFile(), IndexItem[].class)));
        } catch (IOException e) {
            logger.logError(e);
            return new ArrayList<>();
        }
        return indexItems;
    }
}
