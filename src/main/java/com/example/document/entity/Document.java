package com.example.document.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "file")
public class Document {

    @Id
    private String fileName;

    @CreationTimestamp
    private Date insertionDate;

    private String fileType;

    private String userUploader;

   // @OneToMany(fetch = FetchType.EAGER, mappedBy = "userUploader")
    private String folderName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileName" ,cascade = CascadeType.ALL)
    private Set<DocumentAuthority> documentAuthorities;

    public Document(String fileName, Date insertionDate, String fileType, String userUploader, Set<DocumentAuthority> documentAuthorities) {
        this.fileName = fileName;
        this.insertionDate = insertionDate;
        this.fileType = fileType;
        this.userUploader = userUploader;
        this.documentAuthorities = documentAuthorities;
    }

    public Document() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUserUploader() {
        return userUploader;
    }

    public void setUserUploader(String userUploader) {
        this.userUploader = userUploader;
    }

    public Set<DocumentAuthority> getDocumentAuthorities() {
        return documentAuthorities;
    }

    public void setDocumentAuthorities(Set<DocumentAuthority> documentAuthorities) {
        this.documentAuthorities = documentAuthorities;
    }

   public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

   /* public Set<Folder> getFolderName() {
        return folderName;
    }

    public void setFolderName(Set<Folder> folderName) {
        this.folderName = folderName;
    }*/
}
