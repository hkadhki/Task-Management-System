package com.example.taskmanagersystem.dto;

import com.example.taskmanagersystem.model.Priority;
import com.example.taskmanagersystem.model.Status;
import com.example.taskmanagersystem.validator.CheckEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * DTO for create task.
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO for create task")
public class CreateTaskDto {

    /**
     * Title of the task.
     */
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Title of the task", example = "task1")
    private String title;

    /**
     * Description of the task.
     */
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Description of the task", example = "Description")
    private String description;

    /**
     * Status of the task {PENDING, IN_PROGRESS, COMPLETED}.
     */
    @CheckEnum(enumClass = Status.class)
    @Schema(description = "Status of the task {PENDING, IN_PROGRESS, COMPLETED}", example = "PENDING")
    private String status;

    /**
     * Priority of the task {LOW, MEDIUM, HIGH}.
     */
    @CheckEnum(enumClass = Priority.class)
    @Schema(description = "Priority of the task {LOW, MEDIUM, HIGH}", example = "MEDIUM")
    private String priority;

    /**
     * Username of the executor.
     */
    @NotBlank
    @Size(max = 30)
    @Schema(description = "Username of the executor", example = "User2")
    private String executor;
}
