package com.example.document.repository;

import com.example.document.entity.Document;
import com.example.document.entity.Folder;
import com.example.document.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
    @Query(value = "SELECT u FROM Folder u where u.username like %?1%")
    List<Folder> findByFolderName(String username);
    @Query(value = "SELECT u FROM Folder u WHERE u.foldername = :name")
    Folder getFolderByName(@Param("name") String username);
    @Query(value = "SELECT u FROM Folder u WHERE u.folderAccess like %?1% and u.username like %?2%")
    Folder getFolderByA(String folderA, String username);
    @Query(value = "SELECT u FROM Folder u WHERE u.folderAccess like %?1% and u.username like %?2%")
    List<Folder> getFolderByAccess(String acess, String username );
    @Query(value = "SELECT u FROM Folder u WHERE u.folderAccess like %?1% and u.username like %?2% and u.packagename is null")
    List<Folder> getFolderByAccessAndNoPackages(String access, String username);

    @Query(value = "SELECT u FROM Folder u WHERE u.packagename like %?1% and u.username like %?2%")
    List<Folder> getFolderByPackage(String packages, String username );

    @Query(value = "SELECT u FROM Folder u WHERE u.packagename is not null and u.username like %?1%")
    List<Folder> getFoldersWithNoPackage(String username);
    @Query(value = "SELECT u FROM Folder u WHERE u.packagename is null and u.username like %?1%")
    List<Folder> getFoldersWithNoPackage1(String username );
    @Query(value = "SELECT COUNT(u) FROM Folder u WHERE u.packagename is null and u.username like %?1%")
    Long quantityOfPackages(String username);
    @Query(value = "SELECT COUNT(u) FROM Folder u WHERE u.packagename is not null and u.username like %?1%")
    Long quantityOfFolders(String username);

    Folder getFolderById(Integer id);
    Folder findTopByOrderByIdDesc();







}
