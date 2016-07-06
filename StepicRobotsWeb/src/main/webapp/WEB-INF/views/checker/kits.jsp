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

<html>
<head>
    <title><spring:message code="label.kits"/></title>

    <script src="<c:url value='/resources/js/jquery-1.11.3.min.js' />"></script>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/tasks.css' />"/>
</head>
<body>
    <div class="container">
        <div class="col-md-9 col-centered">
            <h2><spring:message code="label.kits"/></h2>
            <br>

            <div style="text-align: left;"><spring:message code="label.language"/>:
                <a href="tasks?locale=ru"><img class="language" src="<c:url value='/resources/css/images/Russia.png' />"/></a>
                <a href="tasks?locale=en"><img class="language" src="<c:url value='/resources/css/images/United-Kingdom.png' />"/></a>
            </div>

            <div id="taskList" class="list-group">
                <div class="list-group-item clearfix">
                    <a class="btn listButton" href="tasks?kit=2014">TRIK (<spring:message code="label.model"/>-2014)</a>
                </div>
                <div class="list-group-item clearfix">
                    <a class="btn listButton" href="tasks?kit=2015">TRIK (<spring:message code="label.model"/>-2015)</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
