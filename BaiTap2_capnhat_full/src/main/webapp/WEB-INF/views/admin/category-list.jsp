<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>Quản lý danh mục</title>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0"><i class="bi bi-tags"></i> Quản lý danh mục (Category)</h4>
    <a href="${pageContext.request.contextPath}/admin/categories/new" class="btn btn-primary btn-sm">
        <i class="bi bi-plus-lg"></i> Thêm danh mục
    </a>
</div>

<form method="get" action="${pageContext.request.contextPath}/admin/categories" class="row g-2 mb-3">
    <div class="col-auto flex-grow-1">
        <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên danh mục..."
               value="${keyword}">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-secondary"><i class="bi bi-search"></i> Tìm</button>
    </div>
    <c:if test="${not empty keyword}">
        <div class="col-auto">
            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-danger">Xóa lọc</a>
        </div>
    </c:if>
</form>

<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr>
                    <th style="width:60px">#</th>
                    <th>Tên danh mục</th>
                    <th>Mô tả</th>
                    <th style="width:120px">Trạng thái</th>
                    <th style="width:160px" class="text-end">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cat" items="${categoryPage.content}">
                    <tr>
                        <td>${cat.id}</td>
                        <td>${cat.name}</td>
                        <td>${cat.description}</td>
                        <td>
                            <c:choose>
                                <c:when test="${cat.enabled}">
                                    <span class="badge bg-success">Hoạt động</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Đã ẩn</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end">
                            <a href="${pageContext.request.contextPath}/admin/categories/edit/${cat.id}"
                               class="btn btn-sm btn-outline-primary"><i class="bi bi-pencil"></i></a>
                            <a href="${pageContext.request.contextPath}/admin/categories/delete/${cat.id}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirm('Xóa danh mục &quot;${cat.name}&quot;?');">
                                <i class="bi bi-trash"></i></a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty categoryPage.content}">
                    <tr>
                        <td colspan="5" class="text-center text-muted py-4">Không có danh mục nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${categoryPage.totalPages > 1}">
    <nav class="mt-3">
        <ul class="pagination justify-content-center">
            <c:forEach begin="0" end="${categoryPage.totalPages - 1}" var="i">
                <li class="page-item ${i == categoryPage.number ? 'active' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/admin/categories?page=${i}&keyword=${keyword}">
                        ${i + 1}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</c:if>
