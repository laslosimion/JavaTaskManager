package com.example.barricade.api.dto;

import com.example.barricade.domain.Task;
import com.example.barricade.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class TaskDtos {

    public static class CreateTaskRequest {

        @NotBlank
        @Size(min = 1, max = 140)
        public String title;

        @Size(max = 2000)
        public String description;
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

        public static TaskResponse fromEntity(Task task) {
            TaskResponse dto = new TaskResponse();
            dto.id = task.getId();
            dto.title = task.getTitle();
            dto.description = task.getDescription();
            dto.status = task.getStatus();
            dto.userId = task.getUserId();
            return dto;
        }
    }
}
