package com.example.document.controller;

import com.example.document.entity.User;
import com.example.document.service.DocumentService;
import com.example.document.service.FolderService;
import com.example.document.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class MainController {
    Logger log = LoggerFactory.getLogger(MainController.class);
    private final UserService userService;
    private final FolderService folderService;
    private final DocumentService documentService;
    @Autowired
    public MainController(UserService userService, FolderService folderService, DocumentService documentService) {
        this.userService = userService;
        this.folderService = folderService;
        this.documentService = documentService;
    }
    @RequestMapping(value = {"", "/", "/home"})
    public String HomePage(Model model){
        log.info("IN HomePage - visited Landing Page");
        return "home";
    }
    @RequestMapping(value = "/contact")
    public String ContactPage(Model model){
        log.info("IN ContactPage - visited Contact Page");
        return "ContactPage";
    }
    @RequestMapping(value = "/pricing")
    public String PricingPage(Model model){
        log.info("IN PricingPage - visited Pricing Page");
        return "PricingPage";
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam(value = "search") String username) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        List<User> searchedUsers = userService.searchUsersByName(username.trim());
        Long q = folderService.quantityOfPackages(currentUsername);
        Long q1 = folderService.quantityOfFolders(currentUsername);
        Long q2 = documentService.quantityOfDocuments(currentUser.getUsername());
        model.addAttribute("name", username);
        model.addAttribute("user", searchedUsers);
        model.addAttribute("username", currentUsername);
        model.addAttribute("curuser", currentUser);
        model.addAttribute("quantitypackages",q);
        model.addAttribute("quantityfolders",q1);
        model.addAttribute("quantitydocuments",q2);
        log.debug("IN search - found users {}",searchedUsers.size());
        return "searchresult";
    }
}
