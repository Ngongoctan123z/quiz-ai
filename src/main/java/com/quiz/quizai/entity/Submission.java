package com.quiz.quizai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String studentName; // Có thể giữ hoặc bỏ (vì đã có User rồi)
    public String studentCode;
    public Double score;
    public String details;

    @Column(name = "submitted_at")
    public LocalDateTime submittedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "exam_id")
    public Exam exam;

    // --- THÊM ĐOẠN NÀY ĐỂ BIẾT BÀI CỦA AI ---
    @ManyToOne
    @JoinColumn(name = "user_id") // Tạo cột user_id trong database
    public User user;
}