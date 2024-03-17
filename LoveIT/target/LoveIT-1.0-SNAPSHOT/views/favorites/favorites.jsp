<%--
  Created by IntelliJ IDEA.
  User: Nhat
  Date: 2/27/2024
  Time: 3:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage
        pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">
    <main>
        <div class="d-flex flex-wrap mt-3"> 
            <c:forEach var="post" items="${favorite_posts}">
                <div class="card mx-auto fav-post card-border shadow">
                    <div class="card-body d-flex flex-column post">
                        <div class="d-flex justify-content-end">
                            <form action="${pageContext.request.contextPath}/favorites?action=remove_fav&post_id=${post.id}" method="post">
                                <button class="trans-button" type="submit">
                                    <i class="fa-solid fa-heart" style="color: red;"></i>
                                </button>
                            </form>
                        </div>
                        <div class="row" class="h-1">
                            <div class="col-2 col-lg-1 col-xl-3 offset-lg-0 d-flex flex-column align-items-center col-md-3">
                                <a href="#">
                                    <img class="card-border" height="100px" width="90px" src="${post.user.imageUrl}">
                                </a>
                                <p class="text-center fst-italic">Age ${post.user.age}</p>
                            </div>
                            <div class="col-lg-6 col-xl-9 offset-lg-0 col-md-3" id="post-top-5">
                                <div class="mb-2">
                                    <h1 class="txt-main-col fs-3 mb-0">${post.user.fullName}</h1>
                                    <small class="fav-subtitle">${post.user.nickName}</small>
                                </div>
                                <div>
                                    <p class="fav-pref text-start">I am a <strong>${post.user.gender.name}</strong>, <br>looking for a <strong>${post.user.preferenceGender.name}</strong> </p>
                                </div>
                            </div>
                        </div>
                        <div>
                            <img class="d-xl-flex mx-auto mb-2" src="${post.imageUrl}" width="100%" height="270">
                        </div>
                        <p class="card-text">${post.content}</p>
                        <div class="d-xl-flex justify-content-xl-end">
                            <form class="col-lg-3 col-xl-3 offset-lg-1 my-auto" action="${pageContext.request.contextPath}/people-zone" method="get">
                                <input type="hidden" name="action" value="post_details">
                                <input type="hidden" name="post_id" value="${post.id}">
                                <input type="hidden" name="is_favorite" value="${true}">
                                <button type="submit" class="trans-button d-flex align-items-center"><strong style="font-size: 20px;">Go to comment <i class="fa-solid fa-arrow-right ms-2"></i></strong></button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>   
    </main>
</DN:GenericPage>