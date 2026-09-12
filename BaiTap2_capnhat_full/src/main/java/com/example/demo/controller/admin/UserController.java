package com.example.demo.controller.admin;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final int PAGE_SIZE = 5;

    // Danh sach + tim kiem (theo email hoac ho ten) + phan trang
    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        Model model) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by("id").descending());
        Page<User> result = keyword.isBlank()
                ? userRepository.findAll(pageable)
                : userRepository.findByEmailContainingIgnoreCaseOrFullnameContainingIgnoreCase(keyword, keyword, pageable);

        model.addAttribute("userPage", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeMenu", "users");
        return "admin/user-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("activeMenu", "users");
        return "admin/user-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user id=" + id));
        // Khong hien mat khau da bam ra form
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("activeMenu", "users");
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("user") User user,
                        BindingResult result,
                        @RequestParam(value = "rawPassword", required = false) String rawPassword,
                        Model model) {

        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("activeMenu", "users");
            return "admin/user-form";
        }

        if (user.getId() == null) {
            // Tao moi: bat buoc phai co mat khau
            if (rawPassword == null || rawPassword.isBlank()) {
                model.addAttribute("error", "Vui lòng nhập mật khẩu cho tài khoản mới.");
                model.addAttribute("roles", Role.values());
                model.addAttribute("activeMenu", "users");
                return "admin/user-form";
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email đã tồn tại.");
                model.addAttribute("roles", Role.values());
                model.addAttribute("activeMenu", "users");
                return "admin/user-form";
            }
            user.setPassword(encoder.encode(rawPassword));
        } else {
            // Cap nhat: giu mat khau cu neu khong nhap mat khau moi
            User existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user"));
            if (rawPassword != null && !rawPassword.isBlank()) {
                user.setPassword(encoder.encode(rawPassword));
            } else {
                user.setPassword(existing.getPassword());
            }
            user.setAvatarPath(existing.getAvatarPath());
        }

        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
