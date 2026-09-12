package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tim kiem theo ten danh muc (khong phan biet hoa thuong), co phan trang
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
