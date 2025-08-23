package com.example.barricade.api;

import com.example.barricade.api.dto.UserDtos;
import com.example.barricade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtos.UserResponse create(@Valid @RequestBody UserDtos.CreateUserRequest req) {
        return userService.create(req);
    }

    @GetMapping("/{id}")
    public UserDtos.UserResponse get(@PathVariable("id") UUID id) {
        return userService.get(id);
    }
}
