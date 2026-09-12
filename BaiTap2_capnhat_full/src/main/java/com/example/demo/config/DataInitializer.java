package com.example.demo.config;

import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Tao san 1 tai khoan admin va vai user/category mau khi ung dung khoi dong lan dau,
 * de co the dang nhap va thao tac CRUD ngay ma khong can insert du lieu thu cong.
 *
 * Tai khoan admin mac dinh: admin@example.com / admin123
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, CategoryRepository categoryRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User("admin@example.com", encoder.encode("admin123"), "Quản trị viên", Role.ADMIN);
                userRepository.save(admin);
            }

            if (userRepository.count() < 2) {
                User u1 = new User("user1@example.com", encoder.encode("user123"), "Nguyễn Văn A", Role.USER);
                u1.setPhone("0900000001");
                userRepository.save(u1);

                User u2 = new User("user2@example.com", encoder.encode("user123"), "Trần Thị B", Role.USER);
                u2.setPhone("0900000002");
                userRepository.save(u2);
            }

            if (categoryRepository.count() == 0) {
                String[][] samples = {
                        {"Điện thoại", "Điện thoại di động các loại"},
                        {"Laptop", "Máy tính xách tay"},
                        {"Phụ kiện", "Phụ kiện điện tử"},
                        {"Đồng hồ", "Đồng hồ thông minh & thời trang"}
                };
                for (String[] s : samples) {
                    Category c = new Category();
                    c.setName(s[0]);
                    c.setDescription(s[1]);
                    categoryRepository.save(c);
                }
            }
        };
    }
}
