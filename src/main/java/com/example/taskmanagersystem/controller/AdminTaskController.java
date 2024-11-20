package com.example.taskmanagersystem.controller;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.FindTasksDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.model.Priority;
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


/**
 * Controller for admin task management.
 */
@RestController
@RequestMapping("/api/task/admin")
@Validated
@Tag(name = "Admin Task Controller", description = "Provides task management methods for the admin")
public class AdminTaskController {

    private final TasksServiceImpl taskService;

    SecurityContextHolderStrategy strategy =
            SecurityContextHolder.getContextHolderStrategy();


    /**
     * Constructs an {@code AdminTaskController} with the given task service.
     *
     * @param taskService the service for managing tasks
     */
    public AdminTaskController(TasksServiceImpl taskService) {
        this.taskService = taskService;
    }


    /**
     * Creates a new task.
     * Allows you to create a task for a user with the admin role
     *
     * @param createTaskDto the DTO containing task creation details
     * @return a {@link ResponseEntity} with a success message and HTTP status 201
     */
    @Operation(
            summary = "Create a task",
            description = "Allows you to create a task for a user with the admin role",
            responses = {
                    @ApiResponse(
                            description = "Task created!",
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
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@Valid @RequestBody CreateTaskDto createTaskDto){
        String email = strategy.getContext().getAuthentication().getName();
        taskService.createTask(createTaskDto, email);
        return new ResponseEntity<>("Task created!", HttpStatus.CREATED);
    }


    /**
     * Show all task
     * Shows a paginated list of all existing tasks for a user with the administrator role
     *
     * @param offset the starting index for pagination, default is 0
     * @param limit  the maximum number of tasks to return, default is 20
     * @return a list of {@link TaskResponseDto} representing all tasks
     */
    @Operation(
            summary = "Show all task",
            description = "Shows a paginated list of all existing tasks for a user with the administrator role",
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
    @GetMapping("/showAll")
    public List<TaskResponseDto> showAllTasks(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                              @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit){
        return taskService.showAllTasks(limit, offset);
    }


    /**
     * Deletes a task by title.
     * Deletes the task based on the passed title for a user with the administrator role
     *
     * @param title the title of the task to delete
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Delete a task",
            description = "Deletes the task based on the passed title for a user with the administrator role",
            responses = {
                    @ApiResponse(
                            description = "Task deleted!",
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
    @DeleteMapping("/delete/{title}")
    public ResponseEntity<String> deleteTask(@PathVariable @Parameter(description = "Title of the task to be deleted", required = true)
                                                    @NotBlank @Size(max = 255) String title){
        taskService.deleteTask(title);
        return new ResponseEntity<>("Task deleted!", HttpStatus.OK);
    }

    /**
     * Changes the priority of a task.
     * Allows you to change the priority of a task for a user with the administrator role
     *
     * @param title      the title of the task
     * @param newPriority the new priority to set, validated against the {@link Priority} enum
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Change priority",
            description = "Allows you to change the priority of a task for a user with the administrator role",
            responses = {
                    @ApiResponse(
                            description = "Priority has been changed!",
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
    @PatchMapping("/edit/{title}/priority")
    public ResponseEntity<String> editPriority(@PathVariable @Parameter(description = "Title of the task to be changed", required = true)
                                                        @NotBlank @Size(max = 255) String title,
                                               @RequestParam @Parameter(description = "New task priority {LOW, MEDIUM, HIGH}", required = true)
                                                        @CheckEnum(enumClass = Priority.class) String newPriority){
        taskService.editPriority(title, Priority.valueOf(newPriority));
        return new ResponseEntity<>("Priority has been changed!", HttpStatus.OK);
    }

    /**
     * Changes the executor of a task.
     * Allows you to change the task executor for a user with the administrator role
     *
     * @param title       the title of the task
     * @param newExecutor the username of the new executor
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Change executor",
            description = "Allows you to change the task executor for a user with the administrator role",
            responses = {
                    @ApiResponse(
                            description = "Priority has been changed!",
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
    @PatchMapping("/edit/{title}/executor")
    public ResponseEntity<String> editExecutor(@PathVariable @Parameter(description = "Title of the task to be changed", required = true)
                                                        @NotBlank @Size(max = 255) String title,
                                               @RequestParam @Parameter(description = "Username of the new executor", required = true)
                                                        @NotBlank  @Size(max = 30) String newExecutor) {
        taskService.editExecutor(title, newExecutor);
        return new ResponseEntity<>("Executor has been changed!", HttpStatus.OK);
    }

    /**
     * Retrieves a task by its title.
     * Allows you to find a task by title for a user with the administrator role
     *
     * @param title the title of the task
     * @return the {@link TaskResponseDto} representing the task
     */
    @Operation(
            summary = "Find by title",
            description = "Allows you to find a task by title for a user with the administrator role",
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
    @GetMapping("/show/byTitle")
    public TaskResponseDto showTaskByTitle(@RequestParam @Parameter(description = "Title of the task you are looking for", required = true)
                                                    @NotBlank @Size(max = 255) String title){
        return taskService.showTaskByTitle(title);
    }

    /**
     * Retrieves tasks by executor username with pagination.
     * Shows a paginated list of tasks for a given executor for a user with the administrator role
     *
     * @param offset   the starting index for pagination, default is 0
     * @param limit    the maximum number of tasks to return, default is 20
     * @param executor the username of the executor
     * @return a list of {@link TaskResponseDto} representing the tasks
     */
    @Operation(
            summary = "Find by executor",
            description = "Shows a paginated list of tasks for a given executor for a user with the administrator role",
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
    @GetMapping("/show/byExecutor")
    public List<TaskResponseDto> showTasksByExecutor(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                                     @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit,
                                                     @RequestParam  @Parameter(description = "Username of the executor", required = true)
                                                            @NotBlank @Size(max = 30) String executor){
        return taskService.showTaskByExecutorUsername(executor, limit, offset);
    }


    /**
     * Search by parameters
     * Allows to find tasks using a flexible list of parameters
     *
     * @param offset the starting index for pagination, default is 0
     * @param limit  the maximum number of tasks to return, default is 20
     * @param findTasksDto the DTO containing information about the desired parameters
     * @return a list of {@link TaskResponseDto} representing all tasks
     */
    @Operation(
            summary = "Search by parameters",
            description = "Allows to find tasks using a flexible list of parameters",
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
    @PostMapping("/find")
    public List<TaskResponseDto> findTasks(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                            @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit,
                                            @Valid @RequestBody FindTasksDto findTasksDto){

        taskService.showAllTasksBySpecification(findTasksDto, limit, offset);
        return taskService.showAllTasksBySpecification(findTasksDto, limit, offset);
    }



}
