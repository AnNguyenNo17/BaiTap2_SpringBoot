package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. Trang chủ: Hiển thị 10 sản phẩm mới nhất
    @GetMapping("/")
    public String homePage(Model model) {
        try {
            List<Product> topProducts = productRepository.findTop10ByOrderByIdDesc();
            if (topProducts == null) {
                topProducts = new ArrayList<>();
            }
            model.addAttribute("topProducts", topProducts);
        } catch (Exception e) {
            model.addAttribute("topProducts", new ArrayList<>());
        }
        return "index";
    }

    // 2. Phân trang 6 sản phẩm / trang tại đường dẫn /product
    @GetMapping("/product")
    public String listProductsPaged(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());
        Page<Product> productPage = productRepository.findAll(pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "product-list";
    }

    // 3. Xem chi tiết 01 sản phẩm
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "product-detail";
    }
}