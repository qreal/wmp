<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Users Control Panel</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <jsp:include page="../include/head.jsp"/>

</head>

<body>

<jsp:include page="../include/navbar.jsp"/>

<div class="container">
    <c:if test="${not empty users}">
        <table class="table">
            <tr>
                <th>Login</th>
                <th>Password</th>
            </tr>
            <c:forEach var="user" items="${users}" varStatus="status">
                <c:if test="${!user.isAdmin()}">
                <form class="form-signin" action="usersPanel/grantUserAdminRights/${usersEncoded[status.index]}" method="post">
                </c:if>
                <c:if test="${user.isAdmin() && user.username != 'Admin'}">
                <form class="form-signin" action="usersPanel/withdrawUserAdminRights/${usersEncoded[status.index]}" method="post">
                </c:if>
                <tr>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <c:if test="${!user.isAdmin()}">
                        <td>
                            <button class="btn btn-sm btn-success  btn-block" type="submit">
                                Grant Admin Rights
                            </button>
                        </td>
                    </c:if>
                    <c:if test="${user.isAdmin() && user.username != 'Admin'}">
                        <td>
                            <button class="btn btn-sm btn-danger  btn-block" type="submit">
                                Withdraw Admin Rights
                            </button>
                        </td>
                    </c:if>
                    <c:if test="${user.isAdmin() && user.username != 'Admin'}">
                        <td>

                        </td>
                    </c:if>
                </tr>
                <c:if test="${user.username != 'Admin'}">
                </form>
                </c:if>
                </c:forEach>
        </table>
    </c:if>
</div>
</body>