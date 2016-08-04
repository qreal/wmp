<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="properties.jsp" %>
<!--.navbar-->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">OAuth Server</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="<c:url value="${home}"/>"><span class="glyphicon glyphicon-home"></span> Home</a></li>
                <%--<sec:authorize access="hasRole('ADMIN')">--%>
                <li><a href="<c:url value="${clientsPanel}"/>"><span class="glyphicon glyphicon-cloud"></span> Table
                    servers</a></li>
                <li><a href="<c:url value="${usersPanel}"/>"><span class="glyphicon glyphicon-user"></span> Table
                    users</a></li>
                <%--</sec:authorize>--%>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${name} <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="logout">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<!--/.navbar -->
