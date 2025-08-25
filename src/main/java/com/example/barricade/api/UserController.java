package com.example.barricade.api;

import com.example.barricade.api.dto.UserDtos;
import com.example.barricade.domain.User;
import com.example.barricade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /users/{id}
     * Returns user information.
     * Only fetching users; creation is done via /auth/register
     */
    @GetMapping("/{id}")
    public UserDtos.UserResponse get(@PathVariable("id") UUID id) {
        User u = userService.getEntity(id);
        return map(u);
    }

    /**
     * Helper to map User entity to DTO
     */
    private static UserDtos.UserResponse map(User u) {
        UserDtos.UserResponse res = new UserDtos.UserResponse();
        res.id = u.getId();
        res.name = u.getName();
        res.email = u.getEmail();
        return res;
    }
}
