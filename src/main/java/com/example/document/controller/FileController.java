package com.example.document.controller;

import com.example.document.dto.DocumentDTO;
import com.example.document.dto.FolderDTO;
import com.example.document.entity.Folder;
import com.example.document.entity.User;
import com.example.document.repository.DocumentRepository;
import com.example.document.repository.FolderRepository;
import com.example.document.service.DocumentService;
import com.example.document.service.FileService;

import com.example.document.service.FolderService;
import com.example.document.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class FileController {
    Logger log = LoggerFactory.getLogger(FileController.class);
    private FileService fileService;

    private DocumentService documentService;

    private final FolderService folderService;

    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public FileController(FileService fileService, DocumentService documentService, FolderService folderService, FolderRepository folderRepository, DocumentRepository documentRepository, ModelMapper modelMapper, UserService userService) {
        this.fileService = fileService;
        this.documentService = documentService;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }



   /* @PostMapping(path = "home")
    public ModelAndView upload(@RequestParam("file") MultipartFile file, Authentication authentication, @ModelAttribute ArrayList<DocumentDTO> documents) {
        String updatedFileName = fileService.saveFileToLocal(file);
        documentService.saveDocument(file, updatedFileName, authentication);
        return documentService.getAllDocumentsAsModel(authentication);
    }*/


    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFileFromLocal(@PathVariable String fileName) {
        log.debug("IN downloadFileFromLocal -  download success  {}",fileName);
        return documentService.getResponseEntity(fileName);
    }

    @GetMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName) throws IOException {
        log.debug("IN deleteFile -  delete success  {}",fileName);
        documentService.deleteFile(fileName);
        return "redirect:/myprofile";
    }



}
