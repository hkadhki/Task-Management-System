package com.example.taskmanagersystem.repository;

import com.example.taskmanagersystem.model.CommentEntity;
import com.example.taskmanagersystem.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on {@link CommentEntity} objects.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
