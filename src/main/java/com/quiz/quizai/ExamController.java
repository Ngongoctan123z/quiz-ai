package com.quiz.quizai;

import com.quiz.quizai.entity.*;
import com.quiz.quizai.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ExamController {

    @Autowired ExamRepository examRepo;
    @Autowired SubmissionRepository submissionRepo;

    // 1. API: LƯU ĐỀ THI
    @PostMapping("/api/tao-de-thi")
    @ResponseBody
    public Map<String, String> luuDeThi(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                response.put("status", "error");
                response.put("message", "Bạn phải đăng nhập mới được lưu đề!");
                return response;
            }

            // Tạo Đề thi
            Exam exam = new Exam();
            exam.title = (String) payload.get("title");
            exam.examCode = "DE-" + (System.currentTimeMillis() % 1000000);
            exam.isPublished = true;
            exam.description = "Người tạo: " + currentUser.fullName;

            // Xử lý câu hỏi
            List<Map<String, String>> rawQuestions = (List<Map<String, String>>) payload.get("questions");
            List<Question> listQuestions = new ArrayList<>();
            for (Map<String, String> qMap : rawQuestions) {
                Question q = new Question();
                q.content = qMap.get("content");
                q.optionA = qMap.get("optionA");
                q.optionB = qMap.get("optionB");
                q.optionC = qMap.get("optionC");
                q.optionD = qMap.get("optionD");
                q.correctAnswer = qMap.get("correctAnswer");
                q.exam = exam;
                listQuestions.add(q);
            }
            exam.questions = listQuestions;
            examRepo.save(exam);

            response.put("status", "success");
            response.put("message", "Lưu thành công!");
            response.put("examCode", exam.examCode);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Lỗi: " + e.getMessage());
            return response;
        }
    }

    // 2. VIEW: TRANG LÀM BÀI
    @GetMapping("/vao-thi")
    public String trangLamBai(@RequestParam String id, Model model) {
        Exam exam = examRepo.findByExamCode(id);
        if (exam == null) return "error";
        model.addAttribute("exam", exam);
        return "thi/index";
    }

    // 3. API: NỘP BÀI
    @PostMapping("/nop-bai")
    @ResponseBody
    public String xuLyNopBai(@RequestBody Map<String, Object> payload, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) return "❌ Bạn phải đăng nhập!";

            String examCode = (String) payload.get("examCode");
            Exam exam = examRepo.findByExamCode(examCode);
            if (exam == null) return "Lỗi đề!";

            Map<String, String> dapAnHS = (Map<String, String>) payload.get("dapAn");
            int soCauDung = 0;
            for (Question q : exam.questions) {
                String userChon = dapAnHS.get("q_" + q.id);
                if (userChon != null && userChon.equals(q.correctAnswer)) soCauDung++;
            }
            double diem = Math.round(((double) soCauDung / exam.questions.size() * 10) * 100.0) / 100.0;

            Submission sub = new Submission();
            sub.score = diem;
            sub.exam = exam;
            sub.user = currentUser;
            sub.studentName = currentUser.fullName;
            submissionRepo.save(sub);

            return "Nộp thành công! Điểm: " + diem;
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    // --- ĐÃ XÓA HÀM XEM CHI TIẾT Ở ĐÂY ĐỂ TRÁNH TRÙNG VỚI WEBCONTROLLER ---

    // 4. API GIẢ LẬP AI
    @PostMapping("/api/generate-ai")
    @ResponseBody
    public List<Map<String, String>> generateAI(@RequestBody Map<String, String> payload) {
        String topic = payload.get("topic").toLowerCase();
        int count = Integer.parseInt(payload.getOrDefault("count", "5"));
        List<Map<String, String>> aiQuestions = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Map<String, String> q = new HashMap<>();
            if(topic.contains("toán")) {
                int a = (int)(Math.random()*50); int b = (int)(Math.random()*50);
                q.put("content", "Tính: " + a + " + " + b + " = ?");
                q.put("optionA", String.valueOf(a+b)); q.put("optionB", String.valueOf(a+b+2));
                q.put("optionC", String.valueOf(a+b-1)); q.put("optionD", String.valueOf(a+b+5));
                q.put("correctAnswer", "A");
            } else {
                q.put("content", "Câu hỏi AI về " + topic + " số " + i);
                q.put("optionA", "Đáp án A"); q.put("optionB", "Đáp án B");
                q.put("optionC", "Đáp án C"); q.put("optionD", "Đáp án D");
                q.put("correctAnswer", "A");
            }
            aiQuestions.add(q);
        }
        return aiQuestions;
    }
}