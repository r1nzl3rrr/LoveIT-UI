<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<DN:GenericPage 
    pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">

    <main> 
        <div class="mt-1 mb-3">
            <div class="card m-auto w-75 card-container card-border shadow">
                <div class="card-body post">
                    <!-- Post --> 
                    <div class="row">
                        <div class="col-2 col-lg-1 col-xl-1 offset-lg-0 col-md-3 ms-1">
                            <c:choose>
                                <c:when test="${post.user.id eq SESSION_USER.id}">
                                    <a  href="user-profile">
                                        <img class="card-border" height="90px" width="90px" src="${post.user.imageUrl}">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a  href="other-profile?userId=${post.user.id}">
                                        <img class="card-border" height="90px" width="90px" src="${post.user.imageUrl}">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-lg-6 offset-lg-0 d-flex flex-wrap col-md-3">
                            <div class="d-flex w-100">
                                <h1 class="d-flex txt-main-col">${post.user.fullName}</h1>
                                <em class="d-flex d-xl-flex my-auto align-items-xl-center ms-4 px-2 border border-dark-subtle rounded border-3">Age ${post.user.age}</em>
                            </div>
                            <small class="post-subtitle">${post.user.nickName}</small>
                        </div>
                        <div class="col-lg-3 col-xl-3 offset-lg-1 offset-xl-0 mt-sm-auto">
                            <p class="text-center text-secondary-emphasis">
                                I am a <strong>${post.user.gender.name}</strong>, looking for a <strong>${post.user.preferenceGender.name}</strong>
                            </p>
                        </div>
                        <div class="col-xl-1 offset-xl-0 d-xl-flex justify-content-xl-center align-items-xl-start ms-5 fs-2">
                            <form action="${pageContext.request.contextPath}/people-zone?action=favorite&post_id=${post.id}" method="post">
                                <button type="submit" class="trans-button">
                                    <c:choose>
                                        <c:when test="${post.isFavorite}">
                                            <i class="fa-solid fa-heart" style="color: red;"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa-solid fa-heart" style="color: black;"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                            </form>
                        </div>
                    </div>
                    <!-- Content of the post -->
                    <div class="d-flex">
                        <pre class="w-75 pt-4 me-2">${post.content}</pre>
                        <img class="w-25" src="${post.imageUrl}">
                    </div>
                                
                    <hr class="my-6">
                    
                    <!-- Comments -->
                    <div class="d-flex justify-content-center">
                        <div class="row mt-1 pt-1 w-75">
                            <div class="container border border-3 border-dark-subtle rounded p-2 shadow mb-3">
                                <c:choose>
                                    <c:when test="${not empty comments and comments ne null}">
                                        <c:forEach var="comment" items="${comments}">
                                            <c:set var="replies" value="${comment.replies}" scope="request" />
                                            <jsp:include page="comment.jsp">
                                                <jsp:param name="post_id" value="${post.id}" />
                                                <jsp:param name="user_id" value="${comment.user.id}" />
                                                <jsp:param name="comment_id" value="${comment.id}" />
                                                <jsp:param name="user_image_url" value="${comment.user.imageUrl}" />
                                                <jsp:param name="user_name" value="${comment.user.fullName}" />
                                                <jsp:param name="content" value="${comment.content}" />
                                            </jsp:include>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="d-flex justify-content-center">
                                            <p>There are no comments yet, be the first to share your thoughts!</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                
                            </div>
                        </div>
                    </div>
                    
                    <!-- Post Comment -->
                    <div class="d-flex flex-row p-2 w-100 mb-3 justify-content-center mt-5">
                        <div class="me-3">
                            <c:choose>
                                <c:when test="${sessionScope.SESSION_USER ne null}">
                                    <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/user-profile">
                                        <img class="card-border" height="60px" src="${sessionScope.SESSION_USER.imageUrl}">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/login">
                                        <img class="card-border" height="60px" src="${pageContext.request.contextPath}/assets/img/Default_pfp.svg">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <form class="d-flex col-xl-10" action="${pageContext.request.contextPath}/post-details?action=create_comment&post_id=${post.id}" method="post">
                            <div class="col-md-4 col-xl-9 d-flex flex-row">
                                <input class="my-auto border-bottom border-3 border-dark-subtle" type="text" name="content" placeholder="Write your comment here...">
                            </div>
                            <div class="col-md-4 col-xl-3 d-xl-flex justify-content-xl-center align-items-xl-center">
                                <button class="btn btn-secondary btn-pink-subtle d-flex justify-content-xl-center align-items-xl-center btn-admin fs-6 h-50 shadow w-50 p-4" type="submit">
                                    Send <i class="fa fa-paper-plane ms-2"></i>
                                </button>
                            </div>
                        </form>
                    </div> 
                </div>
            </div>
        </div>
    </main>
</DN:GenericPage>