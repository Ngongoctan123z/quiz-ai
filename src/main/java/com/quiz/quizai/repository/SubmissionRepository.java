package com.quiz.quizai.repository;

import com.quiz.quizai.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // 1. Tìm bài thi của MỘT HỌC SINH (Để học sinh xem lịch sử của mình)
    List<Submission> findByUserIdOrderBySubmittedAtDesc(Long userId);

    // 2. Tìm tất cả bài thi của MỘT ĐỀ THI (Để giáo viên xem ai đã nộp)
    // 👉 ĐÂY LÀ DÒNG BẠN ĐANG THIẾU
    List<Submission> findByExamIdOrderBySubmittedAtDesc(Long examId);

}