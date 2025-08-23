package com.example.barricade.api.dto;

import com.example.barricade.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class TaskDtos {
    public static class CreateTaskRequest {
        @NotBlank @Size(min=1, max=140)
        public String title;
        public String description;
        @NotNull
        public UUID userId;
    }

    public static class UpdateTaskStatusRequest {
        @NotNull
        public TaskStatus status;
    }

    public static class TaskResponse {
        public UUID id;
        public String title;
        public String description;
        public TaskStatus status;
        public UUID userId;
    }
}
