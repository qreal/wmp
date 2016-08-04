<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Servers Control Panel</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <jsp:include page="../include/head.jsp"/>

</head>

<body>

<jsp:include page="../include/navbar.jsp"/>

<div class="container">
    <table class="table">
        <c:if test="${not empty clients}">
            <tr>
                <th style="text-align:center">Client ID</th>
                <th style="text-align:center">Secret</th>
                <th style="text-align:center"> Scope</th>
            </tr>
            <c:forEach var="o" items="${clients}" varStatus="status">
                <tr>
                    <td align="center">${o.clientId}</td>
                    <td align="center">${o.clientSecret}</td>
                    <td align="center">${o.scope}</td>
                    <td align="center"></td>

                    <td>
                        <a href="clientsPanel/configureClient/${clientsEncoded[status.index]}">
                            <button class="btn btn-default btn-sm" title="Configure">
                                Configure
                            </button>
                        </a>
                    </td>
                </tr>
            </c:forEach>

        </c:if>
    </table>
    <a href="clientsPanel/addClient">
        <button class="btn btn-sm btn-success pull-right" type="submit">
            Add new client
        </button>
    </a>
</div>
</body>