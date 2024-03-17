<%@page import="com.group03.loveit.models.user.EAccountRole"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%--
  Created by IntelliJ IDEA.
  User: Nhat
  Date: 2/26/2024
  Time: 2:38 PM
  To change this template use File | Settings | File Templates.
--%>


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

    <script>
        let page = 1;
        let isLoading = false;
        const contextPath = "${pageContext.request.contextPath}";

        window.onscroll = function() {
            if (!isLoading && (window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                isLoading = true;
                loadMorePosts();
            }
        };

        function loadMorePosts() {
            console.log('Loading more posts...');
            page++;
            fetch(contextPath + '/people-zone?action=fetch&page=' + page)
                .then(response => response.json())
                .then(data => {
                    data.forEach(post => {
                        const newPost = document.createElement('div');
                        newPost.className = 'post';

                        const topArea = document.createElement('div');
                        topArea.className = 'top-area';

                        const topLeftArea = document.createElement('div');
                        topLeftArea.className = 'top-left-area';

                        const userImage = document.createElement('img');
                        userImage.src = post.user.imageUrl;
                        userImage.alt = 'User Image';

                        const info = document.createElement('div');
                        info.className = 'info';

                        const userName = document.createElement('p');
                        userName.textContent = post.user.fullName;

                        const userAge = document.createElement('p');
                        userAge.textContent = 'Age: ' + post.user.age;

                        const userNickName = document.createElement('p');
                        userNickName.textContent = post.user.nickName;

                        info.appendChild(userName);
                        info.appendChild(userAge);
                        info.appendChild(userNickName);

                        topLeftArea.appendChild(userImage);
                        topLeftArea.appendChild(info);

                        const topRightArea = document.createElement('div');
                        topRightArea.className = 'top-right-area';

                        const favoriteButton = document.createElement('button');
                        favoriteButton.type = 'submit';
                        favoriteButton.innerHTML = post.isFavorite ?
                            '<img src="${pageContext.request.contextPath}/assets/img/heart_on.png" alt="Favorite button">' :
                            '<img src="${pageContext.request.contextPath}/assets/img/heart_off.png" alt="Favorite button">';
                        topRightArea.appendChild(favoriteButton);

                        const userGender = document.createElement('p');
                        userGender.textContent = 'I am ' + post.user.gender.name;
                        topRightArea.appendChild(userGender);

                        const userPreferenceGender = document.createElement('p');
                        userPreferenceGender.textContent = 'Looking for ' + post.user.preferenceGender.name;
                        topRightArea.appendChild(userPreferenceGender);

                        topArea.appendChild(topLeftArea);
                        topArea.appendChild(topRightArea);

                        const contentArea = document.createElement('div');
                        contentArea.className = 'content_area';

                        const content = document.createElement('p');
                        content.textContent = post.content;

                        const postImage = document.createElement('img');
                        postImage.src = post.imageUrl;
                        postImage.alt = 'Post image';

                        contentArea.appendChild(content);
                        contentArea.appendChild(postImage);

                        const commentArea = document.createElement('div');
                        commentArea.className = 'comment-area';

                        const topComment = document.createElement('p');
                        topComment.textContent = post.topComment ? post.topComment.content : 'Nobody had commented yet!';
                        commentArea.appendChild(topComment);

                        const discussNowButton = document.createElement('button');
                        discussNowButton.textContent = 'Discuss Now >';
                        commentArea.appendChild(discussNowButton);

                        newPost.appendChild(topArea);
                        newPost.appendChild(contentArea);
                        newPost.appendChild(commentArea);

                        document.getElementById('posts-container').appendChild(newPost);
                    });
                    isLoading = false;
                });
        }
    </script>
</DN:GenericPage>