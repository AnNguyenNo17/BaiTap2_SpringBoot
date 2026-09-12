<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>${empty user.id ? 'Thêm người dùng' : 'Sửa người dùng'}</title>

<h4 class="mb-3">
    <i class="bi bi-people"></i> ${empty user.id ? 'Thêm người dùng mới' : 'Cập nhật người dùng'}
</h4>

<c:if test="${not empty error}">
    <div class="alert alert-danger py-2">${error}</div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/admin/users/save">
            <c:if test="${not empty user.id}">
                <input type="hidden" name="id" value="${user.id}">
            </c:if>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Email <span class="text-danger">*</span></label>
                    <input type="email" name="email" class="form-control" value="${user.email}"
                           required maxlength="150" ${not empty user.id ? 'readonly' : ''}>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Họ tên</label>
                    <input type="text" name="fullname" class="form-control" value="${user.fullname}" maxlength="150">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Số điện thoại</label>
                    <input type="text" name="phone" class="form-control" value="${user.phone}" maxlength="20">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Vai trò</label>
                    <select name="role" class="form-select">
                        <c:forEach var="r" items="${roles}">
                            <option value="${r}" ${user.role == r ? 'selected' : ''}>${r}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">
                    Mật khẩu
                    <c:if test="${not empty user.id}">
                        <span class="text-muted small">(để trống nếu không đổi)</span>
                    </c:if>
                </label>
                <input type="password" name="rawPassword" class="form-control"
                       placeholder="${empty user.id ? 'Bắt buộc nhập' : 'Không đổi'}">
            </div>

            <div class="form-check mb-3">
                <input type="hidden" name="enabled" value="false">
                <input class="form-check-input" type="checkbox" name="enabled" value="true"
                       id="enabled" ${user.enabled ? 'checked' : ''}>
                <label class="form-check-label" for="enabled">Hoạt động (cho phép đăng nhập)</label>
            </div>

            <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> Lưu</button>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-secondary">Hủy</a>
        </form>
    </div>
</div>
