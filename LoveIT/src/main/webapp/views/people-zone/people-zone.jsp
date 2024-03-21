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
        <input type="hidden" id="loggedInUserId" value="${sessionScope.SESSION_USER.id}" />
        <!-- Create post -->
        <div class="new-post" id="new-post">
            <div class="card-body my-4">
                <form action="${pageContext.request.contextPath}/people-zone?action=create_post" method="post">
                    <div class="container card-container card-border shadow">
                        <div class="row">
                            <div class="d-flex align-items-center col-md-12 col-lg-12 ps-4 w-75">
                                <c:choose>
                                    <c:when test="${sessionScope.SESSION_USER ne null}">
                                        <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/user-profile">
                                            <img class="card-border" height="45px" width="45px" src="${sessionScope.SESSION_USER.imageUrl}">
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="nav-link" style="display: inline-block;" href="${pageContext.request.contextPath}/login">
                                            <img class="card-border" height="45px" width="45px" src="${pageContext.request.contextPath}/assets/img/Default_pfp.svg">
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                                <textarea id="content" name="content" rows="4" cols="50" class="w-75 border-bottom border-3 ms-3" placeholder="Make a new post..."></textarea>
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
                <input type="text" name="keyword" placeholder="e.g. Sarah">
                <button type="submit" class="trans-button"><i class="fa fa-search"></i></button>
            </form>
        </div> 
        <!-- Posts -->  
        <div class="mt-5" id="posts-container">
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
        // Initialize variables
        let page = 1;
        let isLoading = false;
        const contextPath = "${pageContext.request.contextPath}";
        let fetchedPostIds = [];

        // Add the IDs of the initial posts to the fetchedPostIds array
        <c:forEach var="post" items="${posts}">
            fetchedPostIds.push(${post.id});
        </c:forEach>

        // Event listener for scroll event
        window.onscroll = function () {
            // Check if the user has scrolled to the bottom of the page
            if (!isLoading && (window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                isLoading = true;
                loadMorePosts();
            }
        };

        // Function to load more posts
        function loadMorePosts() {
            console.log('Loading more posts...');
            page++;
            // Send a POST request to the server to fetch more posts
            fetch(contextPath + '/people-zone?action=fetch&page=' + page, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(fetchedPostIds),
            })
                    .then(response => response.json())
                    .then(data => {
                        // Loop through the posts returned by the server
                        data.forEach(post => {
                            fetchedPostIds.push(post.id);
                            const mainDiv = document.createElement('div');
                            mainDiv.classList.add('mt-1', 'mb-3');

                            const cardDiv = document.createElement('div');
                            cardDiv.classList.add('card', 'm-auto', 'w-75', 'card-container', 'card-border', 'shadow');

                            const cardBodyDiv = document.createElement('div');
                            cardBodyDiv.classList.add('card-body', 'post');

                            // USER INFORMATION SECTION
                            const userRowDiv = document.createElement('div');
                            userRowDiv.classList.add('row');

                            const userProfileColDiv = document.createElement('div');
                            userProfileColDiv.classList.add('col-2', 'col-lg-1', 'col-xl-1', 'offset-lg-0', 'col-md-3', 'ms-1');

                            // Conditional logic for user profile image and link based on current user
                            let userProfileLink;
                            const loggedInUserId = document.getElementById('loggedInUserId').value;

                            if (Number(post.user.id) === Number(loggedInUserId)) {
                              userProfileLink = document.createElement('a');
                              userProfileLink.href = 'user-profile';
                            } else {
                              userProfileLink = document.createElement('a');
                              userProfileLink.href = 'other-profile?userId=' + post.user.id;
                            }

                            const userProfileImg = document.createElement('img');
                            userProfileImg.classList.add('card-border');
                            userProfileImg.height = 90;
                            userProfileImg.width = 90;
                            userProfileImg.src = post.user.imageUrl;

                            userProfileLink.appendChild(userProfileImg);
                            userProfileColDiv.appendChild(userProfileLink);

                            const userNameColDiv = document.createElement('div');
                            userNameColDiv.classList.add('col-lg-6', 'offset-lg-0', 'd-flex', 'flex-wrap', 'col-md-3');

                            //USER PROFILE INFORAMTION
                            const userNameAndAgeDiv = document.createElement('div');
                            userNameAndAgeDiv.classList.add('d-flex', 'w-100');
       
                            const userNameDiv = document.createElement('h1');
                            userNameDiv.classList.add('d-flex', 'txt-main-col');
                            userNameDiv.textContent = post.user.fullName;

                            const userAgeDiv = document.createElement('em');
                            userAgeDiv.classList.add('d-flex', 'd-xl-flex', 'my-auto', 'align-items-xl-center', 'ms-4', 'px-2', 'border', 'border-dark-subtle', 'rounded', 'border-3');
                            userAgeDiv.textContent = 'Age ' + post.user.age;

                            userNameAndAgeDiv.appendChild(userNameDiv);
                            userNameAndAgeDiv.appendChild(userAgeDiv);


                            const userNicknameDiv = document.createElement('small');
                            userNicknameDiv.classList.add('post-subtitle');
                            userNicknameDiv.textContent = post.user.nickName;

                            userNameColDiv.appendChild(userNameAndAgeDiv);
                            userNameColDiv.appendChild(userNicknameDiv);
                            
                            //USER GENDER AND PREFERENCE SECTION
                            const userPrefColDiv = document.createElement('div');
                            userPrefColDiv.classList.add('col-lg-3', 'col-xl-3', 'offset-lg-1', 'offset-xl-0', 'mt-sm-auto');

                            const userPrefPara = document.createElement('p');
                            userPrefPara.classList.add('text-center', 'text-secondary-emphasis');

                            const genderText = document.createTextNode('I am a ');
                            
                            const userGenderStrong = document.createElement('strong');
                            userGenderStrong.textContent = post.user.gender.name;

                            const prefText = document.createTextNode(', looking for a ');

                            const userPrefStrong = document.createElement('strong');
                            userPrefStrong.textContent = post.user.preferenceGender.name;
                            
                            userPrefPara.appendChild(genderText);
                            userPrefPara.appendChild(userGenderStrong);
                            userPrefPara.appendChild(prefText);
                            userPrefPara.appendChild(userPrefStrong);

                            userPrefColDiv.appendChild(userPrefPara);

                            //HEART BUTTON SECTION
                            const userFavColDiv = document.createElement('div');
                            userFavColDiv.classList.add('col-xl-1', 'offset-xl-0', 'd-xl-flex', 'justify-content-xl-center', 'align-items-xl-start', 'ms-5', 'fs-2');

                            const favForm = document.createElement('form');
                            favForm.action = '${contextPath}/LoveIT/people-zone?action=favorite&post_id=' + post.id;
                            favForm.method = 'post';

                            const favButton = document.createElement('button');
                            favButton.type = 'submit';
                            favButton.classList.add('trans-button');

                            // Conditional logic for favorite icon based on is_favorite variable
                            let favIcon;
                            if (post.isFavorite) {
                              favIcon = document.createElement('i');
                              favIcon.classList.add('fa-solid', 'fa-heart');
                              favIcon.style.color = 'red';
                            } else {
                              favIcon = document.createElement('i');
                              favIcon.classList.add('fa-solid', 'fa-heart');
                              favIcon.style.color = 'black';
                            }

                            favButton.appendChild(favIcon);
                            favForm.appendChild(favButton);
                            userFavColDiv.appendChild(favForm);

                            userRowDiv.appendChild(userProfileColDiv);
                            userRowDiv.appendChild(userNameColDiv);
                            userRowDiv.appendChild(userPrefColDiv);
                            userRowDiv.appendChild(userFavColDiv);

                            // POST CONTENT SECTION
                            const contentDiv = document.createElement('div');
                            contentDiv.classList.add('d-flex');

                            const postContentPre = document.createElement('pre');
                            postContentPre.classList.add('w-75', 'pt-4', 'me-2');
                            postContentPre.textContent = post.content;

                            const postImage = document.createElement('img');
                            postImage.classList.add('w-25');
                            postImage.style.borderRadius = '20px';
                            postImage.src = post.imageUrl;

                            contentDiv.appendChild(postContentPre);
                            contentDiv.appendChild(postImage);

                            // COMMENT PREVIEW SECTION
                            const commentRowDiv = document.createElement('div');
                            commentRowDiv.classList.add('row', 'border-top', 'border-3', 'mt-3', 'pt-3');

                            const commentColDiv = document.createElement('div');
                            commentColDiv.classList.add('col-2', 'col-lg-7', 'col-xl-7', 'offset-lg-0');

                            const commentCardDiv = document.createElement('div');
                            commentCardDiv.classList.add('card');

                            const commentCardBodyDiv = document.createElement('div');
                            commentCardBodyDiv.classList.add('card-body');

                            const commentRowInnerDiv = document.createElement('div');
                            commentRowInnerDiv.classList.add('row');

                            // Conditional logic for comment content
                            if (post.topComment != null) {
                              const commentProfileColDiv = document.createElement('div');
                              commentProfileColDiv.classList.add('col-lg-1', 'col-xl-2');

                              const commentProfileImg = document.createElement('img');
                              commentProfileImg.classList.add('card-border');
                              commentProfileImg.height = 70;
                              commentProfileImg.width = 70;
                              commentProfileImg.src = post.topComment.user.imageUrl;

                              commentProfileColDiv.appendChild(commentProfileImg);

                              const commentTextColDiv = document.createElement('div');
                              commentTextColDiv.classList.add('col', 'post-comment-content', 'card-container', 'shadow');

                              const commentUserName = document.createElement('h3');
                              commentUserName.textContent = post.topComment.user.fullName;
                              commentUserName.classList.add('txt-main-col');

                              const commentTextPre = document.createElement('pre');
                              commentTextPre.textContent = post.topComment.content;

                              commentTextColDiv.appendChild(commentUserName);
                              commentTextColDiv.appendChild(commentTextPre);

                              commentRowInnerDiv.appendChild(commentProfileColDiv);
                              commentRowInnerDiv.appendChild(commentTextColDiv);

                            } else {
                              const noCommentPara = document.createElement('p');
                              noCommentPara.textContent = 'Nobody has commented yet, be the first to share your thoughts!';
                              commentRowInnerDiv.appendChild((noCommentPara));
                            }

                            commentCardBodyDiv.appendChild(commentRowInnerDiv);
                            commentCardDiv.appendChild(commentCardBodyDiv);
                            commentColDiv.appendChild(commentCardDiv);

                            // SEE MORE COMMENTS SECTION
                            const seeMoreForm = document.createElement('form');
                            seeMoreForm.classList.add('col-lg-3', 'col-xl-3', 'offset-lg-1', 'my-auto');
                            seeMoreForm.action = `${contextPath}/LoveIT/people-zone`;
                            seeMoreForm.method = 'get';

                            const hiddenActionInput = document.createElement('input');
                            hiddenActionInput.type = 'hidden';
                            hiddenActionInput.name = 'action';
                            hiddenActionInput.value = 'post_details';

                            const hiddenPostIdInput = document.createElement('input');
                            hiddenPostIdInput.type = 'hidden';
                            hiddenPostIdInput.name = 'post_id';
                            hiddenPostIdInput.value = post.id;

                            const hiddenIsFavInput = document.createElement('input');
                            hiddenIsFavInput.type = 'hidden';
                            hiddenIsFavInput.name = 'is_favorite';
                            hiddenIsFavInput.value = post.isFavorite;

                            const seeMoreButton = document.createElement('button');
                            seeMoreButton.type = 'submit';
                            seeMoreButton.classList.add('trans-button', 'd-flex', 'align-items-center');

                            const seeMoreStrong = document.createElement('strong');
                            seeMoreStrong.classList.add('fs-3');

                            const seeMoreText = document.createTextNode('See more');

                            const seeMoreArrow = document.createElement('i');
                            seeMoreArrow.classList.add('fa-solid', 'fa-arrow-right', 'ms-2');

                            seeMoreStrong.appendChild(seeMoreText);
                            seeMoreStrong.appendChild(seeMoreArrow);
                            seeMoreButton.appendChild(seeMoreStrong);

                            seeMoreForm.appendChild(hiddenActionInput);
                            seeMoreForm.appendChild(hiddenPostIdInput);
                            seeMoreForm.appendChild(hiddenIsFavInput);
                            seeMoreForm.appendChild(seeMoreButton);

                            commentRowDiv.appendChild(commentColDiv);
                            commentRowDiv.appendChild(seeMoreForm);

                            // APPEND ALL SECTION TO THE MAIN DIV
                            cardBodyDiv.appendChild(userRowDiv);
                            cardBodyDiv.appendChild(contentDiv);
                            cardBodyDiv.appendChild(commentRowDiv);

                            cardDiv.appendChild(cardBodyDiv);
                            mainDiv.appendChild(cardDiv);

                            // Finally, append the main structure to the desired container in your HTML (assuming you have a container with ID "post-container")
                            document.getElementById('posts-container').appendChild(mainDiv);
                        });
                        return fetchedPostIds;
                    })
                    .then(fetchedPostIds => {
                        isLoading = false;
                        console.log('Fetched post IDs:', fetchedPostIds);
                    });
        }
    </script>

</DN:GenericPage>