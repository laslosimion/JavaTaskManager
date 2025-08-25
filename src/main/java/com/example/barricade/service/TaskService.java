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

@Service
public class TaskService {
    private final TaskRepository tasks;
    private final UserService userService;

    public TaskService(TaskRepository tasks, UserService userService) {
        this.tasks = tasks;
        this.userService = userService;
    }

    @Transactional
    public TaskDtos.TaskResponse create(TaskDtos.CreateTaskRequest req) {
        User owner = userService.getCurrentAuthenticatedUser();
        Task t = new Task();
        t.setTitle(req.title);
        t.setDescription(req.description);
        t.setUser(owner);
        tasks.save(t);
        return map(t);
    }

    @Transactional(readOnly = true)
    public Page<TaskDtos.TaskResponse> list(TaskStatus status, Pageable pageable) {
        User owner = userService.getCurrentAuthenticatedUser();
        if (status == null) {
            return tasks.findByUser_Id(owner.getId(), pageable).map(TaskService::map);
        } else {
            return tasks.findByUser_IdAndStatus(owner.getId(), status, pageable).map(TaskService::map);
        }
    }

    @Transactional
    public TaskDtos.TaskResponse updateStatus(UUID id, TaskStatus newStatus) {
        Task t = tasks.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        User owner = userService.getCurrentAuthenticatedUser();
        if (!t.getUser().getId().equals(owner.getId())) {
            throw new ForbiddenException("You cannot modify someone else's task");
        }

        TaskStatus current = t.getStatus();
        if (current == newStatus) return map(t);

        boolean ok = (current == TaskStatus.TODO && newStatus == TaskStatus.IN_PROGRESS)
                || (current == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.DONE);
        if (!ok) throw new ConflictException("Invalid status transition from " + current + " to " + newStatus);

        t.setStatus(newStatus);
        tasks.save(t);
        return map(t);
    }

    @Transactional
    public void delete(UUID id) {
        Task t = tasks.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        User owner = userService.getCurrentAuthenticatedUser();
        if (!t.getUser().getId().equals(owner.getId())) {
            throw new ForbiddenException("You cannot delete someone else's task");
        }
        tasks.delete(t);
    }

    private static TaskDtos.TaskResponse map(Task t) {
        TaskDtos.TaskResponse res = new TaskDtos.TaskResponse();
        res.id = t.getId();
        res.title = t.getTitle();
        res.description = t.getDescription();
        res.status = t.getStatus();
        res.userId = t.getUser().getId();
        return res;
    }
}
