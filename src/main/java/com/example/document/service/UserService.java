package com.example.document.service;

import com.example.document.entity.Authority;
import com.example.document.entity.User;
import com.example.document.repository.AuthorityRepository;
import com.example.document.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);
    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final SessionRegistry sessionRegistry;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository, SessionRegistry sessionRegistry) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.sessionRegistry = sessionRegistry;
    }

    public List<User> getAllUsers()
    {
        log.info("IN getAllUsers {}  users found",userRepository.findAll().size());
        return userRepository.findAll();
    }


    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority("ROLE_ADMIN");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        System.out.println(user.getAuthorities());
        userRepository.save(user);
        authorityRepository.save(authority);
        log.info("IN saveUser user saved successfully {}",user);
    }


    public List<Object> getAllLogedUsers()
    {
        log.info("IN getAllLogedUsers {} Logged users found",sessionRegistry.getAllPrincipals().size());
        return sessionRegistry.getAllPrincipals();
    }

    public List<String> getAllUsersUsernames() {
        List<User> users =userRepository.findAll();
        List<String> allUsernames = users.stream().map(user1 -> user1.getUsername()).collect(Collectors.toList());
        log.info("IN getAllUsersUsernames {} users found",allUsernames);
        return allUsernames;
    }

    public List<User> searchUsersByName(String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        users.sort(Comparator.comparing(user1 -> user1.getUsername()));
        log.info("IN searchUsersByName User found by name {}",username);
        return users;
    }

    public User getUserByName(String name)
    {
        log.info("IN getUserById User found by name {}",name);
        return userRepository.getUserByName(name);
    }

    public User getUserById(Integer id)
    {
        log.info("IN getUserById User found by id {}",id);
        return userRepository.getOne(id);
    }

    public User getCurrentLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("IN getCurrentLoggedUser current User is {}",auth.getName());
        return userRepository.getUserByName(username);
    }


}
