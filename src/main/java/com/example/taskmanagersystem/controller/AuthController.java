package com.example.taskmanagersystem.controller;

import com.example.taskmanagersystem.dto.LoginDto;
import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.exceptions.ErrorInputDataException;
import com.example.taskmanagersystem.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for user authentication and registration management.
 * It integrates with the {@link AuthServiceImpl} to perform the necessary operations.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Provides an opportunity to register and authorize a user")
public class AuthController {
    
    private final AuthServiceImpl authService;


    /**
     * Constructs an instance of {@code AuthController}.
     *
     * @param authService the authentication service used for login and registration
     */
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }


    /**
     * This method authenticates a user based on the provided credentials and returns a JWT token.
     *
     * @param loginDto the DTO containing the user's login credentials
     * @return a ResponseEntity containing the JWT token and HTTP status 200 if authentication is successful
     */
    @Operation(
            summary = "Authorization",
            description = "Allows to authorize and receive a jwt token",
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
    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        String jwtToken = authService.login(loginDto);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    /**
     * Registers a new user in the system.
     * This endpoint accepts a {@link RegisterDto} with user details,
     * validates them, and registers the user in the system.
     *
     * @param registerDto the DTO containing the user's registration details
     * @return a {@link ResponseEntity} with a success message and HTTP status 200 if registration is successful
     * @throws ErrorInputDataException if the user already exists or other validation errors occur
     */
    @Operation(
            summary = "Registration",
            description = "Allows to register a user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) throws ErrorInputDataException {
        authService.register(registerDto);
        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }


}
