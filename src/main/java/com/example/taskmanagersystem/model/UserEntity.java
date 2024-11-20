package com.example.taskmanagersystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Entity class representing a user in the system.
 * This class maps to the `users` table in the database
 */
@Table(name = "users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /// The unique identifier for the user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// The unique email address of the user.
    @Column(unique = true, nullable = false, length = 30)
    private String email;

    /// The unique username of the user.
    @Column(unique = true, nullable = false, length = 30)
    private String username;

    /// The encrypted password of the user.
    @Column(nullable = false)
    private String password;


    /**
     * The roles assigned to the user.
     * <p>
     * This field establishes a many-to-many relationship with the {@link RoleEntity} class
     * </p>
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles = new ArrayList<>();

}
