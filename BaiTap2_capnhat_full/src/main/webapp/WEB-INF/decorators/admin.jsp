<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> | Trang quản trị</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/app.css">
    <sitemesh:write property="head"/>
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/categories">
            <i class="bi bi-speedometer2"></i> BTL2 Admin
        </a>
        <div class="d-flex ms-auto">
            <span class="navbar-text text-white me-3">
                <i class="bi bi-person-circle"></i> ${sessionScope.user.fullname}
                <span class="badge bg-danger ms-1">ADMIN</span>
            </span>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </div>
    </div>
</nav>

<div class="container-fluid px-4">
    <div class="row">
        <aside class="col-12 col-md-3 col-lg-2 bg-white border-end min-vh-100 py-4">
            <ul class="nav nav-pills flex-column gap-1">
                <li class="nav-item">
                    <a class="nav-link ${activeMenu == 'categories' ? 'active' : 'text-dark'}"
                       href="${pageContext.request.contextPath}/admin/categories">
                        <i class="bi bi-tags"></i> Danh mục (Category)
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeMenu == 'users' ? 'active' : 'text-dark'}"
                       href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-people"></i> Người dùng (User)
                    </a>
                </li>
            </ul>
        </aside>

        <main class="col-12 col-md-9 col-lg-10 py-4">
            <sitemesh:write property="body"/>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
