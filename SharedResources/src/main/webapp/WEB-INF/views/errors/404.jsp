<%@ include file="../include/include.jsp" %>

<html>
<head>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error_page.css' />" />
    <title>404 Not Found</title>
</head>
<body>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <div class="error-template">
          <h1>404</h1>
          <h2>Life is pain.</h2>
          <div class="error-details">
            You are here, but the page is not. <br> Go somewhere else. Or don't. There's no point anyway.
          </div>
          <div class="error-actions">
            <a href="<c:url value="/"/>" class="btn btn-primary btn-lg">
              <span class="glyphicon glyphicon-home"></span> Go away.
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
