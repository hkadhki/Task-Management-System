package com.example.taskmanagersystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity class representing a task in the system.
 * This class maps to the `tasks` table in the database
 */
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class TaskEntity {

    /// The unique identifier for the task.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// The title of the task.
    @Column(unique = true, nullable = false)
    private String title;

    /// The detailed description of the task.
    @Column(nullable = false)
    private String description;


    /**
     * The current status of the task.
     * <p>
     * This field uses the {@link Status} enumeration to represent the task's lifecycle state.
     * Examples include "OPEN", "IN_PROGRESS", or "COMPLETED".
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * The priority level of the task.
     * <p>
     * This field uses the {@link Priority} enumeration to represent the urgency of the task.
     * Examples include "LOW", "MEDIUM", or "HIGH".
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    /**
     * The user who created the task.
     * <p>
     * This field establishes a many-to-one relationship with the {@link UserEntity} class,
     * indicating the task's author.
     * </p>
     */
    @ManyToOne
    private UserEntity author;

    /**
     * The user responsible for executing the task.
     * <p>
     * This field establishes a many-to-one relationship with the {@link UserEntity} class,
     * indicating the task's executor.
     * </p>
     */
    @ManyToOne
    private UserEntity executor;

    /**
     * The list of comments associated with the task.
     * <p>
     * This field establishes a one-to-many relationship with {@link CommentEntity},
     * representing feedback or updates related to the task.
     * </p>
     */
    @OneToMany
    private List<CommentEntity> comments;
}
