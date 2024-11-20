package com.user_management.repository;

import com.user_management.model.Role;
import com.user_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailId(String emailId);
    Optional<User> findByRole(Role role);
    Boolean existsByEmailId(String emailId);
}
