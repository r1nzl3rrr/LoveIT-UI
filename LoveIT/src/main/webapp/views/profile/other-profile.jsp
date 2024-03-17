<%-- 
    Document   : other-profile
    Created on : Mar 16, 2024, 3:25:23 PM
    Author     : duyvu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">
    <div class="card mx-auto w-50 mt-5">
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
                        <p class="fw-bold">${user.fullName}</p>
                    </div>
                </div>
            </div>
            <div>
                <div class="row">
                    <div class="col-xl-5">
                        <p>Nickname</p>
                    </div>
                    <div class="col">
                        <p class="fw-bold">${user.nickName}</p>
                    </div>
                </div>
            </div>
            <div>
                <div class="row">
                    <div class="col-xl-5">
                        <p>Email</p>
                    </div>
                    <div class="col">
                        <p class="fw-bold">${user.email}</p>
                    </div>
                </div>
            </div>
            <div>
                <div class="row">
                    <div class="col-xl-5">
                        <p>Age</p>
                    </div>
                    <div class="col">
                        <p class="fw-bold">${user.age}</p>
                    </div>
                </div>
            </div>
            <div>
                <div class="row">
                    <div class="col-xl-5">
                        <p>I am a </p>
                    </div>
                    <div class="col">
                        <p class="fw-bold">${user.getGender().getName()}</p>
                    </div>
                </div>
            </div>
            <div>
                <div class="row border-bottom border-dark-subtle border-3">
                    <div class="col-xl-5">
                        <p>Looking for a </p>
                    </div>
                    <div class="col">
                        <p class="fw-bold">${user.getPreferenceGender().getName()}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</DN:GenericPage>
