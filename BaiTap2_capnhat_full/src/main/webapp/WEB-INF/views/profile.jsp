<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>Hồ sơ cá nhân</title>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h4 class="mb-4">Hồ sơ cá nhân</h4>

                <c:if test="${not empty message}">
                    <div class="alert alert-success py-2">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2">${error}</div>
                </c:if>

                <div class="text-center mb-3">
                    <c:choose>
                        <c:when test="${not empty avatarPath}">
                            <img src="${pageContext.request.contextPath}${avatarPath}" class="avatar-preview" alt="avatar">
                        </c:when>
                        <c:otherwise>
                            <img src="https://ui-avatars.com/api/?name=${email}&background=0d6efd&color=fff"
                                 class="avatar-preview" alt="avatar">
                        </c:otherwise>
                    </c:choose>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="text" class="form-control" value="${email}" disabled>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Họ và tên</label>
                        <input type="text" name="fullname" class="form-control"
                               value="${profileForm.fullname}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" name="phone" class="form-control"
                               value="${profileForm.phone}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ảnh đại diện</label>
                        <input type="file" name="avatarFile" class="form-control" accept="image/*">
                    </div>
                    <c:if test="${not empty avatarPath}">
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" name="removeAvatar" id="removeAvatar">
                            <label class="form-check-label" for="removeAvatar">Xóa ảnh đại diện hiện tại</label>
                        </div>
                    </c:if>
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                </form>
            </div>
        </div>
    </div>
</div>
