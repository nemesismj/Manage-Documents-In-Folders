package com.example.document.service;

import com.example.document.entity.Folder;
import com.example.document.entity.User;
import com.example.document.repository.DocumentRepository;
import com.example.document.repository.FolderRepository;
import com.example.document.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FolderService {
    Logger log = LoggerFactory.getLogger(FolderService.class);
    @PersistenceContext
    private EntityManager entityManager;
    private final DocumentRepository documentRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    public FolderService(ModelMapper modelMapper, DocumentRepository documentRepository, FileService fileService, FolderRepository folderRepository, UserRepository userRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
        this.fileService = fileService;
        this.folderRepository=folderRepository;
        this.userRepository = userRepository;
        this.userService = userService;

    }
    public List<Folder> getFolders(User user){
        log.info("IN getFolders Folders found by User - {}, quantity - {}",user.getUsername(),folderRepository.findByFolderName(user.getUsername()).size());
        return folderRepository.findByFolderName(user.getUsername());
    }


    @Transactional
    public void saveFolder(Folder folder,Authentication authentication) {
        folder.setUsername(authentication.getName());
        User currentUser = userService.getCurrentLoggedUser();
        List<Folder> folderinho = getFolders(currentUser);
        folderRepository.save(folder);
        Set<Folder> folders = new HashSet<>();
        folders.add(folder);
       // System.out.println(currentUser.getAuthorities());
        log.info("IN saveFolder Folder saved successfully {}",folder.getName());
        userRepository.save(currentUser);
    }

    @Transactional
    public void saveFolderInsideFolder(Folder folder,Authentication authentication,Integer id) {
        folder.setUsername(authentication.getName());
        Folder forLastIndex = folderRepository.findTopByOrderByIdDesc();
        folder.setId(forLastIndex.getId()+1);
        User currentUser = userService.getCurrentLoggedUser();
        Folder folderw = folderRepository.getFolderById(id);
        folder.setPackagename(folderw.getName());
        System.out.println("folder + "+folder);
        folderRepository.save(folder);
        Set<Folder> folders = new HashSet<>();
        folders.add(folder);
        //System.out.println(currentUser.getAuthorities());
        log.info("IN saveFolderInsideFolder Folder saved inside Package successfully {}",folder.getName());
        userRepository.save(currentUser);
    }



    public Folder getFolderByName(String id)
    {
        log.info("IN getFolderByName Folders found by - name {}",id);
        return folderRepository.getFolderByName(id);
    }


    public Folder getFolderByA(String folderA, String name){
        return folderRepository.getFolderByA(folderA,name);
    }

    public  List<Folder> getFolderByAccess(String access, String name){
        log.info("IN getFolderByAccess List of folders found by - access {}, username {}, quantity {}",access,name,  folderRepository.getFolderByAccess(access,name).size());
        return folderRepository.getFolderByAccess(access,name);
    }


    public List<Folder> getFolderByPackage(String packagename,String name){
        log.info("IN getFolderByPackage List of folders found by - packagename {}, username {}, quantity {}",packagename,name,folderRepository.getFolderByPackage(packagename, name).size());
        return folderRepository.getFolderByPackage(packagename, name);
    }

    public List<Folder>  getFoldersWithNoPackage(String username){
        log.info("IN getFoldersWithNoPackage List of folders found by - username {}, quantity {}",username,folderRepository.getFoldersWithNoPackage(username).size());
        return folderRepository.getFoldersWithNoPackage(username);
    }
    public List<Folder>  getFolderByAccessAndNoPackage(String access,String username){
        log.info("IN getFolderByAccessAndNoPackage List of folders found by - username {}, and access {}, quantity {}",username,access,folderRepository.getFolderByAccessAndNoPackages(access,username));
        return folderRepository.getFolderByAccessAndNoPackages(access,username);
    }
    public List<Folder>  getFoldersWithNoPackage1(String username){
        log.info("IN getFoldersWithNoPackage1 List of folders found by - username {}, quantity {}",username,folderRepository.getFoldersWithNoPackage1(username).size());
        return folderRepository.getFoldersWithNoPackage1(username);
    }
   public Folder getFolderById(Integer id){
       log.info("IN getFolderById Folder found by - id {}",id);
        return folderRepository.getOne(id);
   }

    public Long quantityOfPackages(String username){
        log.info("IN quantityOfPackages - {} documents found by username {}",folderRepository.quantityOfPackages(username),username);
        return folderRepository.quantityOfPackages(username);
    }
    public Long quantityOfFolders(String username){
        log.info("IN quantityOfFolders - {} documents found by username {}",folderRepository.quantityOfFolders(username),username);
        return folderRepository.quantityOfFolders(username);
    }



}
