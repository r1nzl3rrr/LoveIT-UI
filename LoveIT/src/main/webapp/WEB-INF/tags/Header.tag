<%@tag description="Page Header" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Constants String --%>
<%
    final String url_people_zone = request.getContextPath() + "/people-zone";
    final String url_favorites = request.getContextPath() + "/favorites";
    final String url_login = request.getContextPath() + "/login";
    final String url_home = request.getContextPath() + "/";


%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="logoUrl" required="true" %>

<%-- any content can be specified here e.g.: --%>
<header class="h">
    <nav class="navbar navbar-expand-lg sticky-top bg-body px-4" data-bs-theme="light">
        <div class="container-fluid">
            <a class="navbar-brand fs-2 fw-bold" href="<%= url_home%>">LoveIT</a>
            <div class="collapse navbar-collapse" id="navcol-1">
                <ul class="navbar-nav mx-auto" id="mid-nav">
                    <li class="nav-item nav-button">
                        <a class="nav-link nav-link-button" href="<%= url_people_zone%>"><i class="fa-solid fa-users me-2"></i>People Zone</a>
                    </li>
                    <li class="nav-item nav-button">
                        <a class="nav-link nav-link-button" href="<%= url_favorites%>"><i class="fa-solid fa-heart me-2"></i>Favorites</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${sessionScope.SESSION_USER ne null}">
                                <a class="nav-link" href="${pageContext.request.contextPath}/user-profile">
                                    <img class="card-border pfp" height="40px" src="${sessionScope.SESSION_USER.imageUrl}">
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a class="nav-link nav-link-button" href="<%= url_login%>"><i class="fa fa-sign-in me-2"></i>Sign in</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <c:if test="${sessionScope.SESSION_USER ne null}">
                        <li class="nav-item d-flex align-items-center">
                             <a class="nav-link nav-link-button" href="logout"><i class="fa fa-sign-in me-2"></i>Sign out</a>
                        </li>
                    </c:if>
                </ul>               
            </div>
        </div>
    </nav>
</header>