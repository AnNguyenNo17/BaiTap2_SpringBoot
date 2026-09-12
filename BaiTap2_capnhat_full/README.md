# BTL2 - Admin CRUD (Category, User)

Spring Boot 4 + JSP/JSTL + SiteMesh 3 + Bootstrap. Hoàn thiện chức năng CRUD trong role
`admin` cho bảng **Category** và **User**, có tìm kiếm, phân trang, giao diện dùng
SiteMesh 3 decorators.

## 1. Yêu cầu môi trường

- JDK 21
- Maven 3.9+
- (Tùy chọn) MySQL 8, nếu không dùng MySQL thì mặc định chạy với H2 (không cần cài gì thêm)

## 2. Cấu trúc chính

```
src/main/java/com/example/demo/
  entity/         User, Category, Role
  repository/     UserRepository, CategoryRepository (Spring Data JPA + search có phân trang)
  controller/      AuthController, ProfileController
  controller/admin/ CategoryController, UserController (CRUD + search + pagination)
  interceptor/    AdminAuthInterceptor (chặn /admin/** nếu chưa đăng nhập / không phải ADMIN)
  config/         WebConfig, DataInitializer (seed dữ liệu mẫu)

src/main/webapp/WEB-INF/
  web.xml             filter SiteMesh
  sitemesh3.xml        cấu hình mapping decorator
  decorators/          main.jsp, admin.jsp, login.jsp (layout Bootstrap)
  views/               login.jsp, profile.jsp, admin/category-*.jsp, admin/user-*.jsp
```

## 3. Chạy ứng dụng

```bash
mvn clean spring-boot:run
```

Mặc định dùng H2 (file `./data/btl2db`), tự tạo bảng và seed dữ liệu mẫu khi khởi động
lần đầu (xem `DataInitializer`).

Truy cập: http://localhost:8080/login

Tài khoản mẫu:
- Admin: `admin@example.com` / `admin123`
- User: `user1@example.com` / `user123`

### Dùng MySQL thay vì H2

Mở `src/main/resources/application.properties`, comment phần cấu hình H2 và bỏ comment
phần MySQL, sau đó đảm bảo MySQL đang chạy ở `localhost:3306` (DB sẽ tự tạo nếu chưa có
nhờ `createDatabaseIfNotExist=true`).

## 4. Chức năng đã hoàn thiện

- Đăng nhập/đăng xuất bằng session, mật khẩu bằm bằng BCrypt.
- `AdminAuthInterceptor` bảo vệ toàn bộ `/admin/**`, chỉ role `ADMIN` mới truy cập được.
- **Category**: danh sách + tìm kiếm theo tên + phân trang, thêm, sửa, xóa.
- **User**: danh sách + tìm kiếm theo email/họ tên + phân trang, thêm, sửa (đổi mật khẩu
  tùy chọn), xóa, chọn vai trò (ADMIN/USER), bật/tắt trạng thái hoạt động.
- Trang `/profile`: user tự cập nhật họ tên, số điện thoại, ảnh đại diện (upload).
- Giao diện dùng SiteMesh 3: layout chung (`main.jsp`), layout khu quản trị có sidebar
  (`admin.jsp`), layout trang đăng nhập (`login.jsp`) — tách biệt hoàn toàn với nội dung JSP.

## 5. Đưa dự án lên GitHub

```bash
git init
git add .
git commit -m "BTL2: Hoan thien CRUD Category/User (admin) - Spring Boot 4 + JSP/JSTL + SiteMesh 3"
git branch -M main
git remote add origin <URL_REPO_GITHUB_CUA_BAN>
git push -u origin main
```

Sau đó nộp link repository (ví dụ `https://github.com/<username>/<repo>`) lên UTEXLMS.

## 6. Ghi chú kỹ thuật

- SiteMesh 3 dùng bản `3.3.0-RC1` (nhánh tương thích Jakarta EE 11 / Tomcat 11 / Spring
  Boot 4, hiện vẫn ở giai đoạn release-candidate tại thời điểm viết README này). Nếu môi
  trường của bạn gặp lỗi resolve dependency với bản RC, có thể hạ về Spring Boot `3.3.x`
  và SiteMesh `3.2.1` (bản ổn định cho Jakarta EE 10) — chỉ cần đổi version trong `pom.xml`,
  toàn bộ code Java/JSP không cần thay đổi.
- Dự án đóng gói dạng `war` để có thể deploy lên Tomcat ngoài, nhưng vẫn chạy trực tiếp
  bằng `mvn spring-boot:run` như một ứng dụng thông thường.
