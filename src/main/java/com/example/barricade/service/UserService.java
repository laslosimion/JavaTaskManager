package com.example.barricade.service;

import com.example.barricade.domain.User;
import com.example.barricade.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public User save(User user) {
        if (users.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        return users.save(user);
    }

    @Transactional(readOnly = true)
    public User getEntity(UUID id) {
        return users.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public java.util.Optional<User> findByEmail(String email) {
        return users.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getCurrentAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("No authenticated user");
        }
        return getEntity(UUID.fromString(auth.getName()));
    }


}