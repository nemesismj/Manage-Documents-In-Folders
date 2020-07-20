package com.example.document.service;

import com.example.document.entity.User;
import com.example.document.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    Logger log = LoggerFactory.getLogger(MyUserDetailsService.class);
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getUsername(), user.isEnabled(), true, true, true,
                user.getAuthorities().stream()
                        .map((authority) -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList()));
    }
}