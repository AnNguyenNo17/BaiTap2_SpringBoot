package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirect", required = false) String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(value = "redirect", required = false) String redirect,
                           HttpSession session,
                           Model model) {

        Optional<User> found = userRepository.findByEmail(email);
        if (found.isEmpty() || !encoder.matches(password, found.get().getPassword())) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
            model.addAttribute("redirect", redirect);
            return "login";
        }

        User user = found.get();
        if (!user.isEnabled()) {
            model.addAttribute("error", "Tài khoản đã bị vô hiệu hóa.");
            return "login";
        }

        session.setAttribute("user", user);

        if (redirect != null && !redirect.isBlank()) {
            return "redirect:" + redirect;
        }
        return user.getRole() == Role.ADMIN ? "redirect:/admin/categories" : "redirect:/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
