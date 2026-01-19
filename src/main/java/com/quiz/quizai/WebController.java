package com.quiz.quizai;

import com.quiz.quizai.entity.*;
import com.quiz.quizai.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Controller
public class WebController { // <--- LỖI CỦA BẠN LÀ DO MẤT DÒNG NÀY

    @Autowired
    ExamRepository examRepo;

    @Autowired
    SubmissionRepository submissionRepo;

    // 1. TRANG CHỦ & LOGIN
    @GetMapping("/")
    public String home() { return "redirect:/login"; }

    @GetMapping("/login")
    public String trangLogin() { return "login/index"; }

    @GetMapping("/dang-xuat")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 2. CÁC TRANG CHỨC NĂNG
    @GetMapping("/de-thi")
    public String trangDeThi() { return "de-thi/index"; }

    @GetMapping("/flashcard")
    public String trangFlashcard() { return "flashcard/index"; }

    @GetMapping("/huong-dan")
    public String trangHuongDan() { return "huong-dan/index"; }

    @GetMapping("/nap-token")
    public String trangNapToken() { return "nap-token/index"; } // Nếu bạn có trang này

    // 3. TRANG LỊCH SỬ (Dành cho Giáo Viên xem danh sách đề đã tạo)
    @GetMapping("/lich-su")
    public String trangLichSu(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        // Lấy tất cả đề thi (Sắp xếp mới nhất lên đầu)
        List<Exam> myExams = examRepo.findAll();
        Collections.reverse(myExams);

        model.addAttribute("examList", myExams);
        return "lich-su/index";
    }

    // 4. TRANG CHI TIẾT ĐỀ (Xem ai đã nộp bài)
    @GetMapping("/chi-tiet-de/{examCode}")
    public String xemChiTietDe(@PathVariable String examCode, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        // Tìm đề thi theo mã
        Exam exam = examRepo.findByExamCode(examCode);
        if (exam == null) return "redirect:/lich-su";

        // Tìm danh sách bài nộp của học sinh cho đề này
        // (Lưu ý: Phải đảm bảo SubmissionRepository có hàm này, nếu chưa có thì Hibernate tự hiểu hoặc bạn thêm vào)
        List<Submission> submissions = submissionRepo.findByExamIdOrderBySubmittedAtDesc(exam.id);

        model.addAttribute("exam", exam);
        model.addAttribute("submissions", submissions);

        return "lich-su/chi-tiet";
    }
}