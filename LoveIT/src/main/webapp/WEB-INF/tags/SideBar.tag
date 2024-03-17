<%@tag description="Sidebar" pageEncoding="UTF-8" isELIgnored="false"%>

<aside class="sidebar">
    <nav class="navbar align-items-start sidebar sidebar-dark accordion bg-gradient-primary p-0 navbar-dark">
        <div class="container-fluid d-flex flex-column p-0">
            <hr class="sidebar-divider my-0">
            <ul class="navbar-nav w-75 fs-5">
                <li class="nav-item ps-2">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=dashboard">
                        <i class="fa fa-dashboard" style="color: #ff4ff8"></i>
                        <span class="txt-main-col">Dashboard</span>
                    </a>
                </li>
                <li class="nav-item ps-2">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=accounts">
                        <i class="fa fa-user" style="color: #ff4ff8"></i>
                        <span class="txt-main-col">Accounts</span>
                    </a>
                </li>
                <li class="nav-item ps-2">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin?action=posts">
                        <i class="fa fa-heart" style="color: #ff4ff8"></i>
                        <span class="txt-main-col">Posts</span>
                    </a>
                </li>
            </ul>
            <div class="text-center d-none d-md-inline">
                <button class="btn rounded-circle border-0" id="sidebarToggle" type="button"></button>
            </div>
        </div>
    </nav>
</aside>