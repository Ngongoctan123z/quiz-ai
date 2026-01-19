package com.quiz.quizai.repository;

import com.quiz.quizai.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}