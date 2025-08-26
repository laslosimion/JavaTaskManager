package com.example.barricade.api;

import com.example.barricade.api.dto.TaskDtos;
import com.example.barricade.domain.TaskStatus;
import com.example.barricade.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDtos.TaskResponse create(@Valid @RequestBody TaskDtos.CreateTaskRequest req) {
        return taskService.create(req);
    }

    @GetMapping
    public Page<TaskDtos.TaskResponse> list(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int pageIndex = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(pageIndex, size);

        if (userId != null && status != null) {
            return taskService.list(userId, status, pageRequest);
        } else if (userId != null) {
            return taskService.list(userId, pageRequest);
        } else if (status != null) {
            return taskService.list(status, pageRequest);
        } else {
            return taskService.list(pageRequest);
        }
    }


    @PutMapping("/{id}")
    public TaskDtos.TaskResponse updateStatus(
            @PathVariable("id") UUID id,
            @Valid @RequestBody TaskDtos.UpdateTaskStatusRequest req) {
        return taskService.updateStatus(id, req.status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        taskService.delete(id);
    }
}
