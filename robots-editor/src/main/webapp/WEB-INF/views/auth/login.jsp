<%@ include file="../include/include.jsp" %>

<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/login.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error.css'/>"/>


</head>
<body>

<%@ include file="../include/navbar.jsp" %>


<div class="container">

    <c:url value="/j_spring_security_check" var="loginUrl"/>


    <form class="form-signin" action="${loginUrl}" method="post">

        <h1 class="form-signin-heading">Sign In</h1>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <input type="text" class="form-control" name='username' placeholder="User name" required="" autofocus="">
        <input type="password" class="form-control" name='password' placeholder="Password" required="">
        <button class="btn btn-lg btn-primary btn-block" type="submit">
            Sign In
        </button>
        <a class="pull-right" href="<c:url value="/register"/>">Sign up here!</a>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

</div>

</body>
</html>