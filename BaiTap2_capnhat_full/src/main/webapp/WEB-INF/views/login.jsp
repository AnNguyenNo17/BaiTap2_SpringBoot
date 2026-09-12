<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>Đăng nhập</title>

<div class="card card-login shadow">
    <div class="card-body p-4">
        <h4 class="text-center mb-4">Đăng nhập hệ thống</h4>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <c:if test="${not empty redirect}">
                <input type="hidden" name="redirect" value="${redirect}">
            </c:if>
            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required autofocus
                       placeholder="admin@example.com">
            </div>
            <div class="mb-3">
                <label class="form-label">Mật khẩu</label>
                <input type="password" name="password" class="form-control" required
                       placeholder="••••••••">
            </div>
            <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
        </form>

        <hr>
        <p class="text-muted small mb-0">
            Tài khoản mẫu:<br>
            Admin: <code>admin@example.com / admin123</code><br>
            User: <code>user1@example.com / user123</code>
        </p>
    </div>
</div>
