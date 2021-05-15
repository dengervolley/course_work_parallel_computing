package com.coursework.webapi.persistance;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSaver {
    public String saveFile(MultipartFile file) throws IOException;
}
