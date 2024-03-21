<%-- 
    Document   : profile
    Created on : Mar 10, 2024, 11:34:43 AM
    Author     : duyvu
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="DN" tagdir="/WEB-INF/tags/" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">
    <main>             
        <c:set var="user" value="${sessionScope.SESSION_USER}"></c:set>
        <div class="card mx-auto w-50">
            <form action="${pageContext.request.contextPath}/user-profile" method="POST">
                <div class="card-body">
                    <div class="text-center border-bottom border-dark-subtle border-3 pb-3 mb-3">
                        <img height="200px" src="${user.imageUrl}">
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Full name</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="text" value="${user.fullName}" name="fullName" class="pf-change-input">
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorFullName != null}">
                            <p class="text-danger">${errorFullName}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Nickname</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="text" value="${user.nickName}" name="nickName" class="pf-change-input">
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorNickName != null}">
                            <p class="text-danger">${errorNickName}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Email</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="text" value="${user.email}" name="email" class="pf-change-input">
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorEmail != null}">
                            <p class="text-danger">${errorEmail}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Old Password</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="password" class="pf-change-input" name="oldPassword" placeholder="••••••••" required>
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorOldPassword != null}">
                            <p class="text-danger">${errorOldPassword}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>New Password</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="password" class="pf-change-input" name="newPassword" placeholder="••••••••" required>
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorNewPassword != null}">
                            <p class="text-danger">${errorNewPassword}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Re-type New Password</p>
                            </div>
                            <div class="col">
                                <div class="h-75 border-bottom border-3 border-dark-subtle">
                                    <input type="password" class="pf-change-input" name="retypePassword" placeholder="••••••••" required>
                                    <i class="fas fa-edit"></i>
                                </div>
                            </div>
                        </div>
                        <c:if test="${errorRetypePassword != null}">
                            <p class="text-danger">${errorRetypePassword}</p>
                        </c:if>
                    </div>
                    <div>
                        <div class="row">
                            <div class="col-xl-5">
                                <p>Age</p>
                            </div>
                            <div class="col">
                                <div class="d-flex flex-row justify-content-between align-items-center">
                                    <span class="me-2">18</span>
                                    <input class="flex" type="range" id="age" min="18" max="99" step="1" value="${user.age}" name="age" class="lh-1">
                                    <span class="ms-2">99</span>
                                </div>
                                <span class="d-xl-flex justify-content-xl-center age-slider fw-bold" id="age-value">${user.age}</span>
                            </div>
                        </div>
                    </div>
                    <div class="mt-2">
                        <div class="row border-bottom border-3 border-dark-subtle pb-3">
                            <div class="col">
                                <div class="input-group mb-2">
                                    <span class="input-group-text input-group-text input-group-text bg-white border-none">I am a</span>
                                    <select class="form-select form-select form-select" name="gender" aria-label="Team options">
                                        <c:forEach items="${requestScope.genders}" var="gender">
                                            <c:choose>
                                                <c:when test="${gender.name eq user.getGender().getName()}">
                                                    <option value="${gender.id}" selected>${gender.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${gender.id}">${gender.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                    <span class="input-group-text input-group-text input-group-text bg-white border-none">, my preference is</span>
                                    <select class="form-select form-select form-select" name="preferenceGender" aria-label="Preferences options">
                                        <c:forEach items="${requestScope.genders}" var="preferenceGender">
                                            <c:choose>
                                                <c:when test="${preferenceGender.name eq user.getPreferenceGender().getName()}">
                                                    <option value="${preferenceGender.id}" selected>${preferenceGender.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${preferenceGender.id}">${preferenceGender.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="text-center mb-3">
                    <input type="hidden" name="action" value="edit" />
                    <button class="btn btn-primary w-75 btn-pink shadow" type="submit">Save your changes</button>
                </div>
            </form>
            <script src="${pageContext.request.contextPath}/assets/js/slider.js"></script>                    
        </div>
        <h3>${success}</h3>
    </main>

</DN:GenericPage>

