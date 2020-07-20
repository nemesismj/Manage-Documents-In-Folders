package com.example.document.repository;

import com.example.document.entity.Document;
import com.example.document.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<Document, String> {

    @Query("select distinct d From Document d " +
            "join d.documentAuthorities auth " +
            "where auth.fileAuthority in :authorities")
    List<Document> findAllByAuthorities(List<String> authorities);
    @Query(value = "SELECT u FROM Document u where u.userUploader like %?1%")
    List<Document> findByUserName(String username);
    @Query(value = "SELECT u FROM Document u where u.userUploader like %?1% and u.folderName like %?2%")
    List<Document> getDocumentsByFolder(String username, String foldername);
    @Query(value = "SELECT u FROM Document u where u.userUploader like %?1% and u.folderName like %?2%")
    List<Document> getDocumentByPackage(String username, String packagename);
    @Query(value = "SELECT COUNT(u) FROM Document u where u.userUploader like %?1%")
    Long quantityOfDocuments(String username);
    Optional<Document> findByFileName(String name);
    void deleteByFolderName(String name);
}
