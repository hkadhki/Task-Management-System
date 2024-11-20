package com.example.taskmanagersystem.repository;

import com.example.taskmanagersystem.model.TaskEntity;
import com.example.taskmanagersystem.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


/**
 * Repository interface for performing CRUD operations and queries on {@link TaskEntity} objects.
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    /**
     * Finds a task by its title.
     *
     * @param title the title of the task to find.
     * @return an {@link Optional} containing the task, or an empty {@link Optional}.
     */
    Optional<TaskEntity> findByTitle(String title);

    /**
     * Checks if a task exists by its title.
     *
     * @param title the title of the task to check.
     * @return {@code true} if a task with the given title exists, otherwise {@code false}.
     */
    boolean existsByTitle(String title);

    /**
     * Finds tasks by their executor.
     *
     * @param executor the user entity representing the task executor.
     * @param pageable the pagination information (page number, size, sorting).
     * @return a {@link Page} of tasks assigned to the given executor.
     */
    Page<TaskEntity> findByExecutor(UserEntity executor, Pageable pageable);

}
