package com.example.taskmanagersystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * DTO for creating a comment.
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO for creating a comment")
public class CommentCreateDto {

    /**
     * Title of the task.
     */
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Title of the task", example = "task1")
    private String taskTitle;

    /**
     * Text of the comment.
     */
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Text of the comment", example = "text")
    private String text;
}
