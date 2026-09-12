package com.example.demo.controller.admin;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final int PAGE_SIZE = 5;

    // Danh sach + tim kiem + phan trang
    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        Model model) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by("id").descending());
        Page<Category> result = keyword.isBlank()
                ? categoryRepository.findAll(pageable)
                : categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);

        model.addAttribute("categoryPage", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeMenu", "categories");
        return "admin/category-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("activeMenu", "categories");
        return "admin/category-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục id=" + id));
        model.addAttribute("category", category);
        model.addAttribute("activeMenu", "categories");
        return "admin/category-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") Category category,
                        BindingResult result,
                        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("activeMenu", "categories");
            return "admin/category-form";
        }

        if (category.getId() != null) {
            // Giu nguyen ngay tao ban dau, chi cap nhat cac truong duoc phep sua
            categoryRepository.findById(category.getId())
                    .ifPresent(existing -> category.setCreatedAt(existing.getCreatedAt()));
        }

        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }
}
