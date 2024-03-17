
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">

    <main>
        <section class="position-relative py-4 py-xl-5">
            <div class="container">
                <div class="row mb-5">
                    <div class="col-md-8 col-xl-6 text-center mx-auto">
                        <h1 class="txt-main-col">LoveIT</h1>
                    </div>
                </div>

                <div class="row d-flex justify-content-center">
                    <div class="col-md-6 col-xl-5">
                        <div class="card mb-5 bg-transparent">
                            <h2 class="txt-main-col ms-4">Sign in</h2>
                            <div class="card-body d-flex flex-column">
                                <form action="login" class="w-100 p-4 pb-2 mb-3 border border-3 border-dark-subtle rounded shadow bg-light" method="POST">
                                    <c:set var="cookie" value="${pageContext.request.cookies}" />
                                    <div class="mb-3">
                                        <input class="form-control" type="email" name="email" placeholder="Email" value="${cookie.cEmail.value}">
                                    </div>
                                    <c:if test="${error != null}">
                                        <p>${error}</p>
                                    </c:if>
                                    <div class="mb-3">
                                        <input class="form-control" type="password" name="password" placeholder="Password" value="${cookie.cPass.value}">
                                    </div>
                                    <div class="mb-3 d-flex align-items-center">
                                        <label for="remember-me" class="text-info">Remember me</label>
                                        <input id="remember-me" name="remember-me" type="checkbox" style="width: 10%;" ${cookie.cRememberMe != null ? 'checked': ''} />
                                    </div>
                                    <div class="d-flex flex-wrap justify-content-around">
                                        <div class="w-100 mb-3">
                                            <input name="action" value="check" type="hidden" />
                                            <button class="btn btn-primary d-block w-100 shadow btn-pink" type="submit">Login</button>
                                        </div>
                                    </div>
                                </form>
                                <a class="text-center pb-3" href="/LoveIT/register">Does not have an account yet? Register here</a>
<!--                                <div class="d-flex align-items-center align-content-center">
                                    <hr class="m-auto w-50">
                                    <span class="px-3 text-secondary">or</span>
                                    <hr class="m-auto w-50">
                                </div>-->
                                <!-- <div class="d-flex justify-content-center mt-3">
                                    <button class="btn btn-light border-dark d-flex justify-content-center align-items-center" type="button">
                                        <img height="20px" width="20px" class="me-2" src="${pageContext.request.contextPath}/assets/img/Google.png"> Continue with Google
                                    </button>
                                </div>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

</DN:GenericPage>
