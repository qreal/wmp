<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Server Configuration</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <jsp:include page="../../include/head.jsp"/>

</head>
<body>

<jsp:include page="../../include/navbar.jsp"/>

<!-- panel  -->
<form role="form" class="col-md-6 col-md-offset-3" action="addClient" method="post">

    <div  class="panel panel-default ">
        <div align="center" class="panel-body form-horizontal payment-form">

            <div class="form-group">
                <label for="clientId" class="col-sm-3 control-label">Client ID</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="clientId" name="clientId">
                </div>
            </div>

            <div class="form-group">
                <label for="scopes" class="col-sm-3 control-label">Scopes</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="scopes" name="scopes">
                </div>
            </div>

            <div class="form-group">
                <label for="secret" class="col-sm-3 control-label">Secret</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="secret" name="secret">
                </div>
            </div>

            <%--<div class="form-group">--%>
                <%--<div class="col-sm-12 text-right">--%>
                    <%--<label><input type="checkbox" value="autoApprove" id="autoApprove">Auto approve</label>--%>
                <%--</div>--%>
            <%--</div>--%>

            <div class="form-group">
                <div class="col-sm-12 text-right">
                    <button type="submit" class="btn btn-default preview-add-button">
                        <span class="glyphicon glyphicon-plus"></span> Add
                    </button>
                </div>
            </div>


        </div>
    </div>
</form><!-- / panel  -->

</body>
</html>