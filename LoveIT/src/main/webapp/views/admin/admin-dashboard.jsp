<%--
  Created by IntelliJ IDEA.
  User: Nhat
  Date: 3/16/2024
  Time: 10:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage
    pageStyleUrl="${pageContext.request.contextPath}/assets/css/styles.css">

    <main class="d-flex">
        <DN:SideBar></DN:SideBar>
        <div class="admin-main mt-5">
            <div class="m-auto w-50">
                <canvas data-bss-chart="{&quot;type&quot;:&quot;horizontalBar&quot;,&quot;data&quot;:{&quot;labels&quot;:[&quot;Men&quot;,&quot;Women&quot;,&quot;Other&quot;],&quot;datasets&quot;:[{&quot;label&quot;:&quot;Posts&quot;,&quot;backgroundColor&quot;:&quot;#ff4ff8&quot;,&quot;borderColor&quot;:&quot;#4e73df&quot;,&quot;data&quot;:[&quot;1200&quot;,&quot;900&quot;,&quot;350&quot;]}]},&quot;options&quot;:{&quot;maintainAspectRatio&quot;:true,&quot;legend&quot;:{&quot;display&quot;:false,&quot;labels&quot;:{&quot;fontStyle&quot;:&quot;normal&quot;},&quot;position&quot;:&quot;top&quot;},&quot;title&quot;:{&quot;fontStyle&quot;:&quot;bold&quot;},&quot;scales&quot;:{&quot;xAxes&quot;:[{&quot;ticks&quot;:{&quot;fontStyle&quot;:&quot;normal&quot;}}],&quot;yAxes&quot;:[{&quot;ticks&quot;:{&quot;fontStyle&quot;:&quot;normal&quot;}}]}}}"></canvas></div>
            <div class="mt-2">
                <div class="row">
                    <div class="col">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-4">
                                    <p class="text-center fs-5">Number of posts</p>
                                    <h1 class="text-center txt-main-col">69,696</h1>
                                </div>
                                <div class="col-md-4">
                                    <p class="text-center fs-5">Number of conversations</p>
                                    <h1 class="text-center txt-main-col">69,696</h1>
                                </div>
                                <div class="col-md-4">
                                    <p class="text-center fs-5">Number of accounts</p>
                                    <h1 class="text-center txt-main-col">69,696</h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</DN:GenericPage>