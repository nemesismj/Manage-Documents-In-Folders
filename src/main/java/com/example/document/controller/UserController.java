package com.example.document.controller;

import com.example.document.entity.Document;
import com.example.document.entity.Folder;
import com.example.document.entity.User;
import com.example.document.repository.FolderRepository;
import com.example.document.service.DocumentService;
import com.example.document.service.FolderService;
import com.example.document.service.MyUserDetailsService;
import com.example.document.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderRepository folderRepository;

    @RequestMapping(value = "/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        log.info("IN registerUser - visited RegisterPage");
        return "registerform";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult bindingResult) {
        //List<String> allUsernames = userService.getAllUsersUsernames();
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();
        String repeatedPassword = user.getRepeatedPassword();
        List<String> allUsernames = userService.getAllUsersUsernames();
        Boolean enable = user.setEnabled(true);
        //log.debug("IN registerUser - visited RegisterPage");
        if (bindingResult.hasErrors() || allUsernames.contains(username) || !password.equals(repeatedPassword)) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "registerform";
        }
        else {
            userService.saveUser(user);
            return "redirect:/login"; }
    }

    @RequestMapping(value = "/login")
    public String login() {
       // log.info("IN login - visited LoginPage");
        return "login";

    }


    @RequestMapping(value = {"/myprofile"})
    public String getMyProfile(Model model, Authentication auth) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        List<Document> documents;
        documents = documentService.getDocuments(auth);
        List<Folder> folderwithnopackages = folderService.getFoldersWithNoPackage1(auth.getName());
        List<Folder> folders = folderService.getFolders(currentUser);
        log.debug("IN getMyProfile - visited myprofile page " + auth.getName());
        model.addAttribute("user", currentUser);
        model.addAttribute("username", currentUsername);
        model.addAttribute("documents",documents);
        model.addAttribute("folder",folderwithnopackages);

        return "myprofile";
    }


    @RequestMapping(value = "/user/{name}")
    public String getUserById(@PathVariable String name, Model model, Authentication auth) {
        User userById = userService.getUserByName(name);
        String userByIdUsername = userById.getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        List<Document> documents;
        documents = documentService.getDocumentsByName(name);
        String Access = "fprivate";
        List<Folder> folders = folderService.getFolderByAccessAndNoPackage("fpublic",name);
        List<Folder> folders2 = folderService.getFolderByAccess("fprivate",name);
        Long q = folderService.quantityOfPackages(name);
        Long q1 = folderService.quantityOfFolders(name);
        Long q2 = documentService.quantityOfDocuments(name);

       // System.out.println(folders);
        if (userByIdUsername.equals(currentUsername)) {
            log.debug("IN getUserById - found your own page {}",name);
            return "redirect:/myprofile";}
        else {

                model.addAttribute("user", userById);
                model.addAttribute("username", currentUsername);
                model.addAttribute("documents", documents);
                model.addAttribute("curuser",currentUser);
                model.addAttribute("folder", folders);
                model.addAttribute("quantitypackages",q);
                model.addAttribute("quantityfolders",q1);
                model.addAttribute("quantitydocuments",q2);
                log.debug("IN getUserById - tracked User by name {}",name);
                return "user";
            }


        }


   /* @RequestMapping(value = "/user/byname/{name}")
    public String getUserByName(@PathVariable String name) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();

        if (name.equals(currentUsername)) {
            return "redirect:/myprofile"; }
        else {
            String byNameUserId = userService.getUserByName(name).getUsername();
            return "redirect:/user/" + byNameUserId;}
    }*/

    @RequestMapping(value = "/user/{name}/{id}")
    public String getUserFolderById(@PathVariable String name,@PathVariable Integer id,Model model, Authentication auth) {
        User userById = userService.getUserByName(name);
        String userByIdUsername = userById.getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        List<Folder> folder = folderService.getFolders(currentUser);
        Folder folder1 = folderService.getFolderById(id);
        List<Document> documents; documents = documentService.getDocumentsByFolderAC(name,folder1);
        Folder folderById = folderService.getFolderById(folder1.getId());
        List<Folder> folderspackage = folderService.getFolderByPackage(folder1.getName(),name);
        if(folderById.getFolderAccess().equals("fprivate")){
            log.error("IN getUserFolderById - trying to visit private folder Access Denied");
            return "redirect:/error";
        }
            model.addAttribute("user", userById);
            model.addAttribute("curuser",currentUser);
            model.addAttribute("username", currentUsername);
            model.addAttribute("documents", documents);
            model.addAttribute("folder", folderById);
             model.addAttribute("packages", folderspackage);
        log.debug("IN getUserFolderById - tracked User by name {} and his folder {}",name,folder1.getName());
            return "folder2";
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUs(@ModelAttribute("user") User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String email = user.getEmail();
        String repeatedPassword = user.getRepeatedPassword();
        List<String> allUsernames = userService.getAllUsersUsernames();
        Boolean enable = user.setEnabled(true);
        userService.saveUser(user);
        log.debug("IN saveUs - User successfuly saved {}",user.getUsername());
        log.debug("Edit done " + user.getUsername());
        return "redirect:/myprofile";
    }

    @RequestMapping("/settings")
    public ModelAndView showEditItemPage() {
        ModelAndView mav = new ModelAndView("settings");
        User currentUser = userService.getCurrentLoggedUser();
        mav.addObject("user", currentUser);
        return mav;
    }

}
