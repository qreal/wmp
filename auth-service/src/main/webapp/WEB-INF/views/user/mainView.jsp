<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
      <title>Servers Available</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <jsp:include page="../include/head.jsp"/>

</head>
<body>

<jsp:include page="../include/navbar.jsp"/>

  <table class="table" align="center" style="width:50%;">
    <tr>
      <th>ID</th>
      <th>Scopes</th>
    </tr>
      <c:forEach var="client" items="${clients}">
          <tr>
              <td >${client.clientId}</td>
              <td >${client.scope}</td>
          </tr>
      </c:forEach>
  </table>
</body>
</html>
