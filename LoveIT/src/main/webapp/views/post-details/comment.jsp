<%-- 
    Document   : comments
    Created on : Mar 15, 2024, 9:38:26 PM
    Author     : khanh
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Comments -->
<div class="d-flex flex-row p-2">
    <div class="me-3">
        <c:choose>
            <c:when test="${param.user_id eq SESSION_USER.id}">
                <a  href="user-profile">
                    <img class="card-border" height="80px" width="80px" src="${param.user_image_url}">
                </a>
            </c:when>
            <c:otherwise>
                <a  href="other-profile?userId=${param.user_id}">
                    <img class="card-border" height="80px" width="80px" src="${param.user_image_url}">
                </a>
            </c:otherwise>
        </c:choose>
    </div>
    <div><a href="#"><strong>${param.user_name}</strong></a>
        <pre>${param.content}</pre>
    </div>
</div>

<!-- Replies -->
<c:choose>
    <c:when test="${not empty replies}">
        <c:forEach var="reply" items="${replies}">
            <div class="d-flex flex-row p-2 w-75 mx-auto border border-3 border-dark-subtle rounded bg-secondary mb-3">
                <div class="me-3">
                    <c:choose>
                        <c:when test="${reply.user.id eq SESSION_USER.id}">
                            <a  href="user-profile">
                                <img class="card-border" height="65px" width="65px" src="${reply.user.imageUrl}">
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a  href="other-profile?userId=${reply.user.id}">
                                <img class="card-border" height="65px" width="65px" src="${reply.user.imageUrl}">
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div><a href="#"><strong>${reply.user.fullName}</strong></a>
                    <pre>${reply.content}</pre>
                </div>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div class="d-flex justify-content-center">
            <p>There are no replies, be the first to share your thoughts!</p>
        </div>
    </c:otherwise>
</c:choose>

<!-- Post reply -->
<div class="d-flex flex-row p-2 w-75 mx-auto mb-3">
    <div class="me-3">
        <c:choose>
            <c:when test="${sessionScope.SESSION_USER ne null}">
                <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/user-profile">
                    <img class="card-border" height="60px" width="60px" src="${sessionScope.SESSION_USER.imageUrl}">
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/login">
                    <img class="card-border" height="60px" src="${pageContext.request.contextPath}/assets/img/Default_pfp.svg">
                </a>
            </c:otherwise>
        </c:choose>
    </div>

    <form class="d-flex col-xl-10" action="${pageContext.request.contextPath}\post-details?action=create_reply&post_id=${param.post_id}&parent_cmt_id=${param.comment_id}" method="post">
        <div class="col-md-4 col-xl-8 d-flex flex-row">
            <input class="my-auto border-bottom border-3 border-dark-subtle" type="text" name="reply_content" placeholder="Write your reply here...">
        </div>
        <div class="col-md-4 col-xl-4 d-xl-flex justify-content-xl-center align-items-xl-center">
            <button class="btn btn-secondary btn-pink-subtle d-flex justify-content-xl-center align-items-xl-center btn-admin fs-6 h-50 shadow w-50 p-4" type="submit">
                Send <i class="fa fa-paper-plane ms-2"></i>
            </button>
        </div>
    </form>
</div>
        
