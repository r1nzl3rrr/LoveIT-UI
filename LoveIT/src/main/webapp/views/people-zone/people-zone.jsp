<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">
    <main>
        <!-- Create post -->
        <div class="new-post" id="new-post">
            <div class="card-body my-4">
                <form action="${pageContext.request.contextPath}/people-zone?action=create_post" method="post">
                    <div class="container card-container card-border shadow">
                        <div class="row">
                            <div class="col-md-12 col-lg-12 ps-4 w-75">
                                <c:choose>
                                    <c:when test="${sessionScope.SESSION_USER ne null}">
                                        <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/user-profile">
                                            <img class="card-border" height="45px" src="${sessionScope.SESSION_USER.imageUrl}">
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/login">
                                            <img class="card-border" height="45px" src="${pageContext.request.contextPath}/assets/img/Default_pfp.svg">
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                                <input type="text" name="content" class="w-75 border-bottom border-3" placeholder="Make a new post...">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-8 offset-lg-1">
                                <p class="fs-6 fw-normal w-auto">Add an image here:<input type="text" name="imageUrl" class="ms-2 mt-2 w-50 border" placeholder="Image Url..."></p>
                            </div>
                            <div class="col">
                                <button class="btn d-flex align-items-center fs-4 p-2 px-3 shadow border border-3 border-dark-subtle" type="submit">
                                    Post <i class="fa fa-paper-plane ms-2"></i>  
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
         <div class="d-flex justify-content-end mt-4" style="margin-right: 15%;">
            <form action="${pageContext.request.contextPath}/people-zone" class="d-flex" method="get">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="e.g. Sarah"><button type="submit" class="trans-button"><i class="fa fa-search"></i></button>
            </form>
        </div> 
        <!-- Posts -->  
        <div class="mt-5">
            <c:forEach var="post" items="${posts}">
                <jsp:include page="post.jsp">
                    <jsp:param name="post_id" value="${post.id}" />
                    <jsp:param name="user_id" value="${post.user.id}" />
                    <jsp:param name="user_image_url" value="${post.user.imageUrl}" />
                    <jsp:param name="user_name" value="${post.user.fullName}" />
                    <jsp:param name="user_nickname" value="${post.user.nickName}" />
                    <jsp:param name="user_age" value="${post.user.age}" />
                    <jsp:param name="user_gender" value="${post.user.gender.name}" />
                    <jsp:param name="user_preference_gender" value="${post.user.preferenceGender.name}" />
                    <jsp:param name="content" value="${post.content}" />
                    <jsp:param name="image_url" value="${post.imageUrl}" />
                    <jsp:param name="tcomment_u_img_url" value="${post.topComment.user.imageUrl}" />
                    <jsp:param name="tcomment_usn" value="${post.topComment.user.fullName}" />
                    <jsp:param name="tcomment_content" value="${post.topComment.content}" />
                    <jsp:param name="is_favorite" value="${post.isFavorite}" />
                </jsp:include>
            </c:forEach>
        </div>
    </main>
</DN:GenericPage>