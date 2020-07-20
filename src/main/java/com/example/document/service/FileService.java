package com.example.document.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private static String FILE_BASE_LOCATION = "/files/";

    public String saveFileToLocal(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(FILE_BASE_LOCATION + fileName);
        try {

            while (Files.exists(path)) {
                fileName = StringUtils.cleanPath(
                        new StringBuilder(
                                fileName).insert(file.getOriginalFilename().lastIndexOf("."), "-").toString());
                path = Paths.get(FILE_BASE_LOCATION + fileName);
            }
            try {
                Files.createDirectories(path);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            System.out.println(path.toAbsolutePath().toString());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public Resource getResource(String fileName) {
        Path path = Paths.get(FILE_BASE_LOCATION + fileName);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resource;
    }

    public void deleteFile(String fileName) throws IOException {

        Path fileToDeletePath = Paths.get(FILE_BASE_LOCATION+fileName);
        Files.delete(fileToDeletePath);
    }
}