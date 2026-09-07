package com.example.demo.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cau hinh SiteMesh 3: bat toan bo cac trang HTML tra ve boi ung dung phai duoc
 * "trang tri" (decorate) boi khung giao dien Bootstrap dung chung, thay vi phai
 * lap lai <html><head>...</head><nav>...</nav> tren tung trang Thymeleaf.
 *
 * Co che hoat dong:
 *  1) SiteMesh bat (intercept) response HTML cua request goc (vi du: GET /login).
 *  2) SiteMesh dieu huong noi bo (server-side include) toi "/decorator"
 *     (duoc xu ly boi DecoratorController -> templates/decorators/main.html).
 *  3) SiteMesh trich xuat <title>/<head>/<body> cua trang goc va "ghep" vao
 *     cac the <sitemesh:write .../> trong decorator, roi tra ve response cuoi cung.
 */
@Configuration
public class SiteMeshConfig {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> registration = new FilterRegistrationBean<>();

        ConfigurableSiteMeshFilter filter = new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder
                        // Ap dung decorator Bootstrap cho TAT CA cac trang
                        .addDecoratorPath("/*", "/decorator")
                        // Khong decorate chinh trang decorator (tranh de quy)
                        .addExcludedPath("/decorator")
                        // Khong decorate tai nguyen tinh / anh upload
                        .addExcludedPath("/css/*")
                        .addExcludedPath("/js/*")
                        .addExcludedPath("/images/*")
                        .addExcludedPath("/webjars/*")
                        .addExcludedPath("/uploads/*");
            }
        };

        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("sitemesh");
        // Dam bao SiteMesh chay TRUOC cac filter khac (neu co) de bat duoc response HTML
        registration.setOrder(1);
        return registration;
    }
}
