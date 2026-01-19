package com.quiz.quizai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String email;
    public String password;
    public String fullName;
    public String role; // USER hoặc ADMIN
    public String authProvider; // LOCAL hoặc GOOGLE

    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();
}