<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">
    <div class="welcome">
        <h1>Welcome to <span style="color: #ff4ff8">LoveIT</span></h1>
        <div class="slogan text-muted mb-5">Let us help you find your special someone.</div>
        
        <form action="register" class="mb-2">
            <div class="gender-selection mb-5">
                <h4>Tell us about yourself</h4>
                <div class="button-container mb-4 mt-3">
                    <select class="form-select" name="picked_gender" aria-label="Team options">
                        <option value="" selected="">Choose</option>
                        <c:forEach items="${requestScope.genders}" var="gender">
                            <option value="${gender.id}">${gender.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <h4>Looking for:</h4>
                <div class="button-container mb-4 mt-3">
                    <select class="form-select" name="picked_preference" aria-label="Preferences options">
                        <option value="">Choose</option>
                        <c:forEach items="${requestScope.genders}" var="gender">
                            <option value="${gender.id}">${gender.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <button class="btn btn-primary d-block w-100 btn-pink shadow" type="submit">Get Started</button>
        </form>
        
        
        <a href="login" class="already-member-link text-muted mt-5">Already a member? Log in here</a>
    </div>
</DN:GenericPage>