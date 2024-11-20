package com.example.taskmanagersystem.dto;

import com.example.taskmanagersystem.model.Priority;
import com.example.taskmanagersystem.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO for response task.
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO for response task")
public class TaskResponseDto {

    /// Title of the task.
    @Schema(description = "Title of the task")
    private String title;

    /// Description of the task.
    @Schema(description = "Description of the task", example = "task1")
    private String description;

    /// Status of the task {PENDING, IN_PROGRESS, COMPLETED}.
    @Schema(description = "Status of the task {PENDING, IN_PROGRESS, COMPLETED}")
    private Status status;

    ///Priority of the task {LOW, MEDIUM, HIGH}.
    @Schema(description = "Priority of the task {LOW, MEDIUM, HIGH}")
    private Priority priority;

    /// Username of the author.
    @Schema(description = "Username of the author")
    private String authorName;

    /// Username of the executor.
    @Schema(description = "Username of the executor")
    private String executorName;

    /// List of comment response DTO.
    @Schema(description = "List of comment response DTO")
    private List<CommentResponseDto> comments;
}
