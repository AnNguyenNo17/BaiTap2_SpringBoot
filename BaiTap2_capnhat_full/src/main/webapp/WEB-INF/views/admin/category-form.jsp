<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>${empty category.id ? 'Thêm danh mục' : 'Sửa danh mục'}</title>

<h4 class="mb-3">
    <i class="bi bi-tags"></i> ${empty category.id ? 'Thêm danh mục mới' : 'Cập nhật danh mục'}
</h4>

<div class="card shadow-sm">
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/admin/categories/save">
            <c:if test="${not empty category.id}">
                <input type="hidden" name="id" value="${category.id}">
            </c:if>

            <div class="mb-3">
                <label class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                <input type="text" name="name" class="form-control" value="${category.name}" required maxlength="150">
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" class="form-control" rows="3" maxlength="500">${category.description}</textarea>
            </div>

            <div class="form-check mb-3">
                <input type="hidden" name="enabled" value="false">
                <input class="form-check-input" type="checkbox" name="enabled" value="true"
                       id="enabled" ${category.enabled ? 'checked' : ''}>
                <label class="form-check-label" for="enabled">Hoạt động</label>
            </div>

            <button type="submit" class="btn btn-primary">
                <i class="bi bi-check-lg"></i> Lưu
            </button>
            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary">Hủy</a>
        </form>
    </div>
</div>
