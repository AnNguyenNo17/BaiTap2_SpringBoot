package com.example.demo.controller;

import com.example.demo.dto.ProfileForm;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Chuc nang Profile cua User: cap nhat fullname, phone, anh dai dien (upload multipart),
 * su dung JPA (UserRepository) va giao dien duoc SiteMesh decorate tu dong.
 */
@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // Lay user dang dang nhap tu session (dong bo voi co che login hien tai cua AuthController)
    private User getCurrentUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return null;
        }
        // Doc lai tu DB de luon co du lieu moi nhat
        Optional<User> fresh = userRepository.findById(sessionUser.getId());
        return fresh.orElse(sessionUser);
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        ProfileForm form = new ProfileForm();
        form.setFullname(user.getFullname());
        form.setPhone(user.getPhone());

        model.addAttribute("profileForm", form);
        model.addAttribute("avatarPath", user.getAvatarPath());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileForm form,
                                 BindingResult result,
                                 @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                 HttpSession session,
                                 Model model) {

        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("avatarPath", user.getAvatarPath());
            model.addAttribute("email", user.getEmail());
            return "profile";
        }

        user.setFullname(form.getFullname());
        user.setPhone(form.getPhone());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String savedRelativePath = saveAvatarFile(avatarFile, user.getId());
                user.setAvatarPath(savedRelativePath);
            } catch (IOException e) {
                model.addAttribute("error", "Tải ảnh đại diện thất bại: " + e.getMessage());
                model.addAttribute("avatarPath", user.getAvatarPath());
                model.addAttribute("email", user.getEmail());
                return "profile";
            }
        }

        userRepository.save(user);
        // Cap nhat lai thong tin trong session cho dong bo
        session.setAttribute("user", user);

        model.addAttribute("message", "Cập nhật hồ sơ thành công!");
        model.addAttribute("avatarPath", user.getAvatarPath());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    private String saveAvatarFile(MultipartFile file, Long userId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "avatar";
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalName.substring(dotIndex);
        }

        String filename = "user_" + userId + "_" + System.currentTimeMillis() + extension;
        Path target = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return uploadDir + "/" + filename;
    }
}
