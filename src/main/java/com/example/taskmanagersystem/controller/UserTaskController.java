package com.example.taskmanagersystem.controller;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.model.Status;
import com.example.taskmanagersystem.service.impl.TasksServiceImpl;
import com.example.taskmanagersystem.validator.CheckEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controller for user task management.
@RestController
@RequestMapping("/api/task")
@Validated
@Tag(name = "User Task Controller", description = "Provides task management methods for the user")
public class UserTaskController {

    private final TasksServiceImpl taskService;

    SecurityContextHolderStrategy strategy =
            SecurityContextHolder.getContextHolderStrategy();


    /**
     * Constructs a {@code UserTaskController} with the given task service.
     *
     * @param taskService the service for managing tasks
     */
    public UserTaskController(TasksServiceImpl taskService) {
        this.taskService = taskService;
    }


    /**
     * Creates a comment for a task.
     * Allows you to create a comment to task for a user with the admin role or tasks executor
     *
     * @param commentCreateDto the DTO containing comment details
     * @return a {@link ResponseEntity} with a success message and HTTP status 201
     */
    @Operation(
            summary = "Create a comment to task",
            description = "Allows you to create a comment to task for a user with the admin role or tasks executor",
            responses = {
                    @ApiResponse(
                            description = "Comment created!",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        String username = strategy.getContext().getAuthentication().getName();
        taskService.addComment(commentCreateDto, username);
        return new ResponseEntity<>("Comment created!", HttpStatus.CREATED);
    }

    /**
     * Changes the status of a task.
     * Allows you to change the status of a task for a user with the administrator role or tasks executor
     *
     * @param title     the title of the task
     * @param newStatus the new status for the task, validated against the {@link Status} enum
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Change status",
            description = "Allows you to change the status of a task for a user with the administrator role or tasks executor",
            responses = {
                    @ApiResponse(
                            description = "Status has been changed!",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @PatchMapping("/edit/{title}/status")
    public ResponseEntity<String> editStatus(@PathVariable @Parameter(description = "Title of the task to be changed", required = true)
                                             @NotBlank @Size(max = 255) String title,
                                             @RequestParam @Parameter(description = "New task status {PENDING, IN_PROGRESS, COMPLETED}", required = true)
                                             @CheckEnum(enumClass = Status.class) String newStatus) {
        String email = strategy.getContext().getAuthentication().getName();
        taskService.editStatus(title, Status.valueOf(newStatus), email);
        return new ResponseEntity<>("Status has been changed!", HttpStatus.OK);
    }


    /**
     * Retrieves a paginated list of tasks assigned to the user.
     *
     * @param offset the starting index for pagination, default is 0
     * @param limit  the maximum number of tasks to return, default is 20
     * @return a list of {@link TaskResponseDto} objects representing the user's tasks
     */
    @Operation(
            summary = "Find my task",
            description = "Shows a paginated list of your tasks",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @GetMapping("/show/myTasks")
    public List<TaskResponseDto> showMyTasks(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                             @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit) {
        String email = strategy.getContext().getAuthentication().getName();
        return taskService.showTaskByExecutorEmail(email, limit, offset);
    }
}
