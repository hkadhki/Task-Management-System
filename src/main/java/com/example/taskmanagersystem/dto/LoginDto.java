package com.example.taskmanagersystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for user authentication.
 */
@Data
@Schema(description = "DTO for user authentication (Для удобства проверки автоматически вводится email и пароль администратора)")
public class LoginDto {

    /**
     * The email address of the user.
     */
    @NotBlank
    @Email
    @Size(max = 30)
    @Schema(description = "User's email", example = "User1@gmail.com")
    private String email;

    /**
     * The password of the user.
     */
    @NotBlank
    @Size(max = 30)
    @Schema(description = "User's password", example = "USER")
    private String password;
}
