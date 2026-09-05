package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // --- 1. ĐĂNG KÝ ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }

        String otp = emailService.generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);

        // Bọc try-catch gửi mail để không bị crash 500 nếu Gmail chưa cấu hình xong
        try {
            emailService.sendEmail(user.getEmail(), "Mã OTP kích hoạt tài khoản", "Mã OTP của bạn: " + otp);
        } catch (Exception e) {
            System.err.println("Không gửi được mail qua SMTP. Lấy OTP trong DB: " + otp);
        }

        return "redirect:/verify-otp?email=" + user.getEmail();
    }

    // --- 2. XÁC NHẬN OTP ---
    @GetMapping("/verify-otp")
    public String showVerifyOtp(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (otp.equals(user.getOtp()) && user.getOtpGeneratedTime() != null 
                    && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 180) {
                user.setActive(true);
                user.setOtp(null);
                userRepository.save(user);
                return "redirect:/login?success=AccountActivated";
            }
        }
        model.addAttribute("error", "Mã OTP sai hoặc đã hết hạn!");
        model.addAttribute("email", email);
        return "verify-otp";
    }

    // --- 3. ĐĂNG NHẬP ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isActive()) {
                model.addAttribute("error", "Tài khoản chưa được kích hoạt OTP!");
                return "login";
            }
            if (user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/";
            }
        }
        model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        return "login";
    }

    // --- 4. QUÊN MẬT KHẨU ---
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = emailService.generateOtp();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);

            try {
                emailService.sendEmail(email, "OTP Quên mật khẩu", "Mã OTP của bạn: " + otp);
            } catch (Exception e) {
                System.err.println("Không gửi được mail qua SMTP. Lấy OTP trong DB: " + otp);
            }

            return "redirect:/reset-password?email=" + email;
        }
        model.addAttribute("error", "Email không tồn tại!");
        return "forgot-password";
    }

    // --- 5. ĐẶT LẠI MẬT KHẨU ---
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email, @RequestParam("otp") String otp, @RequestParam("newPassword") String newPassword, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (otp.equals(user.getOtp()) && user.getOtpGeneratedTime() != null 
                    && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 180) {
                user.setPassword(newPassword);
                user.setOtp(null);
                userRepository.save(user);
                return "redirect:/login?success=PasswordReset";
            }
        }
        model.addAttribute("error", "Mã OTP sai hoặc đã hết hạn!");
        model.addAttribute("email", email);
        return "reset-password";
    }
}