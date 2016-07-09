<%@ include file="include.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>

<c:url value="/j_spring_security_logout" var="logout"/>
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/"/>">Dashboard</a>
        </div>
        <div class="navbar-collapse collapse">

            <ul class="nav navbar-nav">

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Editor<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="/editor"/>">Editor</a></li>
                    </ul>
                </li>

            </ul>

            <ul class="nav navbar-nav navbar-right">

                <sec:authorize access="isAuthenticated()">

                    <li class="dropdown">
                        <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><i
                                class="glyphicon glyphicon-user"></i>
                            <sec:authentication property="name"/>
                            <span class="caret"></span>
                        </a>
                        <ul id="g-account-menu" class="dropdown-menu" role="menu">
                            <li><a href="#">My Profile</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="${logout}">
                            <i class="glyphicon glyphicon-lock"></i>
                            Logout
                        </a>
                    </li>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </sec:authorize>
            </ul>

        </div>
    </div>
    <!-- /container -->
</div>
<!-- /Header -->