package com.example.taskmanagersystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for searching by parameters.
 */
@Data
@Schema(description = "DTO for searching by parameters")
public class FindTasksDto {

    /**
     * Status of the task {PENDING, IN_PROGRESS, COMPLETED}.
     */
    @Schema(description = "Status of the task {PENDING, IN_PROGRESS, COMPLETED}")
    private String status;

    /**
     * Excluded status of the task {PENDING, IN_PROGRESS, COMPLETED}.
     */
    @Schema(description = "Excluded status of the task {PENDING, IN_PROGRESS, COMPLETED}")
    private String nonStatus;

    /**
     * Priority of the task {LOW, MEDIUM, HIGH}.
     */
    @Schema(description = "Priority of the task {LOW, MEDIUM, HIGH}")
    private String priority;

    /**
     * Excluded priority of the task {LOW, MEDIUM, HIGH}.
     */
    @Schema(description = " Excluded priority of the task {LOW, MEDIUM, HIGH}")
    private String nonPriority;

    /**
     * Username of the author.
     */
    @Schema(description = "Username of the author")
    private String author;

    /**
     * Username of the executor.
     */
    @Schema(description = "Username of the executor")
    private String executor;

    /**
     * The number of comments is less than.
     */
    @Schema(description = "The number of comments is less than")
    private Integer countCommentsLess;

    /**
     * The number of comments is more than.
     */
    @Schema(description = "The number of comments is more than")
    private Integer countCommentsGreater;

    /**
     * The number of comments is equal to.
     */
    @Schema(description = "The number of comments is equal to")
    private Integer countCommentsEqual;

}
