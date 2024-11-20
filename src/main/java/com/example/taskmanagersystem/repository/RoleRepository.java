package com.example.taskmanagersystem.repository;

import com.example.taskmanagersystem.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link RoleEntity} objects.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    /**
     * Find a role by its name.
     *
     * @param name the name of the role to find.
     * @return the role entity with the given name, or {@code null} if not found.
     */
    RoleEntity findByName(String name);
}
