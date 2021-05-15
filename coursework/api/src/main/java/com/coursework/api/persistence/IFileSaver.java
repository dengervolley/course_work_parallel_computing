package com.coursework.api.persistence;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSaver {
    public String saveFile(MultipartFile file) throws IOException;
}
