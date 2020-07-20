package com.example.document.repository;

import com.example.document.entity.DocumentAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentAuthorityRepository extends JpaRepository<DocumentAuthority, Long> {
    void deleteByFileName(String username);
}
