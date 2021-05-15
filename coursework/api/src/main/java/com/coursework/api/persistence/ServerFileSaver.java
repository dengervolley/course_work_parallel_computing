package com.coursework.api.persistence;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerFileSaver implements IFileSaver {
    private final String basePath = "index/files/";

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        var fileName = basePath + file.getOriginalFilename();
        File f = new File(fileName);

        if (!f.exists()) {
            f.createNewFile();
        }

        var stream = new FileOutputStream(f);
        stream.write(file.getBytes());
        stream.close();

        return fileName;
    }
}
