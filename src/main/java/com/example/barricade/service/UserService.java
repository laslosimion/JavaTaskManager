package com.example.barricade.service;

import com.example.barricade.api.dto.UserDtos;
import com.example.barricade.domain.User;
import com.example.barricade.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public UserDtos.UserResponse create(UserDtos.CreateUserRequest req) {
        if (users.existsByEmail(req.email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        User u = new User();
        u.setName(req.name);
        u.setEmail(req.email);
        users.save(u);
        return map(u);
    }

    @Transactional(readOnly = true)
    public UserDtos.UserResponse get(UUID id) {
        User u = users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return map(u);
    }
    
    @Transactional(readOnly = true)
    public User getEntity(UUID id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private static UserDtos.UserResponse map(User u) {
        UserDtos.UserResponse res = new UserDtos.UserResponse();
        res.id = u.getId();
        res.name = u.getName();
        res.email = u.getEmail();
        return res;
    }
}
