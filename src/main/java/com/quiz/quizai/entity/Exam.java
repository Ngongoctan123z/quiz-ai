package com.quiz.quizai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String examCode; // Mã đề (VD: DE-12345)

    public String title;
    public String description;
    public Boolean isPublished = false;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    public List<Question> questions; // Một đề chứa nhiều câu hỏi

    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();
}