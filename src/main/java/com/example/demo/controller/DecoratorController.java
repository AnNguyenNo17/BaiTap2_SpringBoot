package com.example.demo.controller;

import com.example.demo.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Trang decorator (khung giao dien chung - Bootstrap) cho SiteMesh 3.
 * Sitemesh se "include" request toi endpoint nay de lay phan HTML khung (header/footer/navbar),
 * sau do gop (merge) voi <title>/<body> cua trang noi dung that su.
 *
 * KHONG duoc goi truc tiep boi nguoi dung; SiteMeshConfig se dieu huong noi bo toi day.
 */
@Controller
public class DecoratorController {

    @GetMapping("/decorator")
    public String decorator(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("currentUser", user);
        return "decorators/main";
    }
}
