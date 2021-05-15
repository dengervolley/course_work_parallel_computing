package com.coursework.api.controllers;

import com.coursework.api.persistence.IFileSaver;
import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ILogger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {
    private final IFileSaver fileSaver;
    private final ILogger logger;
    private final InverseIndex inverseIndex;

    public FileController(InverseIndex index, IFileSaver fileSaver, ILogger logger) {
        this.fileSaver = fileSaver;
        this.logger = logger;
        this.inverseIndex = index;
    }

    @PostMapping("upload")
    public void handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            var newFilePath = this.fileSaver.saveFile(file);
            inverseIndex.addFilesToIndex(newFilePath);
        } catch (IOException e) {
            logger.logError(e);
        }
    }
}
