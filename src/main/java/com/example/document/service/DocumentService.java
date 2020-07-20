package com.example.document.service;

import com.example.document.dto.DocumentDTO;
import com.example.document.entity.Document;
import com.example.document.entity.DocumentAuthority;
import com.example.document.entity.Folder;
import com.example.document.entity.User;
import com.example.document.repository.DocumentAuthorityRepository;
import com.example.document.repository.DocumentRepository;

import com.example.document.repository.FolderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DocumentService {
    Logger log = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final FolderService folderService;
    private final UserService userService;
    private final FolderRepository folderRepository;
    private final DocumentAuthorityRepository documentAuthorityRepository;
    @Autowired
    public DocumentService(ModelMapper modelMapper, DocumentRepository documentRepository, FileService fileService, FolderService folderService, UserService userService, FolderRepository folderRepository, DocumentAuthorityRepository documentAuthorityRepository) {
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
        this.fileService = fileService;
        this.folderService = folderService;
        this.userService = userService;
        this.folderRepository = folderRepository;
        this.documentAuthorityRepository = documentAuthorityRepository;
    }


    public List<Document> getAllDocuments(Authentication authentication) {
        log.info("IN getAllDocuments - {} documents found", documentRepository.findAll().size());
        return documentRepository.findAllByAuthorities(
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

    }

    public List<Document> getDocuments(Authentication auth){
        log.info("IN getDocuments - {} documents found by Username", auth.getName());
        return documentRepository.findByUserName(auth.getName());
    }

    public List<Document> getDocumentsByName(String auth){
        log.info("IN getDocumentsByName - {} documents found by Username", auth);
        return documentRepository.findByUserName(auth);
    }


    public List<Document> getDocumentsByFolderA(Authentication auth, Folder id){
        User currentUser = userService.getCurrentLoggedUser();
        Folder folder = folderService.getFolderById(id.getId());
        log.info("IN getDocumentsByFolderA - {} documents found by Folder", documentRepository.getDocumentsByFolder(auth.getName(), folder.getName()).size());
        return documentRepository.getDocumentsByFolder(auth.getName(), folder.getName());
    }

    public List<Document> getDocumentsByFolderAC(String auth, Folder id){
        User currentUser = userService.getCurrentLoggedUser();
        Folder folder = folderService.getFolderById(id.getId());
        log.info("IN getDocumentsByFolderAC - {} documents found by Folder", documentRepository.getDocumentsByFolder(auth, folder.getName()).size());
        return documentRepository.getDocumentsByFolder(auth, folder.getName());
    }


    public ModelAndView getAllDocumentsByFolderAsModel(Authentication authentication, Integer id) {
        ArrayList<DocumentDTO> documents;
        User currentUser = userService.getCurrentLoggedUser();
        List<Folder> folder = folderService.getFolders(currentUser);

        documents = getDocumentsByFolderA(authentication,folder.get(id))
                .stream()
                .map((document)->modelMapper.map(document,DocumentDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));
        return new ModelAndView("folder", "documents", documents);
    }

    public Document saveDocument(MultipartFile file, String updatedFileName, Authentication authentication, Integer id) {
        Document document = new Document();
        document.setFileName(updatedFileName);
        document.setFileType(file.getContentType());
        document.setUserUploader(authentication.getName());
        User currentUser = userService.getCurrentLoggedUser();
        List<Folder> folder = folderService.getFolders(currentUser);
        Folder folder1 = folderService.getFolderById(id);
        document.setFolderName(folder1.getName());
        document.setDocumentAuthorities(
                authentication.getAuthorities()
                        .stream()
                        .map((userAuth) -> new DocumentAuthority(null, updatedFileName, userAuth.getAuthority()))
                        .collect(Collectors.toSet()));
        log.info("IN saveDocument - {} document saved succesfully ", document);
        return documentRepository.save(document);
    }

    public ResponseEntity<Resource> getResponseEntity(String fileName) {
        Resource resource = fileService.getResource(fileName);
        Optional<Document> documentOptional = documentRepository.findById(fileName);
        ResponseEntity.BodyBuilder ok = ResponseEntity.ok();
        log.info("IN getResponseEntity - {} getResponseEntity request",resource);
        ok = documentOptional.isPresent() ?
                ok.contentType(MediaType.parseMediaType(documentOptional.get().getFileType())) :
                ok;
        return ok.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Transactional
    public void deleteFile(String fileName) throws IOException {
        documentAuthorityRepository.deleteByFileName(fileName);
        documentRepository.deleteById(fileName);
        fileService.deleteFile(fileName);
        log.info("IN deleteFile - {} File successfully deleted",fileName);
    }

    public ModelAndView getAllDocumentsAsModel(Authentication authentication) {
        ArrayList<DocumentDTO> documents;

        documents = getAllDocuments(authentication)
                .stream()
                .map((document)->modelMapper.map(document,DocumentDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));
        return new ModelAndView("filesOverview", "documents", documents);
    }
    public Long quantityOfDocuments(String username){
        log.info("IN quantityOfDocuments - {} documents found by username {}",documentRepository.quantityOfDocuments(username),username);
        return documentRepository.quantityOfDocuments(username);
    }

}
