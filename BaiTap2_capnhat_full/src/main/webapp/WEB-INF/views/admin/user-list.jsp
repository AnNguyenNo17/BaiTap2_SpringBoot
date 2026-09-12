<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>Quản lý người dùng</title>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0"><i class="bi bi-people"></i> Quản lý người dùng (User)</h4>
    <a href="${pageContext.request.contextPath}/admin/users/new" class="btn btn-primary btn-sm">
        <i class="bi bi-plus-lg"></i> Thêm người dùng
    </a>
</div>

<form method="get" action="${pageContext.request.contextPath}/admin/users" class="row g-2 mb-3">
    <div class="col-auto flex-grow-1">
        <input type="text" name="keyword" class="form-control" placeholder="Tìm theo email hoặc họ tên..."
               value="${keyword}">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-secondary"><i class="bi bi-search"></i> Tìm</button>
    </div>
    <c:if test="${not empty keyword}">
        <div class="col-auto">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-danger">Xóa lọc</a>
        </div>
    </c:if>
</form>

<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr>
                    <th style="width:60px">#</th>
                    <th>Email</th>
                    <th>Họ tên</th>
                    <th>SĐT</th>
                    <th style="width:100px">Vai trò</th>
                    <th style="width:110px">Trạng thái</th>
                    <th style="width:160px" class="text-end">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${userPage.content}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.email}</td>
                        <td>${u.fullname}</td>
                        <td>${u.phone}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}">
                                    <span class="badge bg-danger">ADMIN</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-info text-dark">USER</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.enabled}">
                                    <span class="badge bg-success">Hoạt động</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Đã khóa</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end">
                            <a href="${pageContext.request.contextPath}/admin/users/edit/${u.id}"
                               class="btn btn-sm btn-outline-primary"><i class="bi bi-pencil"></i></a>
                            <a href="${pageContext.request.contextPath}/admin/users/delete/${u.id}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirm('Xóa người dùng &quot;${u.email}&quot;?');">
                                <i class="bi bi-trash"></i></a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty userPage.content}">
                    <tr>
                        <td colspan="7" class="text-center text-muted py-4">Không có người dùng nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${userPage.totalPages > 1}">
    <nav class="mt-3">
        <ul class="pagination justify-content-center">
            <c:forEach begin="0" end="${userPage.totalPages - 1}" var="i">
                <li class="page-item ${i == userPage.number ? 'active' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/admin/users?page=${i}&keyword=${keyword}">
                        ${i + 1}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</c:if>
