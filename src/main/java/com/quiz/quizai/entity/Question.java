package com.quiz.quizai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String content;   // Nội dung câu hỏi
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String correctAnswer; // A, B, C, D

    @ManyToOne
    @JoinColumn(name = "exam_id")
    public Exam exam; // Thuộc về đề nào
}