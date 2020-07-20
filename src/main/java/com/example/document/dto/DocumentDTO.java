package com.example.document.dto;


import java.util.Date;

public class DocumentDTO {

    private String fileName;

    private Date insertionDate;

    private String fileType;

    private String userUploader;

    private String folderName;

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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
