package com.example.document.repository;

import com.example.document.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    User getUserByName(@Param("username") String username);

    @Query(value = "SELECT u FROM User u where u.username like %?1%")
    List<User> searchUsersByName(String username);

    List<User> findByUsernameContainingIgnoreCase(String username);

    @Query(value = "SELECT u FROM User u where UPPER(u.username) = UPPER(?1)")
    List<User> findByUsernameIgnoreCase(String username);
}
