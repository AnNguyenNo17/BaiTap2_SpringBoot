package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Tim kiem theo email hoac ho ten (khong phan biet hoa thuong), co phan trang
    Page<User> findByEmailContainingIgnoreCaseOrFullnameContainingIgnoreCase(
            @Param("email") String email, @Param("fullname") String fullname, Pageable pageable);
}
