package com.example.document.entity;



import javax.persistence.*;


@Entity
@Table(name = "file_authority")
public class DocumentAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(name = "file_authority")
    private String fileAuthority;

    public DocumentAuthority() {
    }

    public DocumentAuthority(Long id, String fileName, String fileAuthority) {
        this.id = id;
        this.fileName = fileName;
        this.fileAuthority = fileAuthority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAuthority() {
        return fileAuthority;
    }

    public void setFileAuthority(String fileAuthority) {
        this.fileAuthority = fileAuthority;
    }
}
