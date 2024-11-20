package com.example.taskmanagersystem.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class implements the {@link AuthenticationEntryPoint} interface to handle
 * authentication errors by sending an HTTP 401 Unauthorized response when authentication fails.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles authentication errors by sending an HTTP 401 Unauthorized response.
     *
     * @param request the {@link HttpServletRequest} object that contains the request from the client
     * @param response the {@link HttpServletResponse} object that will be used to send the error response
     * @param authException the {@link AuthenticationException} that indicates the authentication error
     * @throws IOException if an input or output error occurs while handling the HTTP response
     * @throws ServletException if the request cannot be handled
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
