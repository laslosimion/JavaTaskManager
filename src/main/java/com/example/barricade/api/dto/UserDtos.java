package com.example.barricade.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class UserDtos {

    public static class CreateUserRequest {
        @NotBlank
        public String name;

        @NotBlank
        @Email
        public String email;
    }

    public static class UserResponse {
        public UUID id;
        public String name;
        public String email; // no password here
    }
}
