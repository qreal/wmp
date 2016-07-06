<%@ include file="../include/include.jsp" %>
<%--
  ~ Copyright Vladimir Zakharov
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Robots online</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/base.css' />"/>

    <script src="<c:url value='/resources/js/jquery-1.11.0.min.js' />"></script>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>
    <script src="<c:url value='/resources/js/jquery-ui.min.js' />"></script>

    <script>
        $(document).ready(function () {
            $("#submit").click(function () {
                var program = $('#paper').val();
                var id = $("#robotss option:selected").text();
                var data = "id=" + id + "&program=" + program;
                $.ajax({
                    type: 'POST',
                    url: 'sendProgram',
                    data: data,
                    success: function (data) {

                        alert(data);
                    },
                    error: function (response, status, error) {
                        console.log("error");
                    }
                });
            });
        });
    </script>


</head>

<body>


<%@ include file="../include/navbar.jsp" %>

<div class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">File<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="" role="menuitem" tabindex="-1" ng-click="vm.saveDiagram()">Save</a></li>
                        <li><a href="" role="menuitem" tabindex="-1" ng-click="vm.openDiagram()">Open</a></li>
                    </ul>
                </li>
            </ul>
            <p class="navbar-text" ng-click="vm.removeCurrentElement()">
                <span id="remove" class="glyphicon glyphicon-trash"></span>
            </P>
        </div>
    </div>
</div>

<div id="container">

    <textarea id="paper"></textarea>

    <div id="right-menu">
        <legend style="padding: 10px">Choose Robot</legend>
        <select id="robotss" class="form-control">
            <c:forEach items="${robotsId}" var="robot">
                <option>${robot}</option>
            </c:forEach>
        </select>

        <br/>
        <button class="btn-primary" id="submit">Send!</button>
    </div>
</div>

</body>

</html>