package com.example.taskmanagersystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;


/**
 * Entity class representing a comment in the system.
 * This class maps to the `comments` table in the database
 */
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class CommentEntity {

    /// The unique identifier for the comment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// The user who authored the comment
    @ManyToOne
    private UserEntity author;

    /// The date when the comment was created.
    @Column(nullable = false)
    private Date date;

    /// The textual content of the comment.
    @Column(nullable = false)
    private String text;

}
