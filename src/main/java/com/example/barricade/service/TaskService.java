package com.example.barricade.service;

import com.example.barricade.api.dto.TaskDtos;
import com.example.barricade.domain.Task;
import com.example.barricade.domain.TaskStatus;
import com.example.barricade.domain.User;
import com.example.barricade.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository tasks, UserService userService) {
        this.taskRepository = tasks;
        this.userService = userService;
    }

    private UUID getCurrentUserId() {
        String userIdStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(userIdStr);
    }

    @Transactional
    public TaskDtos.TaskResponse create(TaskDtos.CreateTaskRequest req) {
        UUID userId = getCurrentUserId();

        Task task = new Task();
        task.setTitle(req.title);
        task.setDescription(req.description);
        task.setStatus(TaskStatus.TODO);
        task.setUserId(userId);

        Task saved = taskRepository.save(task);
        return TaskDtos.TaskResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<TaskDtos.TaskResponse> list(UUID userId, TaskStatus status, Pageable pageable) {
        return taskRepository.findByUserIdAndStatus(userId, status, pageable)
                .map(TaskDtos.TaskResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<TaskDtos.TaskResponse> list(UUID userId, Pageable pageable) {
        return taskRepository.findByUserId(userId, pageable)
                .map(TaskDtos.TaskResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<TaskDtos.TaskResponse> list(TaskStatus status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable)
                .map(TaskDtos.TaskResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<TaskDtos.TaskResponse> list(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(TaskDtos.TaskResponse::fromEntity);
    }

    @Transactional
    public TaskDtos.TaskResponse updateStatus(UUID id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        User owner = userService.getCurrentAuthenticatedUser();

        if (!task.getUserId().equals(owner.getId())) {
            throw new ForbiddenException("You cannot modify someone else's task");
        }

        TaskStatus current = task.getStatus();
        if (current == newStatus) {
            return map(task); // No change, return current state
        }

        boolean validTransition = (current == TaskStatus.TODO && newStatus == TaskStatus.IN_PROGRESS)
                || (current == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.DONE);

        if (!validTransition) {
            throw new ConflictException("Invalid status transition from " + current + " to " + newStatus);
        }

        task.setStatus(newStatus);
        taskRepository.save(task);

        return map(task);
    }

    // Helper mapping function
    private static TaskDtos.TaskResponse map(Task t) {
        TaskDtos.TaskResponse res = new TaskDtos.TaskResponse();
        res.id = t.getId();
        res.title = t.getTitle();
        res.description = t.getDescription();
        res.status = t.getStatus();
        res.userId = t.getUserId();
        return res;
    }

    @Transactional
    public void delete(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        UUID currentUser = getCurrentUserId();
        if (!task.getUserId().equals(currentUser)) {
            throw new RuntimeException("Forbidden: you cannot delete someone else's task");
        }

        taskRepository.delete(task);
    }
}
