package com.angel.orchestrator_service.services.api;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    void saveDataFromFile(MultipartFile file) throws IOException;
    void saveFile(MultipartFile file);
}
