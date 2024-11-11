package com.user_management.service;

import com.user_management.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    Optional<User> findUserById(Long id);
    User saveUser(User user);
}
