package com.quiz.quizai;

import com.quiz.quizai.entity.User;
import com.quiz.quizai.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepo; // Gọi kho chứa User ra

    // 1. API ĐĂNG KÝ (Hứng dữ liệu từ nút Đăng ký)
    @PostMapping("/api/dang-ky")
    @ResponseBody
    public String dangKy(@RequestBody Map<String, String> data) {
        try {
            String email = data.get("email");
            String password = data.get("password");
            String fullName = data.get("fullName");

            // Kiểm tra: Nếu email này đã có trong DB thì báo lỗi
            if (userRepo.findByEmail(email) != null) {
                return "❌ Email này đã được sử dụng rồi!";
            }

            // Tạo user mới
            User newUser = new User();
            newUser.email = email;
            newUser.password = password;
            newUser.fullName = fullName;
            newUser.role = "USER";
            newUser.authProvider = "LOCAL";

            userRepo.save(newUser); // LƯU VÀO MYSQL TẠI ĐÂY

            return "✅ Đăng ký thành công! Bạn có thể đăng nhập ngay.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Lỗi hệ thống: " + e.getMessage();
        }
    }

    // 2. API ĐĂNG NHẬP (Hứng dữ liệu từ nút Đăng nhập)
    @PostMapping("/api/dang-nhap")
    @ResponseBody
    public String dangNhap(@RequestBody Map<String, String> data, HttpSession session) {
        String email = data.get("email");
        String password = data.get("password");

        // Tìm user trong Database
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return "❌ Tài khoản không tồn tại!";
        }

        if (!user.password.equals(password)) {
            return "❌ Sai mật khẩu rồi!";
        }

        // Đăng nhập đúng -> Lưu vào phiên làm việc
        session.setAttribute("currentUser", user);
        return "OK";
    }
}