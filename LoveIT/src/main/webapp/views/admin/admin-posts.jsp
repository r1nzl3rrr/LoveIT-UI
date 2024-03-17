<%-- 
    Document   : admin-post
    Created on : Mar 17, 2024, 2:57:48 PM
    Author     : duyvu
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage
    pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">

    <main class="d-flex">
        <DN:SideBar />

        <div class="admin-main mt-5">
            <div class="d-flex flex-row justify-content-between">
                <h1 class="justify-content-start text-primary-emphasis ms-3">
                    Posts management</h1>
            </div>
            <div class="table-responsive mt-3 ms-3">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="text-bg-info">Post ID</th>
                            <th class="text-bg-info">User ID</th>
                            <th class="text-bg-info">User Name</th>
                            <th class="text-bg-info">Content</th>
                            <th class="text-bg-info">Status</th>
                            <th class="text-bg-info">Created At</th>
                            <th class="text-bg-info">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${posts}" var="post">
                            <tr>
                                <td>${post.id}</td>
                                <td>${post.user.id}</td>
                                <td>${post.user.fullName}</td>
                                <td>${post.content}</td>
                                <td>${post.status}</td>
                                <td>${post.createdAt}</td>
                                <td>
                                    <div class="d-flex">
                                        <form action="${pageContext.request.contextPath}/admin/posts?action=delete_p&id=${post.id}" method="post">
                                            <button
                                                class="btn btn-white btn-admin d-flex justify-content-xl-center align-items-xl-center shadow bg-white me-2"
                                                type="submit">Delete</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/posts?action=flag_p&id=${post.id}" method="post">
                                            <button
                                                class="btn btn-success btn-admin d-flex justify-content-xl-center align-items-xl-center shadow"
                                                type="submit">${post.status == 'Active' ? 'Flag' : 'Unflag'}</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>                   
                    </tbody>
                </table>
            </div>
            <div class="mt-5">
                <div class="d-flex flex-row justify-content-between mt-5">
                    <h1 class="justify-content-start text-primary-emphasis ms-3">Comments management</h1>
                </div>
                <div class="table-responsive mt-3 ms-3">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th class="text-bg-info">Comment ID</th>
                                <th class="text-bg-info">User ID</th>
                                <th class="text-bg-info">User Name</th>
                                <th class="text-bg-info">Content</th>
                                <th class="text-bg-info">Post ID</th>
                                <th class="text-bg-info">Status</th>
                                <th class="text-bg-info">Created At</th>
                                <th class="text-bg-info">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${comments}" var="comment">
                                <tr>
                                    <td>${comment.id}</td>
                                    <td>${comment.user.id}</td>
                                    <td>${comment.user.fullName}</td>
                                    <td>${comment.content}</td>
                                    <td>${comment.post.id}</td>
                                    <td>${comment.status}</td>
                                    <td>${comment.createdAt}</td>
                                    <td>
                                        <div class="d-flex">
                                            <form action="${pageContext.request.contextPath}/admin/posts?action=delete_cmt&id=${comment.id}" method="post">
                                                <button 
                                                    class="btn btn-white btn-admin d-flex justify-content-xl-center align-items-xl-center shadow bg-white me-2"
                                                    type="submit">Delete</button>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/admin/posts" method="get">
                                                <input type="hidden" name="action" value="flag_cmt" />
                                                <input type="hidden" name="id" value="${comment.id}" />
                                                <button 
                                                    class="btn btn-success btn-admin d-flex justify-content-xl-center align-items-xl-center shadow"
                                                    type="submit">${comment.status == 'Active' ? 'Flag' : 'Unflag'}</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</DN:GenericPage>