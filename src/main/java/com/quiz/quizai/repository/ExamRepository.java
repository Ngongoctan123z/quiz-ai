package com.quiz.quizai.repository;

import com.quiz.quizai.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findByExamCode(String examCode); // Hàm tìm đề thi bằng mã code
}