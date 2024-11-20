package com.example.taskmanagersystem.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is a Spring Security filter that processes JWT tokens for authentication.
 * It extends {@link OncePerRequestFilter}.
 */
 @Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerator tokenGenerator;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtAuthenticationFilter(JwtGenerator tokenGenerator, JwtUserDetailsService jwtUserDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    /**
     * Filters the incoming HTTP request to extract and validate the JWT token. If the token is valid,
     * it sets the authentication context with the authenticated user details.
     *
     * @param request the {@link HttpServletRequest} object that contains the request from the client
     * @param response the {@link HttpServletResponse} object used to send the response to the client
     * @param filterChain the {@link FilterChain} to pass the request and response to the next filter
     * @throws ServletException if an error occurs during the filter process
     * @throws IOException if an input or output error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AuthenticationCredentialsNotFoundException {
        String token = getJWTFromRequest(request);
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getEmailFromJWT(token);

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the {@link HttpServletRequest} object containing the request from the client
     * @return the JWT token, or {@code null} if the token is not present
     */
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
