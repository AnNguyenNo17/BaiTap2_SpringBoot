<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> | BTL2 Admin CRUD</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/app.css">
    <sitemesh:write property="head"/>
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">BTL2 - Admin CRUD</a>
        <div class="d-flex ms-auto">
            <c:if test="${not empty sessionScope.user}">
                <a class="btn btn-outline-light btn-sm me-2" href="${pageContext.request.contextPath}/profile">
                    <i class="bi bi-person-circle"></i> ${sessionScope.user.fullname}
                </a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container my-4">
    <sitemesh:write property="body"/>
</div>

<footer class="text-center text-muted py-4 small">
    Bài tập lớn 2 &mdash; Spring Boot 4 + JSP/JSTL + SiteMesh 3 + Bootstrap
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
