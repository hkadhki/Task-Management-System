package com.example.taskmanagersystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user registration.
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO for user registration")
public class RegisterDto {

    /**
     * User's email.
     */
    @NotBlank
    @Email
    @Size(max = 30)
    @Schema(description = "User's email", example = "User4@gmail.com")
    private String email;


    /**
     * User's username.
     */
    @NotBlank
    @Size(max = 30)
    @Schema(description = "User's username", example = "User4")
    private String username;


    /**
     * User's password.
     */
    @NotBlank
    @Size(min = 8, max = 255)
    @Schema(description = "User's password", example = "USER")
    private String password;
}
