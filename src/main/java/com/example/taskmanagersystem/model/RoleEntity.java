package com.example.taskmanagersystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity class representing a user role in the system.
 * This class maps to the `roles` table in the database
 */
@Table(name = "roles")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    /// The unique identifier for the role.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /// The name of the role.
    @Column(unique = true, nullable = false, length = 30)
    private String name;

}
