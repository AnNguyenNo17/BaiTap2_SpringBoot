package com.example.demo.controller;

import com.example.demo.dto.ForgotPasswordForm;
import com.example.demo.dto.LoginForm;
import com.example.demo.dto.ResetPasswordForm;
import com.example.demo.dto.VerifyOtpForm;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    // --- 1. DANG KY ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        // 1) Kiem tra validate (@NotBlank, @Email, @Size... khai bao tren entity User)
        if (result.hasErrors()) {
            return "register";
        }

        // 2) Kiem tra nghiep vu: email da ton tai chua
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }

        String otp = emailService.generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);

        try {
            emailService.sendEmail(user.getEmail(), "Mã OTP kích hoạt tài khoản", "Mã OTP của bạn: " + otp);
        } catch (Exception e) {
            System.err.println("Không gửi được mail qua SMTP. Lấy OTP trong DB: " + otp);
        }

        return "redirect:/verify-otp?email=" + user.getEmail();
    }

    // --- 2. XAC NHAN OTP ---
    @GetMapping("/verify-otp")
    public String showVerifyOtp(@RequestParam("email") String email, Model model) {
        VerifyOtpForm form = new VerifyOtpForm();
        form.setEmail(email);
        model.addAttribute("otpForm", form);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@Valid @ModelAttribute("otpForm") VerifyOtpForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "verify-otp";
        }

        Optional<User> userOpt = userRepository.findByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (form.getOtp().equals(user.getOtp()) && user.getOtpGeneratedTime() != null
                    && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 180) {
                user.setActive(true);
                user.setOtp(null);
                userRepository.save(user);
                return "redirect:/login?success=AccountActivated";
            }
        }
        model.addAttribute("error", "Mã OTP sai hoặc đã hết hạn!");
        return "verify-otp";
    }

    // --- 3. DANG NHAP ---
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult result,
                             HttpSession session, Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        Optional<User> userOpt = userRepository.findByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isActive()) {
                model.addAttribute("error", "Tài khoản chưa được kích hoạt OTP!");
                return "login";
            }
            if (user.getPassword().equals(form.getPassword())) {
                session.setAttribute("user", user);
                return "redirect:/";
            }
        }
        model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        return "login";
    }

    // --- DANG XUAT ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- 4. QUEN MAT KHAU ---
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("forgotForm", new ForgotPasswordForm());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@Valid @ModelAttribute("forgotForm") ForgotPasswordForm form,
                                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "forgot-password";
        }

        Optional<User> userOpt = userRepository.findByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = emailService.generateOtp();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);

            try {
                emailService.sendEmail(form.getEmail(), "OTP Quên mật khẩu", "Mã OTP của bạn: " + otp);
            } catch (Exception e) {
                System.err.println("Không gửi được mail qua SMTP. Lấy OTP trong DB: " + otp);
            }

            return "redirect:/reset-password?email=" + form.getEmail();
        }
        model.addAttribute("error", "Email không tồn tại!");
        return "forgot-password";
    }

    // --- 5. DAT LAI MAT KHAU ---
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        ResetPasswordForm form = new ResetPasswordForm();
        form.setEmail(email);
        model.addAttribute("resetForm", form);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@Valid @ModelAttribute("resetForm") ResetPasswordForm form,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "reset-password";
        }

        Optional<User> userOpt = userRepository.findByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (form.getOtp().equals(user.getOtp()) && user.getOtpGeneratedTime() != null
                    && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 180) {
                user.setPassword(form.getNewPassword());
                user.setOtp(null);
                userRepository.save(user);
                return "redirect:/login?success=PasswordReset";
            }
        }
        model.addAttribute("error", "Mã OTP sai hoặc đã hết hạn!");
        return "reset-password";
    }
}
