package com.example.document.controller;

import com.example.document.dto.FolderDTO;
import com.example.document.entity.*;
import com.example.document.repository.FolderRepository;
import com.example.document.service.FileService;
import com.example.document.service.FolderService;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import com.example.document.dto.DocumentDTO;
import com.example.document.entity.Document;
import com.example.document.entity.User;
import com.example.document.service.DocumentService;
import com.example.document.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.print.Doc;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FolderController {
    Logger log = LoggerFactory.getLogger(FolderController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FolderRepository folderRepository;



    @RequestMapping(value = "/createFolder")
    public String newFolder(Model model, Authentication auth) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        List<Document> documents;
        documents = documentService.getDocuments(auth);
        List<Folder> folderwithnopackages = folderService.getFoldersWithNoPackage1(auth.getName());
        List<Folder> folders = folderService.getFolders(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("username", currentUsername);
        model.addAttribute("documents", documents);
        model.addAttribute("folders", folderwithnopackages);
        model.addAttribute("folder", new Folder());
        model.addAttribute("user", currentUser);
        log.info("IN newFolder - create package page");
        return "folderform";
    }

    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public String saveFolder(@Valid Folder folder, Authentication auth, BindingResult bindingResult) {
        // List<String> allUsernames = userService.getAllUsersUsernames();
        User currentUser = userService.getCurrentLoggedUser();
        Integer folderId = folder.getId();
        String folderName = folder.getName();
        String folderCreator = folder.getUsername();
        String folderAccess = folder.getFolderAccess();
        String packageName = folder.getPackagename();
        Date date = folder.getCreationDate();
        System.out.println("THIS IS ID " + folderRepository.findAll().stream().count());
        List<Folder> foleds = folderRepository.findByFolderName(currentUser.getUsername());
        System.out.println("FOLEDS " + foleds);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            log.error("IN saveFolder -  package not saved found some errors");
            return "folderform";
        } else {
            folderService.saveFolder(folder, auth);
            log.debug("IN saveFolder -  package successfuly saved {}",folder);
            return "redirect:/myprofile";
        }
    }
    @RequestMapping(value = "/myprofile/{id}/createFolderInsidePackage")
    public String newFolderInsidePackage(Model model, Authentication auth, @PathVariable Integer id) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        List<Document> documents = documentService.getDocuments(auth);
        List<Folder> folderwithnopackages = folderService.getFoldersWithNoPackage(currentUser.getUsername());
        List<Folder> folders = folderService.getFolders(currentUser);
        Folder packag = folderService.getFolderById(id);
        model.addAttribute("user", currentUser);
        model.addAttribute("username", currentUsername);
        model.addAttribute("documents", documents);
        model.addAttribute("folders", folderwithnopackages);
        model.addAttribute("folder", new Folder());
        model.addAttribute("user", currentUser);
        model.addAttribute("package",packag);
        log.info("IN newFolderInsidePackage -  visited create folder page");
        return "folderform2";
    }

    @RequestMapping(value = "/myprofile/{id}/createFolderInsidePackage", method = RequestMethod.POST)
    public String saveFolderInsidePackage(@Valid Folder folder, @PathVariable Integer id, Authentication auth, BindingResult bindingResult) {
        // List<String> allUsernames = userService.getAllUsersUsernames();
       // User currentUser = userService.getCurrentLoggedUser();
        Integer folderId = folder.getId();
        String folderName = folder.getName();
        String folderCreator = folder.getUsername();
        String folderAccess = folder.getFolderAccess();
        String packageName = folder.getPackagename();
        Date date = folder.getCreationDate();
        //System.out.println("THIS IS ID " + folderRepository.findAll().stream().count());
        List<Folder> foleds = folderRepository.findByFolderName(auth.getName());
       // System.out.println("FOLEDS " + foleds);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            log.error("IN saveFolder -  folder not saved found some errors");
            return "folderform2";
        } else {
            folderService.saveFolderInsideFolder(folder, auth, id);
            log.debug("IN saveFolder -  folder successfuly saved {} inside package {}",folder.getName(),folderService.getFolderById(id).getName());
            return "redirect:/myprofile";
        }
    }


    @RequestMapping(value = "/myprofile/{id}")
    public String getFolderById(@PathVariable Integer id, Model model, Authentication auth) {

        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        List<Folder> folder = folderService.getFolders(currentUser);
        Folder folder1 = folderService.getFolderById(id);
        List<Document> documents;
        documents = documentService.getDocumentsByFolderA(auth, folder1);
        Folder folderById = folderService.getFolderById(folder1.getId());
        List<Folder> folderspackage = folderService.getFolderByPackage(folder1.getName(), auth.getName());
        Folder packag = folderService.getFolderById(id);
        model.addAttribute("folder", folderById);
        model.addAttribute("documents", documents);
        model.addAttribute("document", new Document());
        model.addAttribute("user", currentUser);
        model.addAttribute("packages", folderspackage);
        model.addAttribute("package", packag);
        log.debug("IN getFolderById -  visited folder {}", folder1.getName());
        return "folder";
    }

    @RequestMapping(value = "myprofile/{id}/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Integer id, Authentication authentication, @ModelAttribute ArrayList<DocumentDTO> documents, @ModelAttribute ArrayList<FolderDTO> folders) {
        String updatedFileName = fileService.saveFileToLocal(file);
        User currentUser = userService.getCurrentLoggedUser();
        List<Folder> folder = folderService.getFolders(currentUser);
        Folder folder1 = folderService.getFolderById(id);

        documentService.saveDocument(file, updatedFileName, authentication, folder1.getId());
        log.debug("IN uploadFile -  file successfully uploaded {}", updatedFileName);
        return "redirect:/myprofile/{id}";
        // return documentService.getAllDocumentsByFolderAsModel(authentication,folder1.getId());
    }


    @RequestMapping(value = "/myprofile/byname/{name}")
    public String getFolderByName(@PathVariable String name) {
        Integer byNameUserId = folderService.getFolderByName(name).getId();
        log.debug("IN getFolderByName -  tracked Folder by name  {}", name);
        return "redirect:/myprofile/" + byNameUserId;
    }


}
