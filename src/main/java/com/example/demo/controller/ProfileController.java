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
                                 @RequestParam(value = "removeAvatar", required = false) String removeAvatar,
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

        // Xoa anh dai dien theo yeu cau cua user (tick checkbox "Xoa anh dai dien")
        if ("on".equals(removeAvatar) || "true".equals(removeAvatar)) {
            deleteAvatarFileIfExists(user.getAvatarPath());
            user.setAvatarPath(null);
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String contentType = avatarFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                model.addAttribute("error", "File không hợp lệ. Vui lòng chọn một file ảnh (jpg, png, gif...).");
                model.addAttribute("avatarPath", user.getAvatarPath());
                model.addAttribute("email", user.getEmail());
                return "profile";
            }
            try {
                // Xoa anh cu (neu co) truoc khi luu anh moi, tranh rac file tren server
                deleteAvatarFileIfExists(user.getAvatarPath());
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

    // Xoa file anh dai dien cu tren o dia (neu ton tai), tranh tich luy file rac
    private void deleteAvatarFileIfExists(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        try {
            Path path = Paths.get(relativePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Không thể xóa file ảnh cũ: " + relativePath + " - " + e.getMessage());
        }
    }
}
