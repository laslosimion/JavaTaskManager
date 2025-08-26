package com.example.barricade.repository;

import com.example.barricade.domain.Task;
import com.example.barricade.domain.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByUserId(UUID userId, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByUserIdAndStatus(UUID userId, TaskStatus status, Pageable pageable);

}
