<%@ include file="../include/include.jsp" %>

<html>
<head>
    <script src="<c:url value='/resources/js/libs/bootstrap/bootstrap.min.js' />"></script>
    <link rel="stylesheet" href="<c:url value='/resources/css/bootstrap/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error/error_page.css' />" />
    <title>Error</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>Oops!</h1>
                <h2>
                    ${message}
                </h2>
                <div class="error-details">
                    Sorry, an error has occured.
                </div>
                <div class="error-actions">
                    <a href="<c:url value="/"/>" class="btn btn-primary btn-lg">
                        <span class="glyphicon glyphicon-home"></span>Take Me Home
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
