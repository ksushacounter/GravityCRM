package com.crm.gravity.model.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadDir = "uploads/documents/";

    public String saveDocument(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой!");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir, uniqueFileName);

        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}