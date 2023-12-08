package com.angel.orchestrator_service.endpoints;

import com.angel.orchestrator_service.services.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/files")
public class FilesController {

    private FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "multipart/form-data")
    @CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Cache-Control", "Content-Type","Access-Control-Allow-Origin"})
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file)
        throws IOException {
        String result = this.fileService.saveDataFromFile(file);
        return ResponseEntity.ok(result);
    }
}
