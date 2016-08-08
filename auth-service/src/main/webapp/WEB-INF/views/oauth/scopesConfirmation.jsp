<%@ page
        import="org.springframework.security.core.AuthenticationException" %>
<%@ page
        import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ page
        import="org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>OAuth Server</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
          integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ=="
          crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"
          integrity="sha384-aUGj/X2zp5rLCbBxumKTCw2Z50WgIr1vs/PFN4praOTvYXWlVyh2UtNUU0KAUhAX" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"
            integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ=="
            crossorigin="anonymous"></script>
</head>

<body>

<h1>Authorization</h1>

<div id="content">

    <h2>Please Confirm</h2>

    <p>
        You authorizing "<c:out value="${client.clientId}"/>" to access your protected resources.
    </p>

    <form id="confirmationForm" name="confirmationForm"
          action="<%=request.getContextPath()%>/oauth/authorize" method="post">
        <input name="user_oauth_approval" value="true" type="hidden"/>
        <ul class="list-unstyled">
            <c:forEach items="${scopes}" var="scope">
                <c:set var="approved">
                    <c:if test="${scope.value}"> checked</c:if>
                </c:set>
                <c:set var="denied">
                    <c:if test="${!scope.value}"> checked</c:if>
                </c:set>
                <li>
                    <div class="form-group">
                            ${scope.key}: <input type="radio" name="${scope.key}"
                                                 value="true" ${approved}>Approve</input> <input type="radio"
                                                                                                 name="${scope.key}"
                                                                                                 value="false" ${denied}>Deny</input>
                    </div>
                </li>
            </c:forEach>
        </ul>
        <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Submit</button>
    </form>

</div>

</body>
</html>