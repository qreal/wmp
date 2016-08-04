<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>
    <link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/bootstrap-social.css" />" rel="stylesheet">

    <jsp:include page="../include/head.jsp"/>

</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title">Sign in to continue to Auth</h1>
            <div class="account-wall">
                <img class="profile-img"
                     src="https://cdn4.iconfinder.com/data/icons/mechanical-cogs-and-gear-wheel/500/cogwheel_configuration_configure_control_gear_gears_gearwheel_mechanics_pinion_rackwheel_screw-wheel_settings_tool_steel_machinery-512.png"
                     alt="">
                <form class="form-signin" action="login" method="post">
                    <input type="text" name="username" class="form-control" placeholder="Email:" required autofocus>
                    <input type="password" name="password" class="form-control" placeholder="Password:" required>
                    <button class="btn btn-lg btn-primary btn-block" type="submit">
                        Sign in
                    </button>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </form>
            </div>

            <c:if test="${error}">
                <h2 class="text-center login-title">Password or login wrong</h2>
            </c:if>
            <a href="register" class="text-center new-account">Create an account </a>
            <p class="text-center">or</p>
            <div class="col-md-offset-0 row">
                <a class="btn btn-block btn-social btn-google " href="oauth/google?redirect=${redirect}">
                    <span class="fa fa-google"></span>Sign in with Google+
                </a>
                <a class="btn btn-block btn-social btn-github" href="oauth/github?redirect=${redirect}">
                    <span class="fa fa-github"></span>Sign  in  with Github
                </a>
            </div>
        </div>
    </div>
</div>
</body>

</html>
