<%--
  Created by IntelliJ IDEA.
  User: Nhat
  Date: 3/16/2024
  Time: 10:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage
        pageStyleUrl="${pageContext.request.contextPath}/css/admin/admin-posts.css">
    <main>
        <h2>Posts Management Data Table</h2>
        <table>
            <thead>
                <tr>
                    <th>Post ID</th>
                    <th>User ID</th>
                    <th>User Name</th>
                    <th>Content</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
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
                            <form action="${pageContext.request.contextPath}/admin?action=delete_p&id=${post.id}" method="post">
                                <button type="submit">Delete</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin?action=flag_p&id=${post.id}" method="post">
                                <button type="submit">${post.status == 'Active' ? 'Flag' : 'Unflag'}</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h2>Comments Management Data Table</h2>
        <table>
            <thead>
                <tr>
                    <th>Comment ID</th>
                    <th>User ID</th>
                    <th>User Name</th>
                    <th>Content</th>
                    <th>Post ID</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
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
                            <form action="${pageContext.request.contextPath}/admin?action=delete_cmt&id=${comment.id}" method="post">
                                <button type="submit">Delete</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin?action=flag_cmt&id=${comment.id}" method="post">
                                <button type="submit">${comment.status == 'Active' ? 'Flag' : 'Unflag'}</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</DN:GenericPage>