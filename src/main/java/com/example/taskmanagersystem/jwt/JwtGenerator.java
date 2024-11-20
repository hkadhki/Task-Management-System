package com.example.taskmanagersystem.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

 /**
 * This class is responsible for generating, parsing, and validating JWT (JSON Web Tokens).
 * It uses the HS512 algorithm for signing the tokens.
 */
@Component
public class JwtGenerator {

     /** The signing key used to sign the JWT tokens. */
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

     /** The expiration time for the JWT tokens, in milliseconds. */
    @Value("${jwt.expiration}")
    private Long expiration;

     /**
      * Generates a JWT token for the authenticated user.
      *
      * @param authentication the authentication object containing the user's details
      * @return a JWT token as a string
      */
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

     /**
      * Extracts the email from the given JWT token.
      *
      * @param token the JWT token
      * @return the email extracted from the token
      */
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

     /**
      * Validates the given JWT token.
      *
      * @param token the JWT token to validate
      * @return true if the token is valid, otherwise false
      * @throws AuthenticationCredentialsNotFoundException if the token is expired or incorrect
      */
    public boolean validateToken(String token) throws AuthenticationCredentialsNotFoundException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect", ex.fillInStackTrace());
        }
    }
}
