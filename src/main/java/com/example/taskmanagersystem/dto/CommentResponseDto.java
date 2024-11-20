package com.example.taskmanagersystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;


/**
 * DTO for response comments.
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO for response comments")
public class CommentResponseDto {

    /**
     * Username of the author.
     */
    @Schema(description = "Username of the author")
    private String author;

    /**
     * Date of creation.
     */
    @Schema(description = "Date of creation")
    private Date date;

    /**
     * Text of the comment.
     */
    @Schema(description = "Text of the comment")
    private String text;
}
