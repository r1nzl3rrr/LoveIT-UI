<%@page import="com.group03.loveit.models.user.EStatus"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage
    pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">

    <main class="d-flex">
        <DN:SideBar />
        <div class="admin-main mt-5">
            <div class="d-flex flex-row justify-content-between">
                <h1 class="justify-content-start text-primary-emphasis ms-3">Accounts management</h1>
            </div>
            <div class="table-responsive mt-3 ms-3">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="text-bg-info">User ID</th>
                            <th class="text-bg-info">Fullname</th>
                            <th class="text-bg-info">Nickname</th>
                            <th class="text-bg-info">Email</th>
                            <th class="text-bg-info">Age</th>
                            <th class="text-bg-info">Status</th>
                            <th class="text-bg-info">Role</th>
                            <th class="text-bg-info">Gender</th>
                            <th class="text-bg-info">Preference Gender</th>
                            <th class="text-bg-info">Created At</th>
                            <th class="text-bg-info">Image Url</th>
                            <th class="text-bg-info"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${accounts}" var="acc">
                            <tr>
                                <td>${acc.id}</td>
                                <td>${acc.fullName}</td>
                                <td>${acc.nickName}</td>
                                <td>${acc.email}</td>
                                <td>${acc.age}</td>
                                <td>${acc.status}</td>
                                <td>${acc.role}</td>
                                <td>${acc.gender.name}</td>
                                <td>${acc.preferenceGender.name}</td>
                                <td>${acc.createdAt}</td>
                                <td>${acc.imageUrl}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/accounts?action=flag_acc&id=${acc.id}" method="post">
                                        <button type="submit" class="btn btn-success btn-admin d-flex justify-content-xl-center align-items-xl-center shadow">
                                            ${acc.status == EStatus.ACTIVE? "Flag": "Unflag"}
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</DN:GenericPage>