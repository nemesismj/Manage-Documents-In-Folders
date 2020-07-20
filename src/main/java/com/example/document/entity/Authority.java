package com.example.document.entity;



import javax.persistence.*;


@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String authority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public String setAuthority(String authority) {
        this.authority = authority;
        return authority;
    }

    public Authority() {
    }

    public Authority(long id, String username, String authority) {

        this.id = id;
        this.username = username;
        this.authority = authority;
    }

}
