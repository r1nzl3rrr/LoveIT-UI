<%--
  Created by IntelliJ IDEA.
  User: Nhat
  Date: 2/27/2024
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
    
<div class="mt-1 mb-3">
    <div class="card m-auto w-75 card-container card-border shadow">
        <div class="card-body post">
            <!-- Information of the user who posted this post -->
            <div class="row">
                <div class="col-2 col-lg-1 col-xl-1 offset-lg-0 col-md-3 ms-1">
                    <c:choose>
                        <c:when test="${param.user_id eq SESSION_USER.id}">
                            <a  href="user-profile">
                                <img class="card-border" height="90px" width="90px" src="${param.user_image_url}">
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a  href="other-profile?userId=${param.user_id}">
                                <img class="card-border" height="90px" width="90px" src="${param.user_image_url}">
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-lg-6 offset-lg-0 d-flex flex-wrap col-md-3">
                    <div class="d-flex w-100">
                        <h1 class="d-flex txt-main-col">${param.user_name}</h1>
                        <em class="d-flex d-xl-flex my-auto align-items-xl-center ms-4 px-2 border border-dark-subtle rounded border-3">Age ${param.user_age}</em>
                    </div>
                    <small class="post-subtitle">${param.user_nickname}</small>
                </div>
                <div class="col-lg-3 col-xl-3 offset-lg-1 offset-xl-0 mt-sm-auto">
                    <p class="text-center text-secondary-emphasis">
                        I am a <strong>${param.user_gender}</strong>, looking for a <strong>${param.user_preference_gender}</strong>
                    </p>
                </div>
                <div class="col-xl-1 offset-xl-0 d-xl-flex justify-content-xl-center align-items-xl-start ms-5 fs-2">
                    <form action="${pageContext.request.contextPath}/people-zone?action=favorite&post_id=${param.post_id}" method="post">
                        <button type="submit" class="trans-button">
                            <c:choose>
                                <c:when test="${param.is_favorite}">
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
                <p class="w-75 pt-4 me-2">${param.content}</p>
                <img class="w-25" src="${param.image_url}">
            </div>
            <!-- Preview of the comment section of the post -->
            <div class="row border-top border-3 mt-3 pt-3">
                <div class="col-2 col-lg-7 col-xl-7 offset-lg-0">
                    <div class="card">
                        <div class="card-body">
                            <div class="row">
                                <c:choose>
                                    <c:when test="${not empty param.tcomment_u_img_url}">
                                        <div class="col-lg-1 col-xl-2">
                                            <img class="card-border" height="70px" width="70px" src="${param.tcomment_u_img_url}" alt="Comment avatar">
                                        </div>
                                        <div class="col post-comment-content card-container shadow">
                                            <h3><span class="txt-main-col">${param.tcomment_usn}</span></h3>
                                            <p>${param.tcomment_content}</p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Nobody has commented yet, be the first to share your thoughts!</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Routes to detailed comment section -->
                <form class="col-lg-3 col-xl-3 offset-lg-1 my-auto" action="${pageContext.request.contextPath}/people-zone" method="get">
                    <input type="hidden" name="action" value="post_details">
                    <input type="hidden" name="post_id" value="${param.post_id}">
                    <input type="hidden" name="is_favorite" value="${param.is_favorite}">
                    <button type="submit" class="trans-button d-flex align-items-center"><strong class="fs-3">See more<i class="fa-solid fa-arrow-right ms-2"></i></strong></button>
                </form>
            </div>
        </div>
    </div>
</div>