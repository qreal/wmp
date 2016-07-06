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
    <title>${name}</title>

    <jsp:include page="../include/scripts.jsp" flush="true"/>
    <script src="<c:url value='/resources/js/jquery.form.min.js' />"></script>
    <script src="<c:url value='/resources/js/bootstrap-filestyle.min.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/offlineSolution.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/twoDModel.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/description.css' />"/>
</head>
<body>
    <div class="container" ng-app ng-controller="RootDiagramController">
        <div class="col-md-9 col-centered">
            <div id="taskContent" ng-controller="CheckerController" task="${id}" kit="${kit}">
                <%@ include file="description.jsp" %>

                <a href="downloadTask/${id}?kit=${kit}&title=${title}"><h4><spring:message code="label.download"/></h4></a>
                <br>

                <h4><spring:message code="label.checkSolution"/></h4>

                <form id="uploadForm" method="POST" action="upload/${id}" enctype="multipart/form-data">
                    <input type="file" name="fileName" id="fileName" class="filestyle" accept=".qrs" data-buttonText="<spring:message code="label.browse"/>" data-buttonName="btn-primary"/>
                </form>
                <br>

                <button id="upload" class="btn btn-success" ng-click="uploadFile()"><spring:message code="label.uploadAndRun"/></button>
                <br>
                <br>

                <div id="spinner" class="spinner">
                </div>

                <div id="result"></div>
                <br>

                <button id="rerun" class="btn btn-primary" ng-click="showResult()"><spring:message code="label.rerun"/></button>
                <br>
                <br>
            </div>
            <%@ include file="twoDModelContent.jsp" %>
        </div>
    </div>
</body>
</html>
