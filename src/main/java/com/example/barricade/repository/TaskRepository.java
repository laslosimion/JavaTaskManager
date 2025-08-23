package com.example.barricade.repository;

import com.example.barricade.domain.Task;
import com.example.barricade.domain.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUser_Id(UUID userId);
    Page<Task> findByUser_Id(UUID userId, Pageable pageable);
    Page<Task> findByUser_IdAndStatus(UUID userId, TaskStatus status, Pageable pageable);
}
