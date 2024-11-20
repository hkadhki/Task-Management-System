package com.example.taskmanagersystem.repository;


import com.example.taskmanagersystem.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repository interface for {@link UserEntity}.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An {@link Optional} containing the {@link UserEntity} if found, otherwise an empty {@link Optional}.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return An {@link Optional} containing the {@link UserEntity} if found, otherwise an empty {@link Optional}.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Checks if a user with the specified email exists.
     *
     * @param email The email address to check.
     * @return {@code true} if a user with the given email exists, otherwise {@code false}.
     */
    Boolean existsByEmail(String email);

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username The username address to check.
     * @return {@code true} if a user with the given username exists, otherwise {@code false}.
     */
    Boolean existsByUsername(String username);
}
